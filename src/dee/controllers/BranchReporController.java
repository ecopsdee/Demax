
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.BranchDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.ProductDAO;
import dee.models.Branch;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class BranchReporController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXComboBox<String> category;
    @FXML
    private JFXComboBox<String> item;
    @FXML
    private Label modelname;
    @FXML
    private TableView<Branch> modelaccounttable;
    @FXML
    private TableColumn<Branch, String> datecol;
    @FXML
    private TableColumn<Branch, String> statuscol;
    @FXML
    private TableColumn<Branch, Integer> incol;
    @FXML
    private TableColumn<Branch, Integer> outcol;
    @FXML
    private TableColumn<Branch, Integer> remaincol;
    @FXML
    private TableView<Branch> summary;
    @FXML
    private TableColumn<Branch, Integer> sumsncol;
    @FXML
    private TableColumn<Branch, String> sumpnamecol;
    @FXML
    private TableColumn<Branch, Integer> sumqtycol;
    @FXML
    private JFXComboBox<String> _subbranch;
    @FXML
    private JFXButton _getbranch;
    @FXML
    private JFXButton _getaccount;

    BranchDAO branchDAO; EmployeeDAO employeeDAO; ProductDAO productDAO; 
    ObservableList<Branch> allproductdetails = FXCollections.observableArrayList();  ObservableList<Branch> _branchaccount = FXCollections.observableArrayList();
    private Executor exec;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); branchDAO = new BranchDAO(); employeeDAO = new EmployeeDAO(); productDAO = new ProductDAO();
        Initcol(); combo();
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
    private void getItem(ActionEvent event) {
        if (category.getValue() != null) {
            item.setItems(branchDAO.getProduct(employeeDAO.getBranchID(MainviewController.getBranch()), branchDAO.subBranchID(category.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))));
        }
    }

    @FXML
    private void getmodel(ActionEvent event) {
    }

    @FXML
    private void getaccount(ActionEvent event) {
        if (category.getValue() != null && item.getValue() != null) {
            getProductAccount(branchDAO.getStockAccount(employeeDAO.getBranchID(MainviewController.getBranch()), branchDAO.subBranchID(category.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), productDAO.getStockID(item.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }
    
    @FXML
    private void _displayBranch(ActionEvent event) {
        
    }

    @FXML
    private void _getbranchaccount(ActionEvent event) {
        if (_subbranch.getValue() != null) {
            getBranchAccount(branchDAO.subBranchID(_subbranch.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }
    
    private void setupHelp(){
        _getbranch.setTooltip(new Tooltip("Display Stock Details "));
        _subbranch.setTooltip(new Tooltip("Select a Branch"));
        category.setTooltip(new Tooltip("Select a Branch"));
        item.setTooltip(new Tooltip("Select a Stock"));
        _getaccount.setTooltip(new Tooltip("Display Stock Details"));
       
    }
    
    private void combo(){
        category.setItems(branchDAO.getBranch(employeeDAO.getBranchID(MainviewController.getBranch())));
        _subbranch.setItems(branchDAO.getBranch(employeeDAO.getBranchID(MainviewController.getBranch())));
    }
    
    private void Initcol(){
       datecol.setCellValueFactory(new PropertyValueFactory("date"));
       statuscol.setCellValueFactory(new PropertyValueFactory("pname"));
       incol.setCellValueFactory(new PropertyValueFactory("qty"));
       outcol.setCellValueFactory(new PropertyValueFactory("uprice"));
       remaincol.setCellValueFactory(new PropertyValueFactory("tprice"));
       modelaccounttable.setItems(allproductdetails);
       
       sumsncol.setCellValueFactory(new PropertyValueFactory("uprice"));
       sumpnamecol.setCellValueFactory(new PropertyValueFactory("pname"));
       sumqtycol.setCellValueFactory(new PropertyValueFactory("qty"));
       summary.setItems(_branchaccount);
       
       
    }
    
    private void getProductAccount(String account, Integer brID){
        Task<ObservableList<Branch>> connect = new Task<ObservableList<Branch>>(){
            @Override
            protected ObservableList<Branch> call() throws Exception {
                return branchDAO.getsubbranchdetails(account, brID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                allproductdetails = connect.get();
                modelname.setText(account);
                modelaccounttable.setItems(allproductdetails);   
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }

    private void getBranchAccount(Integer sbrID, Integer brID){
        Task<ObservableList<Branch>> connect = new Task<ObservableList<Branch>>(){
            @Override
            protected ObservableList<Branch> call() throws Exception {
                return branchDAO.getAllBranchAccount(brID, sbrID);
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                _branchaccount = connect.get();
                summary.setItems(_branchaccount);   
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }
    
   
    
}
