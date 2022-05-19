
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.ProductDAO;
import dee.DAO.WarehouseDAO;
import dee.DAO.WarehouseOrderDAO;
import dee.models.WarehouseOrder;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

//in the print section, Put the print in a thread
public class WarehouseorderController implements Initializable {

    @FXML
    private ComboBox<String> warehouse;
    @FXML
    private ComboBox<String> product;
    @FXML
    private JFXTextField noofcarton;
    @FXML
    private DatePicker date;
    @FXML
    private TableView<WarehouseOrder> warehouseordertable;
    @FXML
    private TableColumn<WarehouseOrder, String> wnamecol;
    @FXML
    private TableColumn<WarehouseOrder, String> pnamecol;
    @FXML
    private TableColumn<WarehouseOrder, Integer> noofcartoncol;
    @FXML
    private Label totalcaron;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXButton _add;
    @FXML
    private JFXButton _delete;
    @FXML
    private JFXButton _save;
    @FXML
    private FontAwesomeIconView help_button;
    @FXML
    private TextArea help_area;
    
    SimpleIntegerProperty index = new SimpleIntegerProperty();
    ObservableList<WarehouseOrder>  orderlist = FXCollections.observableArrayList();
    ObservableList<String> prlist = FXCollections.observableArrayList();
    ObservableList<String> warelist = FXCollections.observableArrayList();
    Integer total = 0;
    private EmployeeDAO employeeDAO; private WarehouseDAO warehouseDAO; private ProductDAO productDAO; private WarehouseOrderDAO warehouseOrderDAO;
    
    
   
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employeeDAO = new EmployeeDAO();  warehouseDAO = new WarehouseDAO(); productDAO = new ProductDAO(); warehouseOrderDAO = new WarehouseOrderDAO();
        initcol();
        fillwarename();
    }    


    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void add(ActionEvent event) {
        try{  
            if( !product.getValue().isEmpty() && !warehouse.getValue().isEmpty() && noofcarton.getText().matches("\\d+")){
                Integer PRICE = Integer.parseInt(noofcarton.getText());
                if (checkProductqty(product.getValue(), warehouse.getValue(), PRICE, employeeDAO.getBranchID(MainviewController.getBranch()))) {
                    orderlist.add(new WarehouseOrder("", warehouse.getValue(), product.getValue(), PRICE));
                    total += PRICE;
                    totalcaron.setText(String.valueOf(total));
                    emptydata();
                }else{
                    Alert nosave = new Alert(Alert.AlertType.ERROR);
                    nosave.setContentText("The product requested is more than what is in the Warehouse. \n Please Restock!");
                    nosave.showAndWait();
                }
            }else{ 
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("invalid!");
                alert.showAndWait();
            }
        }catch(NumberFormatException ex){
            
        }
    }

    private void print(ActionEvent event) {
        print();
    }

    @FXML
    private void save(ActionEvent event) {
        if (orderlist.isEmpty() || date.getValue() == null) {
            Alert nosave = new Alert(Alert.AlertType.WARNING);
            nosave.setContentText("Check your Information");
            nosave.showAndWait();       
        }else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Save Product");
                alert.setContentText("Are you sure?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    if (warehouseOrderDAO.CreateOrder(orderlist, employeeDAO.getBranchID(MainviewController.getBranch())) && warehouseOrderDAO.updateProductAccount(orderlist, 0, employeeDAO.getBranchID(MainviewController.getBranch())) && warehouseOrderDAO.updateStockAccount(orderlist, 0, employeeDAO.getBranchID(MainviewController.getBranch())) ) {
                        Alert nosave = new Alert(Alert.AlertType.CONFIRMATION);
                        nosave.setContentText("Saved Successfully!");
                        nosave.showAndWait();
                        print(); 
                        emptyalldata();
                    }else{
                        Alert nosave = new Alert(Alert.AlertType.ERROR);
                        nosave.setContentText("Error occured! try again");
                        nosave.showAndWait();
                    }
                           
                }         
            }
    }

    @FXML
    private void delete(ActionEvent event) {
        if (orderlist.isEmpty()) {
            
        }else{
            int i = index.get(); Integer pricesum = 0;
            if(i > -1){
                
                
//                for (int j = 0; j < orderlist.size(); j++) {
//                    pricesum += orderlist.get(j).getCartonnum();
//                }
                total -= orderlist.get(i).getCartonnum();
                totalcaron.setText(String.valueOf(total));
                orderlist.remove(i);
                warehouseordertable.getSelectionModel().clearSelection();
            }
        }
    }
    
    @FXML
    private void loadproduct(ActionEvent event) {
        if (warehouse.getValue() == null) {
            
        }else{
            fillproduct(warehouse.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
       
    }
    
    @FXML
    private void Help(MouseEvent event) {
        if (checkA()) {
            help_area.setText(productDAO.displayproductinfo(warehouseDAO.getWarehouseId(warehouse.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), productDAO.getProductId(product.getValue(), warehouseDAO.getWarehouseId(warehouse.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))), employeeDAO.getBranchID(MainviewController.getBranch())));
        }
    }

    private void refresh(ActionEvent event) {
        warehouseordertable.getItems().clear();
    }

    private void initcol() {
        index.set(-1);
        wnamecol.setCellValueFactory(new PropertyValueFactory("wname"));
        pnamecol.setCellValueFactory(new PropertyValueFactory("pname"));
        noofcartoncol.setCellValueFactory(new PropertyValueFactory("cartonnum"));
        warehouseordertable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(orderlist.indexOf(newvalue));
            System.out.println("OK, the index is " + orderlist.indexOf(newvalue));
        }); 
        warehouseordertable.setItems(orderlist);
    }
    
    private void fillwarename() {
       warelist = warehouseDAO.getWarename(employeeDAO.getBranchID(MainviewController.getBranch()));
       warehouse.setItems(warelist);
    }
    
    private void fillproduct(String warehousename, Integer brID) {
        if (warehousename.isEmpty()) {
            
        }else{
            prlist = productDAO.getProduct(warehousename, brID);
            product.setItems(prlist);
        }     
    }

    private void emptydata() {
        noofcarton.clear();
        product.getSelectionModel().clearSelection();
        warehouse.getSelectionModel().clearSelection();
    }
    
    private  void emptyalldata(){
        orderlist.clear();
        date.setValue(null);
        totalcaron.setText("");
        total = 0;
    }

    private void print() {
        if (orderlist.isEmpty() && date.getValue() == null) {
           Alert nodata = new Alert(Alert.AlertType.ERROR);
           nodata.setContentText("No data to Print");
           nodata.showAndWait();
        }else{
            
        }
    }
    
    private String displaydateandtime(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }
    
    private Boolean checkProductqty(String stocname, String warehousename ,Integer qtyneeded, Integer brID){
        Boolean status = true;
        if (stocname.isEmpty() && qtyneeded == null) {
            
        }else{
            if (qtyneeded > productDAO.ProductRemaining(productDAO.getProductTablename(stocname, warehousename, brID) )) {
                status = false;
            }
        }
        System.out.println(status);
        return status;
    }

    private Boolean checkA(){
        return product.getValue() != null && warehouse.getValue() != null;
    }

    

    
    
}
