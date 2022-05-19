
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.BranchDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.ReportDAO;
import dee.models.Branch;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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


public class BranchBalanceSheetController implements Initializable {

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
    private TableView<Branch> updatetable;
    @FXML
    private TableColumn<Branch, String> updatedate;
    @FXML
    private TableColumn<Branch, Integer> updatepayment;
    @FXML
    private TableColumn<Branch, Integer> updatecharges;
    @FXML
    private TableColumn<Branch, Integer> updatebalance;
    @FXML
    private JFXButton printsheet;
    @FXML
    private JFXButton _getbalancesheet;
    @FXML
    private JFXButton _updateamountpaid;
    @FXML
    private JFXButton _viewbalancesheet;

    private Executor exec;
    ObservableList<Branch> brlist = FXCollections.observableArrayList();
    BranchDAO branchDAO; EmployeeDAO employeeDAO;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); Initcol(); branchDAO = new BranchDAO(); employeeDAO = new EmployeeDAO();
        parrnerbalancesheetaccount.setText(BranchreportController.getAccount());
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
                    Alert ok = new Alert(Alert.AlertType.INFORMATION);
                    ok.setContentText("Account Update successful");
                    ok.showAndWait();
                    updateamount.clear();
                }
            }
        }      
    }

    @FXML
    private void viewbalancesheet(ActionEvent event) {
        viewBalancesheet(parrnerbalancesheetaccount.getText());
    }

    @FXML
    private void handleprintsheet(ActionEvent event) {
        if (!brlist.isEmpty()) {
            Alert print = new Alert(Alert.AlertType.CONFIRMATION);
            print.setContentText("Do you want to generate the print out?");
            Optional<ButtonType> result = print.showAndWait();
            if (result.get() == ButtonType.OK) { 
                if (dateA.getValue() != null && dateB.getValue() != null && !parrnerbalancesheetaccount.getText().isEmpty() ) {
                    printBalanceSheet(dateA.getValue().toString(), dateB.getValue().toString(), parrnerbalancesheetaccount.getText());
                }else if (dateA.getValue() != null && dateB.getValue() == null && !parrnerbalancesheetaccount.getText().isEmpty() ) {
                    printBalanceSheet(dateA.getValue().toString(), "", parrnerbalancesheetaccount.getText());
                }else if (dateA.getValue() == null && dateB.getValue() == null && !parrnerbalancesheetaccount.getText().isEmpty() ) {
                    printBalanceSheet("", "", parrnerbalancesheetaccount.getText());
                }
            }
            
        }
                        
    }
    
    private void setupHelp(){
        dateA.setTooltip(new Tooltip("Select the Starting Date"));
        dateB.setTooltip(new Tooltip("Select the Ending Date"));
        _getbalancesheet.setTooltip(new Tooltip("Display Balance Sheet with respect to date selected"));
        updateamount.setTooltip(new Tooltip("Enter the Amount received "));
        _updateamountpaid.setTooltip(new Tooltip("Update Balance Sheet Account"));
        _viewbalancesheet.setTooltip(new Tooltip("Display Balance Sheet"));
        printsheet.setTooltip(new Tooltip("SPrint Balance Sheet"));
        
    }
    
    private void viewBalancesheet(String balanceaccount){
        brlist = branchDAO.getPartnerBalanceSheet(balanceaccount);
        updatetable.setItems(brlist);
    }
    
    private void viewaccountdetails(String account, String date, String date2){
        brlist = branchDAO.getPartnerBalanceSheet(account, date, date2);
        updatetable.setItems(brlist);
    }
    
    private void viewaccountdetails(String account, String date){
        brlist = branchDAO.getPartnerBalanceSheet(account, date);
        updatetable.setItems(brlist);  
    }
    
    private void printBalanceSheet(String dateA, String dateB, String name){
    ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printBranchSheet(brlist, LoginController.getUsername(), name, dateA, dateB, DATE(), LoginController.getPrintLogo());
            }
        };
        exec.execute(connect);
    }
    
    private Boolean clear(){
        brlist.clear(); updatetable.getItems().clear();
        return true;
    }
    
    private Boolean updateamount(Integer amount, String account){
        Boolean status = false;
        if (branchDAO.SaveUpdateBalanceSheet(amount, account)) {
            status = true;
        }
        return status;
    }
    
    public static String DATE(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }

    
    private void Initcol(){
        updatedate.setCellValueFactory(new PropertyValueFactory("date"));
        updatepayment.setCellValueFactory(new PropertyValueFactory("qty"));
        updatecharges.setCellValueFactory(new PropertyValueFactory("uprice"));
        updatebalance.setCellValueFactory(new PropertyValueFactory("tprice"));
        updatetable.setItems(brlist);
    }
    
}
