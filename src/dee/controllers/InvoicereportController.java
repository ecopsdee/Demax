
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.GoodReturnDAO;
import dee.DAO.InvoiceDAO;
import dee.DAO.ReportDAO;
import dee.models.Employee;
import dee.models.Invoice;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;


public class InvoicereportController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXDatePicker invoicedateA;
    @FXML
    private JFXDatePicker invoicedateB;
    @FXML
    private JFXTextField invoiceinvoice;
    @FXML
    private JFXTextField invoicetotal;
    @FXML
    private TableView<Invoice> invoicereport;
    @FXML
    private TableColumn<Invoice, String> invoicedate;
    @FXML
    private TableColumn<Invoice, Integer> invoicecol;
    @FXML
    private TableColumn<Invoice, String> invoiceowner;
    @FXML
    private TableColumn<Invoice, String> invoicecustomer;
    @FXML
    private TableColumn<Invoice, Integer> invoicecustomerID;
    @FXML
    private TableColumn<Invoice, Integer> invoicebalance;
    @FXML
    private TableColumn<Invoice, Integer> invoicetotalcol;
    @FXML
    private JFXButton invoicereceipt;
    @FXML
    private JFXTextField returntotal;
    @FXML
    private TableView<Invoice> returntable;
    @FXML
    private TableColumn<Invoice, String> returndate;
    @FXML
    private TableColumn<Invoice, Integer> returninvoice;
    @FXML
    private TableColumn<Invoice, String> returnstock;
    @FXML
    private TableColumn<Invoice, Integer> returnqty;
    @FXML
    private JFXDatePicker returndateA;
    @FXML
    private JFXDatePicker returndateB;
    @FXML
    private JFXTextField returnINVOICE;
    @FXML
    private JFXButton moredetails;
    @FXML
    private Label invoicelabel;
    @FXML
    private FontAwesomeIconView serach;
    @FXML
    private JFXButton _getinvoice;
    @FXML
    private JFXButton _getreturndetail;
   
    
    ObservableList<Invoice> allinvoicedetails = FXCollections.observableArrayList();
    ObservableList<Invoice> allreturn = FXCollections.observableArrayList();
    private InvoiceDAO invoiceDAO; private EmployeeDAO employeeDAO; private GoodReturnDAO goodReturnDAO;
    private Executor exec; 
    private static Integer InvoiceNO;
    private double xOffset = 0;
    private double yOffset = 0;
    

    
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); Initcol();  invoiceDAO = new InvoiceDAO(); employeeDAO = new EmployeeDAO(); goodReturnDAO = new GoodReturnDAO();
        exec = Executors.newCachedThreadPool(runnable -> {
           Thread t = new Thread(runnable);
           t.setDaemon(true);
           return t;
       });
    }    

    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void getinvoice(ActionEvent event) {
        if (invoicedateA.getValue() != null && invoicedateB.getValue() != null) {
            viewInvoiceaccount(employeeDAO.getBranchID(MainviewController.getBranch()), invoicedateA.getValue().toString(), invoicedateB.getValue().toString());
        }else if (invoicedateA.getValue() != null && invoicedateB.getValue() == null) {
            viewInvoiceaccount(employeeDAO.getBranchID(MainviewController.getBranch()), invoicedateA.getValue().toString());
        }
    }

    @FXML
    private void handleInvoicereceipt(ActionEvent event) {
        if (!allinvoicedetails.isEmpty()) {
            Alert print = new Alert(Alert.AlertType.CONFIRMATION);
            print.setContentText("Do you want to generate the print out?");
            Optional<ButtonType> result = print.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (invoicedateA.getValue() != null && invoicedateB.getValue() != null ) {
                    printInvoice(invoicedateA.getValue().toString(), invoicedateB.getValue().toString(), convert(invoiceinvoice.getText()), convert(invoicetotal.getText()));
                } else if (invoicedateA.getValue() != null && invoicedateB.getValue() == null ) {
                    printInvoice(invoicedateA.getValue().toString(), "", convert(invoiceinvoice.getText()), convert(invoicetotal.getText()));
                }
            }
        }
        
    }

    @FXML
    private void searchinvoice(KeyEvent event) {
        FilteredList<Invoice> filteredData = new FilteredList<>(allinvoicedetails, e -> true);  
            invoiceinvoice.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Invoice>) stock -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return  true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase(); 
                    if (String.valueOf(stock.getId()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (stock.getProductname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (String.valueOf(stock.getQuantity()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;    
                });
            });
            SortedList<Invoice> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(invoicereport.comparatorProperty());
            invoicereport.setItems(filteredData);
    }

    @FXML
    private void getreturndetail(ActionEvent event) {
        if (returndateA.getValue() != null && returndateB.getValue() != null) {
            viewReturnaccount(employeeDAO.getBranchID(MainviewController.getBranch()), returndateA.getValue().toString(), returndateB.getValue().toString());
        }else if (returndateA.getValue() != null && returndateB.getValue() == null) {
            viewReturnaccount(employeeDAO.getBranchID(MainviewController.getBranch()), returndateA.getValue().toString());
        }
    }
    
    @FXML
    private void openmoredetails(ActionEvent event) {
        if (!invoicelabel.getText().isEmpty()) {
            loadwindow("/dee/views/SalesBookreport.fxml", "SalesBook");
        }
    }
    
    private void setupHelp(){
        invoicedateA.setTooltip(new Tooltip("Select Starting Date"));
        invoicedateB.setTooltip(new Tooltip("Select Ending Date"));
        _getinvoice.setTooltip(new Tooltip("Display Invoice Details"));
        invoiceinvoice.setTooltip(new Tooltip("Enter Invoice"));
        moredetails.setTooltip(new Tooltip("More Invoice Details"));
        invoicereceipt.setTooltip(new Tooltip("Print Receipt"));
        returndateA.setTooltip(new Tooltip("Select Starting Date"));
        returndateB.setTooltip(new Tooltip("Select Ending Date"));
        _getreturndetail.setTooltip(new Tooltip("Display Return Sales"));
        returnINVOICE.setTooltip(new Tooltip("Enter Invoice"));
        
        
    }
    
    private void Initcol(){
       
       invoicereport.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());
       invoicedate.setCellValueFactory(new PropertyValueFactory("date"));
       invoicecol.setCellValueFactory(new PropertyValueFactory("id"));
       invoiceowner.setCellValueFactory(new PropertyValueFactory("status"));
       invoicecustomer.setCellValueFactory(new PropertyValueFactory("productname"));
       invoicecustomerID.setCellValueFactory(new PropertyValueFactory("quantity"));
       invoicetotalcol.setCellValueFactory(new PropertyValueFactory("totalprice"));
       invoicebalance.setCellValueFactory(new PropertyValueFactory("profit"));
       invoicereport.setItems(allinvoicedetails);
       
       returndate.setCellValueFactory(new PropertyValueFactory("date"));
       returninvoice.setCellValueFactory(new PropertyValueFactory("invoicenum"));
       returnstock.setCellValueFactory(new PropertyValueFactory("productname"));
       returnqty.setCellValueFactory(new PropertyValueFactory("quantity"));
       returntable.setItems(allreturn);

    }
    
    private void viewInvoiceaccount( Integer brID, String date, String date2){
        Task<ObservableList<Invoice>> connect = new Task<ObservableList<Invoice>>(){
            @Override
            protected ObservableList<Invoice> call() throws Exception {
                return invoiceDAO.getInvoiceDetails(date, date2, brID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                allinvoicedetails = connect.get();
                invoicereport.setItems(allinvoicedetails);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);  
    }
    
    private void viewInvoiceaccount( Integer brID, String date){
        Task<ObservableList<Invoice>> connect = new Task<ObservableList<Invoice>>(){
            @Override
            protected ObservableList<Invoice> call() throws Exception {
                return invoiceDAO.getInvoiceDetails(date, brID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                allinvoicedetails = connect.get();
                invoicereport.setItems(allinvoicedetails);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);  
    }
    
    private void viewReturnaccount( Integer brID, String date, String date2){
        Task<ObservableList<Invoice>> connect = new Task<ObservableList<Invoice>>(){
            @Override
            protected ObservableList<Invoice> call() throws Exception {
                return goodReturnDAO.displayGoodReturn(date, date2, brID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                allreturn = connect.get();
                returntable.setItems(allreturn);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);  
    }
    
    private void viewReturnaccount( Integer brID, String date){
        Task<ObservableList<Invoice>> connect = new Task<ObservableList<Invoice>>(){
            @Override
            protected ObservableList<Invoice> call() throws Exception {
                return goodReturnDAO.displayGoodReturn(date, brID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                allreturn = connect.get();
                returntable.setItems(allreturn);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);  
    }
    
    private void printInvoice(String fromdate, String todate, Integer invoice, Integer total){
        ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printInvoice(allinvoicedetails, LoginController.getUsername(), fromdate, todate, DATE(), invoice, total, LoginController.getPrintLogo());
            }
        };
        exec.execute(connect);
    }
    
    private void loadwindow(String loc, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle(title);
            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Integer convert(String data){
        return !data.isEmpty() && data.matches("\\d+") ? Integer.parseInt(data) : 0;
    }
    
    private class RowSelectChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
            int ix = newVal.intValue();
            if ((ix < 0) || (ix >= allinvoicedetails.size())) {
                return; // invalid data
            }
            Invoice detail = allinvoicedetails.get(ix);
            setInvoiceNO(detail.getId());
            invoicelabel.setText(String.valueOf(detail.getId()));
        }
    }

    public static String DATE(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }

    public static Integer getInvoiceNO() {
        return InvoiceNO;
    }

    public static void setInvoiceNO(Integer InvoiceNO) {
        InvoicereportController.InvoiceNO = InvoiceNO;
    }
    
    
    
    
}

