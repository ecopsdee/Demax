
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.ProductDAO;
import dee.DAO.ReportDAO;
import dee.DAO.WarehouseDAO;
import dee.DAO.WarehouseOrderDAO;
import dee.models.Product;
import dee.models.Warehouse;
import dee.models.WarehouseOrder;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class WarehousereportController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private TableView<Warehouse> warehouse;
    @FXML
    private TableColumn<Warehouse, String> wnamecol;
    @FXML
    private TableColumn<Warehouse, String> citycol;
    @FXML
    private TableColumn<Warehouse, String> describecol;
    @FXML
    private TableView<Product> product;
    @FXML
    private TableColumn<Product, Integer> waresncol;
    @FXML
    private TableColumn<Product, String> warepnamecol;
    @FXML
    private TableColumn<Product, Integer> wareTNOGcol;
    @FXML
    private JFXComboBox<String> selectwname;
    @FXML
    private TableView<WarehouseOrder> warehouseorder;
    @FXML
    private TableColumn<WarehouseOrder, String> datecol;
    @FXML
    private TableColumn<WarehouseOrder, String> warehousecol;
    @FXML
    private TableColumn<WarehouseOrder, String> pnameordercol;
    @FXML
    private TableColumn<WarehouseOrder, Integer> NOCcol;
    @FXML
    private JFXDatePicker orderdateA;
    @FXML
    private JFXDatePicker orderdateB;
    @FXML
    private JFXButton _displaywarehouse;
    @FXML
    private JFXButton _printwarehouse;
    @FXML
    private JFXButton _printwarehouseprodct;
    @FXML
    private JFXButton _retrievedata;
    @FXML
    private JFXButton _printwareorder;
    
    ObservableList<Warehouse> warehouselist = FXCollections.observableArrayList(); ObservableList<WarehouseOrder> warehouseorderlist = FXCollections.observableArrayList();
    ObservableList<Product> produclist = FXCollections.observableArrayList();  
    private WarehouseDAO warehouseDAO; private EmployeeDAO employeeDAO; private ProductDAO productDAO; private WarehouseOrderDAO warehouseOrderDAO; private ReportDAO reportDAO;
    private Executor exec;
    
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        warehouseDAO = new WarehouseDAO(); employeeDAO = new EmployeeDAO(); productDAO = new ProductDAO();  warehouseOrderDAO = new WarehouseOrderDAO(); reportDAO = new ReportDAO();
        Initcol(); Fillwarehouse();
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
    private void displaywarehouse(ActionEvent event) {
        viewWarehouse(employeeDAO.getBranchID(MainviewController.getBranch()));
    }

    @FXML
    private void printwarehouse(ActionEvent event) {
        if (!warehouselist.isEmpty()) {
            Alert print = new Alert(Alert.AlertType.CONFIRMATION);
            print.setContentText("Do you want to generate the print out?");
            Optional<ButtonType> result = print.showAndWait();
            if (result.get() == ButtonType.OK) {
                printWarehouse();
            }
        }
    }

    @FXML
    private void getwnamegoods(ActionEvent event) {
        if (selectwname != null) {
            getWnamegoods(employeeDAO.getBranchID(MainviewController.getBranch()), selectwname.getValue());
        }
    }

    @FXML
    private void printwarehouseprodct(ActionEvent event) {
        Alert print = new Alert(Alert.AlertType.CONFIRMATION);
        print.setContentText("Do you want to generate the print out?");
        Optional<ButtonType> result = print.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (selectwname.getValue() != null) {
                printWarehouseDetail(selectwname.getValue());
            }
        }
        
       
    }

    @FXML
    private void printwareorder(ActionEvent event) {
        Alert print = new Alert(Alert.AlertType.CONFIRMATION);
        print.setContentText("Do you want to generate the print out?");
        Optional<ButtonType> result = print.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (orderdateA.getValue() != null && orderdateB.getValue() != null) {
                printWarehouseOrder(orderdateA.getValue().toString(), orderdateB.getValue().toString());
            }else if (orderdateA.getValue() != null && orderdateB.getValue() == null) {
                printWarehouseOrder(orderdateA.getValue().toString(), "");
            } 
        } 
    }
    
    @FXML
    private void retrievedata(ActionEvent event) {
        warehouseorder.getItems().clear();
        if (orderdateA.getValue() != null && orderdateB.getValue() == null) {
            retrieveData(orderdateA.getValue().toString(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
        else if (orderdateA.getValue() != null && orderdateB.getValue() != null) {
            retrieveData(orderdateA.getValue().toString(), orderdateB.getValue().toString(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }
    
    private void Initcol(){
       wnamecol.setCellValueFactory(new PropertyValueFactory("wnamecol"));
       citycol.setCellValueFactory(new PropertyValueFactory("wcitycol"));
       describecol.setCellValueFactory(new PropertyValueFactory("wdescribecol"));
       warehouse.setItems(warehouselist);
       
       waresncol.setCellValueFactory(new PropertyValueFactory("id"));
       warepnamecol.setCellValueFactory(new PropertyValueFactory("prname"));
       wareTNOGcol.setCellValueFactory(new PropertyValueFactory("numberofcartons"));
       product.setItems(produclist);
       
       datecol.setCellValueFactory(new PropertyValueFactory("DATE"));
       warehousecol.setCellValueFactory(new PropertyValueFactory("wname"));
       pnameordercol.setCellValueFactory(new PropertyValueFactory("pname"));
       NOCcol.setCellValueFactory(new PropertyValueFactory("cartonnum"));
       warehouseorder.setItems(warehouseorderlist);
    }
    
    private void viewWarehouse(Integer brID){
        Task<ObservableList<Warehouse>> connect = new Task<ObservableList<Warehouse>>(){
            @Override
            protected ObservableList<Warehouse> call() throws Exception {
                return warehouseDAO.getWarehouse(brID);
            }
        };
        connect.setOnSucceeded(e ->{  
            try {
                warehouselist = connect.get();
                warehouse.setItems(warehouselist); 
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }
    
    private void getWnamegoods(Integer brID, String wname){
        Task<ObservableList<Product>> connect = new Task<ObservableList<Product>>(){
            @Override
            protected ObservableList<Product> call() throws Exception {
                return productDAO.displayProduct(wname);
            }
        };
        connect.setOnSucceeded(e ->{  
            try {
                produclist = connect.get();
                product.setItems(produclist);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);
    }
    
    private void retrieveData(String date, Integer brID){
        Task<ObservableList<WarehouseOrder>> connect = new Task<ObservableList<WarehouseOrder>>(){
            @Override
            protected ObservableList<WarehouseOrder> call() throws Exception {
                return warehouseOrderDAO.getwarehouseorder(date, brID);
            }
        };
        connect.setOnSucceeded(e ->{  
            try {
                warehouseorderlist = connect.get();
                warehouseorder.setItems(warehouseorderlist);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);
    }
    
    private void retrieveData(String date, String date2, Integer brID){
        Task<ObservableList<WarehouseOrder>> connect = new Task<ObservableList<WarehouseOrder>>(){
            @Override
            protected ObservableList<WarehouseOrder> call() throws Exception {
                return warehouseOrderDAO.getwarehouseorder(date, date2, brID);
            }
        };
        connect.setOnSucceeded(e ->{  
            try {
                warehouseorderlist = connect.get();
                warehouseorder.setItems(warehouseorderlist);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);
    }

    private void Fillwarehouse() {
        selectwname.setItems(warehouseDAO.getWarename(employeeDAO.getBranchID(MainviewController.getBranch())));
    }
    
    private void printWarehouse(){
        ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printWarehouse(warehouselist, LoginController.getUsername() , DATE(), LoginController.getPrintLogo(), "", "", "" );
            }
        };
        exec.execute(connect);
        
        connect.setOnSucceeded(e ->{ 
            System.out.println("success is good");
        });
    }
    
    private void printWarehouseOrder(String dateA , String dateB){
        ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printWarehouseOrder(warehouseorderlist, LoginController.getUsername() , dateA, dateB, LoginController.getPrintLogo(), DATE() );
            }
        };
        exec.execute(connect);
    }
    
    private void printWarehouseDetail(String warename){
        ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printWarehouseDetail(produclist, LoginController.getUsername(), DATE(), warename, LoginController.getPrintLogo());
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
