
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.BranchDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.ReportDAO;
import static dee.controllers.CreditreportController.setAccountname;
import dee.models.Branch;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class BranchreportController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXComboBox<String> branchname;
    @FXML
    private JFXDatePicker branchdateA;
    @FXML
    private JFXDatePicker branchdateB;
    @FXML
    private TableView<Branch> branchreport;
    @FXML
    private TableColumn<Branch, String> datecol;
    @FXML
    private TableColumn<Branch, String> modelcol;
    @FXML
    private TableColumn<Branch, Integer> qtycol;
    @FXML
    private TableColumn<Branch, Integer> upricecol;
    @FXML
    private TableColumn<Branch, Integer> totalcol;
    @FXML
    private Label invoicelabel;
    @FXML
    private JFXButton moredetails;
    @FXML
    private JFXButton branchreceipt;
    @FXML
    private JFXButton _getbranch;
    
    private static String account;
    private double xOffset = 0;
    private double yOffset = 0;
    private Executor exec;
    
    ObservableList<Branch> brlist = FXCollections.observableArrayList();
    BranchDAO branchDAO; EmployeeDAO employeeDAO;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); InitCol(); branchDAO = new BranchDAO(); employeeDAO = new EmployeeDAO();
        fillcombo();
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
    private void getbranchaccount(ActionEvent event) {
        if (branchname.getValue() != null) {
            invoicelabel.setText(branchDAO.getBranchBalanceSheet(branchDAO.subBranchID(branchname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()) ), employeeDAO.getBranchID(MainviewController.getBranch()) ));
            setAccount(invoicelabel.getText());
        }
    }

    @FXML
    private void getbranch(ActionEvent event) {
        if (branchdateA.getValue() != null && branchdateB.getValue() == null && branchname.getValue() != null) {
            if (clear()) {
                viewaccountdetails(branchDAO.getBranchAccount(branchDAO.subBranchID(branchname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()) ), employeeDAO.getBranchID(MainviewController.getBranch()) ), employeeDAO.getBranchID(MainviewController.getBranch()), branchdateA.getValue().toString());
            }
        }else if (branchdateA.getValue() != null && branchdateB.getValue() != null && branchname.getValue() != null ) {
            if (clear()) {
                viewaccountdetails(branchDAO.getBranchAccount(branchDAO.subBranchID(branchname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()) ), employeeDAO.getBranchID(MainviewController.getBranch()) ), employeeDAO.getBranchID(MainviewController.getBranch()), branchdateA.getValue().toString(), branchdateB.getValue().toString() );
            }
        }else if (  branchdateA.getValue() == null && branchdateB.getValue() == null && branchname.getValue() != null  ) {
            if (clear()) {
                viewaccountdetails(branchDAO.getBranchAccount(branchDAO.subBranchID(branchname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()) ), employeeDAO.getBranchID(MainviewController.getBranch()) ), employeeDAO.getBranchID(MainviewController.getBranch()));
            } 
        }
    }

    @FXML
    private void openmoredetails(ActionEvent event) {
        if (!invoicelabel.getText().isEmpty()) {
            loadcontrol("/dee/views/BranchBalanceSheet.fxml", "Balance Sheet", moredetails);
        }
    }

    @FXML
    private void handlereceipt(ActionEvent event) {
        if (!brlist.isEmpty()) {
            Alert print = new Alert(Alert.AlertType.CONFIRMATION);
            print.setContentText("Do you want to generate the print out?");
            Optional<ButtonType> result = print.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (branchdateA.getValue() != null && branchdateB.getValue() != null && branchname.getValue() != null) {
                    printCredit(branchdateA.getValue().toString(), branchdateB.getValue().toString(), branchname.getValue());
                }else if (branchdateA.getValue() != null && branchdateB.getValue() == null && branchname.getValue() != null ) {
                    printCredit(branchdateA.getValue().toString(), " ", branchname.getValue());
                }else if (  branchdateA.getValue() == null && branchdateB.getValue() == null && branchname.getValue() != null  ) {
                    printCredit(" ", " ", branchname.getValue());
                }
            }
        }
    }
    
    private Boolean clear(){
        brlist.clear(); branchreport.getItems().clear();
        return true;
    }
    
    private void setupHelp(){
        branchname.setTooltip(new Tooltip("Select a Branch"));
        branchdateA.setTooltip(new Tooltip("Select Staring Date"));
        branchdateB.setTooltip(new Tooltip("Select Ending Date"));
        _getbranch.setTooltip(new Tooltip("Display Branch Details"));
        moredetails.setTooltip(new Tooltip("Open Account Sheet"));
        branchreceipt.setTooltip(new Tooltip("Print"));       
    }
    
    private void printCredit(String dateA, String dateB, String branch){
        ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printBranchOutlet(brlist, LoginController.getUsername(), branch, Date(), dateA, dateB, LoginController.getPrintLogo());
            }
        };
        exec.execute(connect);
    }
    
    private void viewaccountdetails(String account, Integer brID, String date, String date2){
        if (branchDAO.checkBranchAccount(account, brID)) {
            Task<ObservableList<Branch>> connect = new Task<ObservableList<Branch>>(){
                @Override
                protected ObservableList<Branch> call() throws Exception {
                    return branchDAO.getBranchAccountDetails(account, brID, date, date2);
                }
            };
         
            connect.setOnSucceeded(e ->{  
                try {
                    brlist = connect.get();
                    branchreport.setItems(brlist);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }); 
            exec.execute(connect);  
        }    
    }
    
    private void viewaccountdetails(String account, Integer brID, String date){
        if (branchDAO.checkBranchAccount(account, brID)) {
            Task<ObservableList<Branch>> connect = new Task<ObservableList<Branch>>(){
                @Override
                protected ObservableList<Branch> call() throws Exception {
                    return branchDAO.getBranchAccountDetails(account, brID, date);
                }
            };
         
            connect.setOnSucceeded(e ->{  
                try {
                    brlist = connect.get();
                    branchreport.setItems(brlist);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }); 
            exec.execute(connect);            
        }   
    }
    
    private void viewaccountdetails(String account, Integer brID){
        if (branchDAO.checkBranchAccount(account, brID)) {
            Task<ObservableList<Branch>> connect = new Task<ObservableList<Branch>>(){
                @Override
                protected ObservableList<Branch> call() throws Exception {
                    return branchDAO.getBranchAccountDetails(account, brID);
                }
            };
         
            connect.setOnSucceeded(e ->{  
                try {
                    brlist = connect.get();
                    branchreport.setItems(brlist);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }); 
            exec.execute(connect);            
        }   
    }
    
    private void fillcombo(){
        branchname.setItems(branchDAO.getBranch(employeeDAO.getBranchID(MainviewController.getBranch())));
    }
    
    private void InitCol(){
       datecol.setCellValueFactory(new PropertyValueFactory("date"));
       modelcol.setCellValueFactory(new PropertyValueFactory("pname"));
       qtycol.setCellValueFactory(new PropertyValueFactory("qty"));
       upricecol.setCellValueFactory(new PropertyValueFactory("uprice"));
       totalcol.setCellValueFactory(new PropertyValueFactory("tprice"));
       branchreport.setItems(brlist);
      
    }
    
    private void loadcontrol(String fxml, String name, JFXButton OPEN ){
        try {
            Parent employeebody = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(employeebody);
            employeebody.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            Stage stage = new Stage(StageStyle.UNDECORATED);
            employeebody.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            stage.setTitle(name);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(OPEN.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static String Date(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }
    
    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        BranchreportController.account = account;
    }

    
    
}
