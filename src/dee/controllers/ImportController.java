
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.ImportDAO;
import dee.DAO.ProductDAO;
import dee.DAO.WarehouseDAO;
import dee.models.Import;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class ImportController implements Initializable {

    @FXML
    private ComboBox<String> wname;
    @FXML
    private ComboBox<String> pname;
    @FXML
    private TextField noofcart;
    @FXML
    private DatePicker date;
    @FXML
    private TableView<Import> importtable;
    @FXML
    private TableColumn<Import, String> pnamecol;
    @FXML
    private TableColumn<Import, String> wnamecol;
    @FXML
    private TableColumn<Import, Integer> noofcartcol;
    @FXML
    private Label totalqty;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXButton _add;
    @FXML
    private JFXButton _clear;
    @FXML
    private JFXButton _save;
    @FXML
    private FontAwesomeIconView help_button;
    @FXML
    private TextArea help_area;
    @FXML
    private JFXComboBox<String> status;
    
    
    SimpleIntegerProperty index = new SimpleIntegerProperty();
    ObservableList<Import> implist = FXCollections.observableArrayList();
    ObservableList<String> prlist = FXCollections.observableArrayList();  ObservableList<String> statuslist = FXCollections.observableArrayList();
    ObservableList<String> warelist = FXCollections.observableArrayList();
    Integer qtytotal = 0; 
    private EmployeeDAO employeeDAO; private WarehouseDAO warehouseDAO; private ProductDAO productDAO; private ImportDAO importDAO;
    
    
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); employeeDAO = new EmployeeDAO(); warehouseDAO = new WarehouseDAO(); productDAO = new ProductDAO(); importDAO = new ImportDAO();
        initcol();
        fillstatus();
        fillwarename( employeeDAO.getBranchID(MainviewController.getBranch()));
    }    


    @FXML
    private void add(ActionEvent event) {    
        if( validateA()  ){
            implist.add(new Import(wname.getValue(), pname.getValue(),status.getValue(), Integer.parseInt(noofcart.getText())));
            qtytotal += Integer.parseInt(noofcart.getText());
            totalqty.setText(String.valueOf(qtytotal));
            emptydata();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid!");
            alert.showAndWait();
        }
    }


    @FXML
    private void save(ActionEvent event) {
        if (!implist.isEmpty() && date.getValue() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if(  importDAO.CreateImport(implist, employeeDAO.getBranchID(MainviewController.getBranch()))){              
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);                
                    alert1.setContentText("Save Complete!");
                    alert1.showAndWait();
                    empty();
                } else{
                    Alert nosave = new Alert(Alert.AlertType.ERROR);
                    nosave.setContentText("Error occured! please try again");
                    nosave.showAndWait();
                }
            }         
        }else{
            Alert nosave = new Alert(Alert.AlertType.WARNING);
            nosave.setContentText("Nothing to save");
            nosave.showAndWait();
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        if (!implist.isEmpty()) {
            int i = index.get(); Integer qtysum = 0; Integer pricesum = 0;
            if(i > -1){
                implist.remove(i);
                importtable.getSelectionModel().clearSelection();
                for (int j = 0; j < implist.size(); j++) {
                    qtysum += implist.get(j).getTotalqtyofgoods();
                    qtytotal = qtysum;
                }
                totalqty.setText(String.valueOf(qtytotal));
            }
        }
    }
    
    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }
    
    @FXML
    private void getwarehouse(ActionEvent event) {
        if (wname.getValue() != null) {
            fillproduct(wname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }
    
    @FXML
    private void Help(MouseEvent event) {
        if (checkA()) {
            help_area.setText(productDAO.displayproductinfo(warehouseDAO.getWarehouseId(wname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), productDAO.getProductId(pname.getValue(), warehouseDAO.getWarehouseId(wname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))), employeeDAO.getBranchID(MainviewController.getBranch())));
        }
    }

    
    private Boolean validateA(){
        return pname.getValue() != null && wname.getValue() != null && noofcart.getText().matches("\\d+") && status.getValue() != null;
    }
    
    private void setupHelp(){
        wname.setTooltip(new Tooltip("Select a Warehouse"));
        pname.setTooltip(new Tooltip("Select a Stock"));
        noofcart.setTooltip(new Tooltip("Enter the quantity"));
        _add.setTooltip(new Tooltip("Add Item to the List"));
        date.setTooltip(new Tooltip("Date"));
        _clear.setTooltip(new Tooltip("Delete a Selected Item"));
        _save.setTooltip(new Tooltip("Save Import"));
    }

    private void initcol() {
        index.set(-1);
        wnamecol.setCellValueFactory(new PropertyValueFactory("warehouse"));
        pnamecol.setCellValueFactory(new PropertyValueFactory("productname"));
        noofcartcol.setCellValueFactory(new PropertyValueFactory("totalqtyofgoods"));
        importtable.getSelectionModel().selectedItemProperty().addListener((observable,  oldvalue, newvalue) -> {
            index.set(implist.indexOf(newvalue));
            System.out.println("OK, the index is " + implist.indexOf(newvalue));
        });
        importtable.setItems(implist);
        
        
    }

    private void fillproduct( String warehousename, Integer brID) {
        prlist = productDAO.getProduct(wname.getValue(), brID);
        pname.setItems(prlist);
    }
    
    private void fillwarename(Integer brID) {
       warelist = warehouseDAO.getWarename( brID);
       wname.setItems(warelist);
    }
    
    private void fillstatus(){
        statuslist.add("new Import");
        statuslist.add("Good sent back to Warehouse");
        status.setItems(statuslist);
    }

    private void emptydata() {
      noofcart.clear();
      pname.getSelectionModel().clearSelection();
      wname.getSelectionModel().clearSelection();
      status.getSelectionModel().clearSelection();
    }

    private void empty() {
        totalqty.setText("");
        date.setValue(null);
        importtable.getItems().clear();
        implist.clear();
        qtytotal = 0;
    }

    private Boolean checkA(){
        return pname.getValue() != null && wname.getValue() != null;
    }

    @FXML
    private void getstatus(ActionEvent event) {
    }
    

    
    
}
