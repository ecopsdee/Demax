
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.PartnerDAO;
import dee.DAO.ReportDAO;
import dee.models.Partner;
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
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class CreditreportController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXDatePicker dateA;
    @FXML
    private JFXDatePicker dateB;
    @FXML
    private JFXTextField accountnumber;
    @FXML
    private JFXTextField credittotal;
    @FXML
    private TableView<Partner> creditreport;
    @FXML
    private TableColumn<Partner, String> creditdate;
    @FXML
    private TableColumn<Partner, String> creditstatus;
    @FXML
    private TableColumn<Partner, Integer> creditinvoicecol;
    @FXML
    private TableColumn<Partner, String> creditstock;
    @FXML
    private TableColumn<Partner, Integer> creditqty;
    @FXML
    private TableColumn<Partner, Integer> creditunitprice;
    @FXML
    private TableColumn<Partner, Integer> credittotalcol;
    @FXML
    private JFXButton creditreceipt;
    @FXML
    private TableView<Partner> partnertable;
    @FXML
    private TableColumn<Partner, String> partnername;
    @FXML
    private TableColumn<Partner, String> partneraccount;
    @FXML
    private TableView<Partner> creditsalestable;
    @FXML
    private TableColumn<Partner, String> CDdate;
    @FXML
    private TableColumn<Partner, Integer> CDinvoice;
    @FXML
    private TableColumn<Partner, Integer> CDcustID;
    @FXML
    private TableColumn<Partner, Integer> CDbalance;
    @FXML
    private JFXButton CDreceipt;
    @FXML
    private JFXButton balancesheet;
    @FXML
    private JFXTextField Searchaccount;
    @FXML
    private JFXButton _getcreditacc;
    @FXML
    private JFXButton _viewallaccount;

    private static String Accountname; private Executor exec;
    private SimpleIntegerProperty index = new SimpleIntegerProperty(); 
    private PartnerDAO partnerDAO; private EmployeeDAO employeeDAO;
    ObservableList<Partner> invoicelist = FXCollections.observableArrayList(); ObservableList<Partner> partnersect = FXCollections.observableArrayList(); ObservableList<Partner> partnersummary = FXCollections.observableArrayList();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setuphelp();  partnerDAO = new PartnerDAO();  employeeDAO = new EmployeeDAO();
        Initcol();
        exec = Executors.newCachedThreadPool(runnable -> {
           Thread t = new Thread(runnable);
           t.setDaemon(true);
           return t;
       });
    }    

    @FXML
    private void closeAction(MouseEvent event) {
        closeallobject(); close.getScene().getWindow().hide();
    }

    @FXML
    private void search(KeyEvent event) {
        FilteredList<Partner> filteredData = new FilteredList<>(partnersect, e -> true);  
        searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Partner>) stock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return  true;
                }
                String lowerCaseFilter = newValue.toLowerCase(); 
                if (stock.getFname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                    return false;
            });
        });
        SortedList<Partner> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(partnertable.comparatorProperty());
        partnertable.setItems(filteredData);
    }

    @FXML
    private void handlecreditreceipt(ActionEvent event) {
        if (!invoicelist.isEmpty()) {
            Alert print = new Alert(Alert.AlertType.CONFIRMATION);
            print.setContentText("Do you want to generate the print out?");
            Optional<ButtonType> result = print.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (dateA.getValue() != null && dateB.getValue() != null && !accountnumber.getText().isEmpty() ) {
                    printCredit(dateA.getValue().toString(), dateB.getValue().toString(), accountnumber.getText(), convert(credittotal.getText()));
                } else if (dateA.getValue() != null && dateB.getValue() == null && !accountnumber.getText().isEmpty()   ) {
                    printCredit(dateA.getValue().toString(), " ", accountnumber.getText(), convert(credittotal.getText()));
                }else if (dateA.getValue() == null && dateB.getValue() == null && !accountnumber.getText().isEmpty()   ) {
                    printCredit(" ", " ", accountnumber.getText(), convert(credittotal.getText()));
                }
            }
        }
    }

    @FXML
    private void handleCDreceipt(ActionEvent event) {
        viewbalancesummary(employeeDAO.getBranchID(MainviewController.getBranch()));
    }

    @FXML
    private void viewballancesheet(ActionEvent event) {
        if (!accountnumber.getText().isEmpty() && !getAccountname().isEmpty() && partnerDAO.checkAccount(accountnumber.getText(), employeeDAO.getBranchID(MainviewController.getBranch()))) {
            loadcontrol("/dee/views/BalanceSheet.fxml", "Balance Sheet", balancesheet);
        }
    }
    
    @FXML
    private void searchallaccount(KeyEvent event) {
        FilteredList<Partner> filteredData = new FilteredList<>(invoicelist, e -> true);  
        Searchaccount.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Partner>) stock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return  true;
                }
                String lowerCaseFilter = newValue.toLowerCase(); 
                if (stock.getStockname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (stock.getLname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (String.valueOf(stock.getTabid()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Partner> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(creditreport.comparatorProperty());
        creditreport.setItems(filteredData);
    }

    @FXML
    private void viewallaccount(ActionEvent event) {
        viewaccount(employeeDAO.getBranchID(MainviewController.getBranch()));
    }

    @FXML
    private void getcreditacc(ActionEvent event) {
        if (dateA.getValue() != null && dateB.getValue() == null && !accountnumber.getText().isEmpty()) {
            if (clear()) {
                viewaccountdetails(accountnumber.getText(), employeeDAO.getBranchID(MainviewController.getBranch()), dateA.getValue().toString());
            }
        }else if (dateA.getValue() != null && dateB.getValue() != null && !accountnumber.getText().isEmpty()) {
            if (clear()) {
                viewaccountdetails(accountnumber.getText(), employeeDAO.getBranchID(MainviewController.getBranch()), dateA.getValue().toString(), dateB.getValue().toString() );
            }
        }else if (!accountnumber.getText().isEmpty() &&  dateB.getValue() == null && dateA.getValue() == null  ) {
            if (clear()) {
                viewaccountdetails(accountnumber.getText(), employeeDAO.getBranchID(MainviewController.getBranch()));
            } 
        }
    }  
    
    private void setuphelp(){
        dateA.setTooltip(new Tooltip("Select the Starting Date"));
        dateB.setTooltip(new Tooltip("Select the Ending Date"));
        _getcreditacc.setTooltip(new Tooltip("Display Account Details"));
        accountnumber.setTooltip(new Tooltip("Enter the Account Numbers"));
        Searchaccount.setTooltip(new Tooltip("Search"));
        creditreceipt.setTooltip(new Tooltip("Print Account Details"));
        balancesheet.setTooltip(new Tooltip("View Balance Sheet"));
        searchField.setTooltip(new Tooltip("Search"));
        _viewallaccount.setTooltip(new Tooltip("Display Partner Account"));
        CDreceipt.setTooltip(new Tooltip("Display Credit Summary"));
    }
    
    private void Initcol(){
        creditdate.setCellValueFactory(new PropertyValueFactory("lname"));
        creditstatus.setCellValueFactory(new PropertyValueFactory("fname"));
        creditstock.setCellValueFactory(new PropertyValueFactory("stockname"));
        creditinvoicecol.setCellValueFactory(new PropertyValueFactory("tabid"));
        creditqty.setCellValueFactory(new PropertyValueFactory("numberofgoods"));
        creditunitprice.setCellValueFactory(new PropertyValueFactory("unitprice"));
        credittotalcol.setCellValueFactory(new PropertyValueFactory("totalprice"));
        creditreport.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(invoicelist.indexOf(newvalue));
            System.out.println("OK, the index is " + invoicelist.indexOf(newvalue));
        });
        creditreport.setItems(invoicelist);
        
        partnername.setCellValueFactory(new PropertyValueFactory("fname"));
        partneraccount.setCellValueFactory(new PropertyValueFactory("lname"));
        partnertable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(partnersect.indexOf(newvalue));
            System.out.println("OK, the index is " + partnersect.indexOf(newvalue));
        });
        partnertable.setItems(partnersect);
        
        
        CDdate.setCellValueFactory(new PropertyValueFactory("tabinvoice"));
        CDinvoice.setCellValueFactory(new PropertyValueFactory("tabpartnername"));
        CDcustID.setCellValueFactory(new PropertyValueFactory("totalprice"));
        CDbalance.setCellValueFactory(new PropertyValueFactory("tabOrder"));
        creditsalestable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(partnersummary.indexOf(newvalue));
            System.out.println("OK, the index is " + partnersummary.indexOf(newvalue));
        });
        creditsalestable.setItems(partnersummary);
    }
    
    private void viewaccount(Integer brID){
        Task<ObservableList<Partner>> connect = new Task<ObservableList<Partner>>(){
            @Override
            protected ObservableList<Partner> call() throws Exception {
                return partnerDAO.getPartnerAccountList(brID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                partnersect = connect.get();
                partnertable.setItems(partnersect); 
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }

    private void viewaccountdetails(String account, Integer brID){
        if (partnerDAO.checkAccount(account, brID)) {
            Task<ObservableList<Partner>> connect = new Task<ObservableList<Partner>>(){
                @Override
                protected ObservableList<Partner> call() throws Exception {
                    return partnerDAO.getPartnerInvoice(account, brID);
                }
            };
         
            connect.setOnSucceeded(e ->{  
                try {
                    invoicelist = connect.get();
                    setAccountname(partnerDAO.getBalanceSheet(account,brID)) ;
                    creditreport.setItems(invoicelist);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }); 
            exec.execute(connect);  
        }else{
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Item does not exist!");
            error.showAndWait();
        } 
    }
    
    private void viewaccountdetails(String account, Integer brID, String date, String date2){
        if (partnerDAO.checkAccount(account, brID)) {
            Task<ObservableList<Partner>> connect = new Task<ObservableList<Partner>>(){
                @Override
                protected ObservableList<Partner> call() throws Exception {
                    return partnerDAO.getPartnerInvoice(account, brID, date, date2);
                }
            };
         
            connect.setOnSucceeded(e ->{  
                try {
                    invoicelist = connect.get();
                    setAccountname(partnerDAO.getBalanceSheet(account,brID)) ;
                    creditreport.setItems(invoicelist);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }); 
            exec.execute(connect);  
        }else{
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Item does not exist!");
            error.showAndWait();
        }     
    }
    
    private void viewaccountdetails(String account, Integer brID, String date){
        if (partnerDAO.checkAccount(account, brID)) {
            Task<ObservableList<Partner>> connect = new Task<ObservableList<Partner>>(){
                @Override
                protected ObservableList<Partner> call() throws Exception {
                    return partnerDAO.getPartnerInvoice(account, brID, date);
                }
            };
         
            connect.setOnSucceeded(e ->{  
                try {
                    invoicelist = connect.get();
                    setAccountname(partnerDAO.getBalanceSheet(account,brID)) ;
                    creditreport.setItems(invoicelist);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }); 
            exec.execute(connect);            
        }else{
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Item does not exist!");
            error.showAndWait();
        }    
    }
    
    private void viewbalancesummary(Integer brID){
        Task<ObservableList<Partner>> connect = new Task<ObservableList<Partner>>(){
            @Override
            protected ObservableList<Partner> call() throws Exception {
                return partnerDAO.getPartnerSummary(brID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                partnersummary = connect.get();
                creditsalestable.setItems(partnersummary);   
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
        
    }
    
    private void printCredit(String dateA, String dateB, String account, Integer total){
        ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printCredit(invoicelist, LoginController.getUsername(), account, dateA, dateB, Date(), total, LoginController.getPrintLogo());
            }
        };
        exec.execute(connect);
    }
    
    private Integer convert(String data){
        return !data.isEmpty() && data.matches("\\d+") ? Integer.parseInt(data) : 0;
    }
    
    private void loadcontrol(String fxml, String name, JFXButton OPEN ){
        try {
            Parent employeebody = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(employeebody);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle(name);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(OPEN.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void closeallobject(){
        setAccountname(""); 
    }
    
    public static String Date(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }
    
    private Boolean clear(){
        invoicelist.clear(); creditreport.getItems().clear();
        return true;
    }

    public static String getAccountname() {
        return Accountname;
    }

    public static void setAccountname(String Accountname) {
        CreditreportController.Accountname = Accountname;
    }

    
    

    
    
}
