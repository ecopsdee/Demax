
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.PartnerDAO;
import dee.DAO.ProductDAO;
import dee.DAO.ReportDAO;
import dee.models.Partner;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class BalanceSheetController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXDatePicker dateA;
    @FXML
    private JFXDatePicker dateB;
    @FXML
    private JFXTextField updateamount;
    @FXML
    private Label parrnerbalancesheetaccount;
    @FXML
    private TableView<Partner> updatetable;
    @FXML
    private TableColumn<Partner, String> updatedate;
    @FXML
    private TableColumn<Partner, Integer> updatepayment;
    @FXML
    private TableColumn<Partner, Integer> updatecharges;
    @FXML
    private TableColumn<Partner, Integer> updatebalance;
    @FXML
    private JFXButton printsheet;
    @FXML
    private JFXButton _getbalancesheet;
    @FXML
    private JFXButton _updateamountpaid;
    @FXML
    private JFXButton _viewbalancesheet;

    private SimpleIntegerProperty index = new SimpleIntegerProperty();
    ObservableList<Partner> invoicelist = FXCollections.observableArrayList();
    private PartnerDAO partnerDAO; private Executor exec; private ProductDAO productDAO; private EmployeeDAO employeeDAO;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); partnerDAO = new PartnerDAO(); productDAO = new ProductDAO(); employeeDAO = new EmployeeDAO(); parrnerbalancesheetaccount.setText(CreditreportController.getAccountname());
        Initcol();
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
    private void getbalancesheet(ActionEvent event) {
        if (dateA.getValue() != null && dateB.getValue() == null && !parrnerbalancesheetaccount.getText().isEmpty()) {
            if (clear()) {
                viewaccountdetails(parrnerbalancesheetaccount.getText(),  dateA.getValue().toString());
            }
        }else if (dateA.getValue() != null && dateB.getValue() != null && !parrnerbalancesheetaccount.getText().isEmpty()) {
            if (clear()) {
                viewaccountdetails(parrnerbalancesheetaccount.getText(), dateA.getValue().toString(), dateB.getValue().toString() );
            }
        }
    }

    @FXML
    private void updateamountpaid(ActionEvent event) {
        if (!parrnerbalancesheetaccount.getText().isEmpty() && updateamount.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to Update this account");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (updateamount(Integer.parseInt(updateamount.getText()), parrnerbalancesheetaccount.getText())) {
                    if (productDAO.createAnalysis(employeeDAO.getBranchID(MainviewController.getBranch()), 0, "Money paid by " + partnerDAO.getPartnerName(parrnerbalancesheetaccount.getText(), employeeDAO.getBranchID(MainviewController.getBranch())), Integer.parseInt(updateamount.getText()))) {
                        Alert ok = new Alert(Alert.AlertType.INFORMATION);
                        ok.setContentText("Account Update successful");
                        ok.showAndWait();
                        updateamount.clear();
                    }
                    
                }
            }
        }      
    }

    @FXML
    private void viewbalancesheet(ActionEvent event) {
        viewbalancesheet();
    }

    @FXML
    private void handleprintsheet(ActionEvent event) {
        if (!invoicelist.isEmpty()) {
            Alert print = new Alert(Alert.AlertType.CONFIRMATION);
            print.setContentText("Do you want to generate the print out?");
            Optional<ButtonType> result = print.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (dateA.getValue() != null && dateB.getValue() != null ) {
                    printBalanceSheet(dateA.getValue().toString(), dateB.getValue().toString());
                }else if (dateA.getValue() != null && dateB.getValue() == null ) {
                    printBalanceSheet(dateA.getValue().toString(), "");
                }else{
                    printBalanceSheet("", "");
                }
            }     
        }
    }
    
    private void setupHelp(){
        dateA.setTooltip(new Tooltip("Select the Starting Date "));
        dateB.setTooltip(new Tooltip("Select the Ending Date "));
        _getbalancesheet.setTooltip(new Tooltip("Display the Balance Sheet with respect to Date selected. "));
        _updateamountpaid.setTooltip(new Tooltip("Enter the Amount paid by the Partner"));
        _viewbalancesheet.setTooltip(new Tooltip("Display the Balance Sheet of the selected Partner "));
        printsheet.setTooltip(new Tooltip("Print the Details of the Balance Sheet "));
    }
    
    private void Initcol(){
        updatedate.setCellValueFactory(new PropertyValueFactory("lname"));
        updatepayment.setCellValueFactory(new PropertyValueFactory("numberofgoods"));
        updatecharges.setCellValueFactory(new PropertyValueFactory("unitprice"));
        updatebalance.setCellValueFactory(new PropertyValueFactory("totalprice"));
        updatetable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(invoicelist.indexOf(newvalue));
            System.out.println("OK, the index is " + invoicelist.indexOf(newvalue));
        });
        updatetable.setItems(invoicelist);
    }
    
    private void viewbalancesheet(){
        invoicelist = partnerDAO.getPartnerBalanceSheet(parrnerbalancesheetaccount.getText());
        updatetable.setItems(invoicelist);
    }
    
    private void viewaccountdetails(String account, String date, String date2){
        invoicelist = partnerDAO.getPartnerBalanceSheet(account, date, date2);
        updatetable.setItems(invoicelist);
    }
    
    private void viewaccountdetails(String account, String date){
        invoicelist = partnerDAO.getPartnerBalanceSheet(account, date);
        updatetable.setItems(invoicelist);  
    }
    
    private Boolean clear(){
        invoicelist.clear(); updatetable.getItems().clear();
        return true;
    }
    
    private Boolean updateamount(Integer amount, String account){
        return partnerDAO.SaveUpdateBalanceSheet(amount, account);
    }
    
    private void printBalanceSheet(String dateA, String dateB){
    ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printBalanceSheet(invoicelist, LoginController.getUsername(), CreditreportController.getAccountname(), dateA, dateB, DATE(), LoginController.getPrintLogo());
            }
        };
        exec.execute(connect);
    }
    
    public static String DATE(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }

    
}
