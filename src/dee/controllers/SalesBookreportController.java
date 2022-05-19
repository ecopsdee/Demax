
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.InvoiceDAO;
import dee.DAO.ReportDAO;
import static dee.controllers.InvoicereportController.DATE;
import dee.models.Invoice;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class SalesBookreportController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXTextField total;
    @FXML
    private TableView<Invoice> dailyreport;
    @FXML
    private TableColumn<Invoice, String> salesdate;
    @FXML
    private TableColumn<Invoice, Integer> salesinvoice;
    @FXML
    private TableColumn<Invoice, String> salesstock;
    @FXML
    private TableColumn<Invoice, Integer> salesqty;
    @FXML
    private TableColumn<Invoice, Integer> salesunitprice;
    @FXML
    private TableColumn<Invoice, Integer> salestotalprice;
    @FXML
    private TableColumn<Invoice, String> salesstatus;
    @FXML
    private TableColumn<Invoice, Integer> salesqtyreturn;
    @FXML
    private TableColumn<Invoice, Integer> salesqtyremain;
    @FXML
    private TableColumn<Invoice, Integer> salestotal;
    @FXML
    private JFXButton receipt;
    @FXML
    private JFXButton _seegraph;
    
    ObservableList<Invoice> allinvoice = FXCollections.observableArrayList();
    private InvoiceDAO invoiceDAO; private EmployeeDAO employeeDAO;
    private Executor exec;
  

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); Initcol();  invoiceDAO = new InvoiceDAO(); employeeDAO = new EmployeeDAO();
        exec = Executors.newCachedThreadPool(runnable -> {
           Thread t = new Thread(runnable);
           t.setDaemon(true);
           return t;
        });
        viewaccountdetails(employeeDAO.getBranchID(MainviewController.getBranch()), InvoicereportController.getInvoiceNO());
    }    

    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void seegraph(ActionEvent event) {
    }

    @FXML
    private void handleReceipt(ActionEvent event) {
        if (!allinvoice.isEmpty()) {
            Alert print = new Alert(Alert.AlertType.CONFIRMATION);
            print.setContentText("Do you want to generate the print out?");
            Optional<ButtonType> result = print.showAndWait();
            if (result.get() == ButtonType.OK) {
                printDailySales("", "", InvoicereportController.getInvoiceNO(), Integer.parseInt(total.getText())); 
            }
        }else{
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Error occurred! \nPlease review.");
            error.showAndWait();
        }
        
    }
    
    private void setupHelp(){
        receipt.setTooltip(new Tooltip("Print"));
    }
    
    private void viewaccountdetails( Integer brID, Integer invoice){
        Task<ObservableList<Invoice>> connect = new Task<ObservableList<Invoice>>(){
            @Override
            protected ObservableList<Invoice> call() throws Exception {
                return invoiceDAO.getInvoice(invoice, brID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                allinvoice = connect.get();
                dailyreport.setItems(allinvoice);
                Total(allinvoice);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);  
  
    }
    
    private void Total(ObservableList<Invoice> invoiceLIST){
        Integer tot = 0;
        for (Invoice invoice : invoiceLIST) {
            tot += invoice.getTotalprofit();
        }
        total.setText(String.valueOf(tot));
    }
    
    private void Initcol(){
       salesdate.setCellValueFactory(new PropertyValueFactory("date"));
       salesinvoice.setCellValueFactory(new PropertyValueFactory("id"));
       salesstock.setCellValueFactory(new PropertyValueFactory("productname"));
       salesqty.setCellValueFactory(new PropertyValueFactory("quantity"));
       salesunitprice.setCellValueFactory(new PropertyValueFactory("unitprice"));
       salestotalprice.setCellValueFactory(new PropertyValueFactory("totalprice"));
       salesstatus.setCellValueFactory(new PropertyValueFactory("status"));
       salesqtyreturn.setCellValueFactory(new PropertyValueFactory("profit"));
       salesqtyremain.setCellValueFactory(new PropertyValueFactory("loss"));
       salestotal.setCellValueFactory(new PropertyValueFactory("totalprofit"));
       dailyreport.setItems(allinvoice);
    }
    
    private void printDailySales(String dateA, String dateB, Integer invoice, Integer total){
        ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printDailySales(allinvoice, LoginController.getUsername(), dateA, dateB, DATE(), invoice, total, LoginController.getPrintLogo());
            }
        };
        exec.execute(connect);
    }
    
}
