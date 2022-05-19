
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.ProductDAO;
import dee.DAO.WarehouseDAO;
import dee.models.Product;
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


public class ProductreportController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXComboBox<String> category;
    @FXML
    private JFXComboBox<String> item;
    @FXML
    private Label modelname;
    @FXML
    private TableView<Product> modelaccounttable;
    @FXML
    private TableColumn<Product, String> datecol;
    @FXML
    private TableColumn<Product, String> statuscol;
    @FXML
    private TableColumn<Product, Integer> incol;
    @FXML
    private TableColumn<Product, Integer> outcol;
    @FXML
    private TableColumn<Product, Integer> remaincol;   
    @FXML
    private JFXComboBox<String> modelitem;
    @FXML
    private Label modelaccountname;
    @FXML
    private TableView<Product> modeltable;
    @FXML
    private TableColumn<Product, String> modeldate;
    @FXML
    private TableColumn<Product, String> modelstatus;
    @FXML
    private TableColumn<Product, Integer> modelinvoice;
    @FXML
    private TableColumn<Product, Integer> modelinstock;
    @FXML
    private TableColumn<Product, Integer> modeloutstock;
    @FXML
    private TableColumn<Product, Integer> modelremainstock;
    @FXML
    private JFXButton _getaccount;
    @FXML
    private JFXButton _getmodelaccount;
    
    ObservableList<Product> allproductdetails = FXCollections.observableArrayList();  ObservableList<Product> allmodeldetails = FXCollections.observableArrayList();
    private WarehouseDAO warehouseDAO; private EmployeeDAO employeeDAO; public ProductDAO productDAO;
    private Executor exec;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp();    warehouseDAO = new WarehouseDAO();  employeeDAO = new EmployeeDAO();  productDAO = new ProductDAO();
        Initcol(); getwarehouse(employeeDAO.getBranchID(MainviewController.getBranch())); getallModel(employeeDAO.getBranchID(MainviewController.getBranch()));
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
            getModel(category.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }

    @FXML
    private void getmodel(ActionEvent event) {
    }

    @FXML
    private void getaccount(ActionEvent event) {
        if (category.getValue() != null && item.getValue() != null) {
            getProductAccount(category.getValue(), item.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }
    
    @FXML
    private void getmodelaccount(ActionEvent event) {
        if (modelitem.getValue() != null) {
            getModelAccount( modelitem.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }
    
    private void setupHelp(){
        category.setTooltip(new Tooltip("Select Warehouse"));
        item.setTooltip(new Tooltip("Select Stock"));
        _getaccount.setTooltip(new Tooltip("Display"));
        modelitem.setTooltip(new Tooltip("Select Stock"));
        _getmodelaccount.setTooltip(new Tooltip("Display"));
        
    }
    
    private void Initcol(){
       datecol.setCellValueFactory(new PropertyValueFactory("date"));
       statuscol.setCellValueFactory(new PropertyValueFactory("status"));
       incol.setCellValueFactory(new PropertyValueFactory("totalnumberofgoods"));
       outcol.setCellValueFactory(new PropertyValueFactory("totalNumberOfGoodsRemaining"));
       remaincol.setCellValueFactory(new PropertyValueFactory("numberofcartons"));
       modelaccounttable.setItems(allproductdetails);
       
       modeldate.setCellValueFactory(new PropertyValueFactory("date"));
       modelstatus.setCellValueFactory(new PropertyValueFactory("status"));
       modelinstock.setCellValueFactory(new PropertyValueFactory("totalnumberofgoods"));
       modeloutstock.setCellValueFactory(new PropertyValueFactory("totalNumberOfGoodsRemaining"));
       modelinvoice.setCellValueFactory(new PropertyValueFactory("numberofPacket"));
       modelremainstock.setCellValueFactory(new PropertyValueFactory("numberofcartons"));
       modeltable.setItems(allmodeldetails);
    }

    private void getwarehouse(Integer brID) {
        category.setItems(warehouseDAO.getWarename(brID));
    }
    
    private void getModel(String warename, Integer brID){
        item.setItems(productDAO.getProduct(warename, brID));
    }
    
    private void getProductAccount(String wname, String pname, Integer brID){
        Task<ObservableList<Product>> connect = new Task<ObservableList<Product>>(){
            @Override
            protected ObservableList<Product> call() throws Exception {
                return productDAO.productoveralldetails(productDAO.getProductTablename(pname, wname, brID));
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                allproductdetails = connect.get();
                modelname.setText(productDAO.getProductTablename(pname, wname, brID));
                modelaccounttable.setItems(allproductdetails);   
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }
    
    private void getallModel(Integer brID){
        modelitem.setItems(productDAO.getProduct(brID));
    }

    private void getModelAccount(String pname, Integer brID){
        Task<ObservableList<Product>> connect = new Task<ObservableList<Product>>(){
            @Override
            protected ObservableList<Product> call() throws Exception {
                return productDAO.displaystockdetails(productDAO.getStockTablename(productDAO.getStockID(pname, brID), brID));
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                allmodeldetails = connect.get();
                modelaccountname.setText(productDAO.getStockTablename(productDAO.getStockID(pname, brID), brID));
                modeltable.setItems(allmodeldetails);   
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }
    
}
