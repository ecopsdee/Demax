
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.PriceDAO;
import dee.DAO.ProductDAO;
import dee.DAO.WarehouseDAO;
import dee.models.Price;
import dee.models.Product;
import dee.reports.run.Report;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;


public class ProductsController implements Initializable {

    @FXML
    private JFXTextField pname;
    @FXML
    private ComboBox<String> wname;
    @FXML
    private TableView<Product> product;
    @FXML
    private TableColumn<Product, Integer> idcol;
    @FXML
    private TableColumn<Product, String> pnamecol;
    @FXML
    private TableColumn<Product, String> wnamecol;
    @FXML
    private TableColumn<Product, String> accountcol;
    @FXML
    private TableColumn<Product, String> describecol;
    @FXML
    private TableColumn<Product, String> brandcol;
    @FXML
    private TableColumn<Product, String> otherinfocol;
    @FXML
    private TableColumn<Product, Integer> packcol;
    @FXML
    private TableColumn<Product, Integer> nopackcol;
    @FXML
    private TableColumn<Product, Integer> nogpaackcol;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXTextField describe;
    @FXML
    private JFXTextField brand;
    @FXML
    private JFXTextField otherinfo;
    @FXML
    private RadioButton box;
    @FXML
    private ToggleGroup pickone;
    @FXML
    private RadioButton bag;
    @FXML
    private JFXTextField nop;
    @FXML
    private JFXTextField nogpp;
    @FXML
    private JFXTextField noop;
    @FXML
    private VBox boxgroup;
    @FXML
    private TilePane baggroup;
    @FXML
    private JFXButton _add;
    @FXML
    private JFXButton _delete;
    @FXML
    private JFXButton _save;
    


    ObservableList<Product> prlist = FXCollections.observableArrayList();  ObservableList<Product> stlist = FXCollections.observableArrayList();
    ObservableList<String> warelist = FXCollections.observableArrayList(); ObservableList<Price> pricelist = FXCollections.observableArrayList();
    private final IntegerProperty index = new SimpleIntegerProperty();
    Integer Count = 0;
    private EmployeeDAO employeeDAO; private WarehouseDAO warehouseDAO; private ProductDAO productDAO; private PriceDAO priceDAO;
 

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); employeeDAO = new EmployeeDAO();  warehouseDAO = new WarehouseDAO(); productDAO = new ProductDAO(); priceDAO = new PriceDAO();
        
       toggle();
       initcol();
       fillwarename();
    }    


    @FXML
    private void add(ActionEvent event) {
        Integer gen_ID = 0; Integer st_ID = 0;
        do {            
            gen_ID = test_ID(generateID(), prlist);  
        } while (productDAO.checkProductID(gen_ID, employeeDAO.getBranchID(MainviewController.getBranch())));
        
        do {            
            st_ID = test_ID(generateID(), stlist);
        } while (productDAO.checkStockID(st_ID, employeeDAO.getBranchID(MainviewController.getBranch())));

        if (box.isSelected()) {
            if (!describe.getText().isEmpty() && !brand.getText().isEmpty() && !pname.getText().isEmpty() && nop.getText().matches("\\d+") && nogpp.getText().matches("\\d+") && wname.getValue() != null) {
                Count++;
                prlist.add(new Product(Count, gen_ID, warehouseDAO.getWarehouseId(wname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), captitalizeWord(wname.getValue()), captitalizeWord(describe.getText()), captitalizeWord(brand.getText()), display_model(brand.getText(), describe.getText(), pname.getText()), otherinfo.getText(), generateAccount(), "Box", Integer.parseInt(nop.getText()), Integer.parseInt(nogpp.getText())));
                stlist.add(new Product(Count, st_ID, 0, "", "", "", display_model(brand.getText(), describe.getText(), pname.getText()), "", generateStockAccount(), "", 0, 0));
                emptydata();
            }
        }else if (bag.isSelected()) {
            if (!describe.getText().isEmpty() && !brand.getText().isEmpty() && !pname.getText().isEmpty() && noop.getText().matches("\\d+") && wname.getValue() != null) {
                Count++;
                prlist.add(new Product(Count, gen_ID, warehouseDAO.getWarehouseId(wname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), captitalizeWord(wname.getValue()), captitalizeWord(describe.getText()), captitalizeWord(brand.getText()), display_model(brand.getText(), describe.getText(), pname.getText()), otherinfo.getText(), generateAccount(), "Bag", Integer.parseInt(noop.getText()), 0));
                stlist.add(new Product(Count, st_ID, 0, "", "", "", display_model(brand.getText(), describe.getText(), pname.getText()), "", generateStockAccount(), "", 0, 0));
                emptydata();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error!");
            alert.showAndWait();
        }      
    }

    @FXML
    private void save(ActionEvent event) {
        if (!prlist.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (productDAO.createProduct(prlist, employeeDAO.getBranchID(MainviewController.getBranch())) && productDAO.createStock(stlist, employeeDAO.getBranchID(MainviewController.getBranch())) && priceDAO.CreateStockPrice(stlist, 1, 0,0, employeeDAO.getBranchID(MainviewController.getBranch()))    ) {
                    Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                    nosave.setContentText("Save Complete");
                    nosave.showAndWait();
                    clearA();
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
        if (prlist.isEmpty() && stlist.isEmpty()) {
            
        }else{
            int i = index.get();
            if(i > -1){
                prlist.remove(i);
                stlist.remove(i);
                product.getSelectionModel().clearSelection();   
                Count -= 1;
            }
        }
    }
    
    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }
    
    @FXML
    private void show(ActionEvent event) {
    }
    
    private void setupHelp(){
        describe.setTooltip(new Tooltip("Enter the Stock Description"));
        brand.setTooltip(new Tooltip("Enter the Stock Brand"));
        pname.setTooltip(new Tooltip("Enter the Stock "));
        otherinfo.setTooltip(new Tooltip("Enter other Information"));
        wname.setTooltip(new Tooltip("Select Warehouse"));
        _add.setTooltip(new Tooltip("Add Item to List"));
        _delete.setTooltip(new Tooltip("Delete selected Item from the List"));
        _save.setTooltip(new Tooltip("Save"));
    }

    private void initcol() {
       index.set(-1);
       idcol.setCellValueFactory(new PropertyValueFactory("id"));
       pnamecol.setCellValueFactory(new PropertyValueFactory("prname"));
       wnamecol.setCellValueFactory(new PropertyValueFactory("warename"));
       accountcol.setCellValueFactory(new PropertyValueFactory("account"));
       describecol.setCellValueFactory(new PropertyValueFactory("whname"));
       brandcol.setCellValueFactory(new PropertyValueFactory("prtable"));
       otherinfocol.setCellValueFactory(new PropertyValueFactory("date"));
       packcol.setCellValueFactory(new PropertyValueFactory("type"));
       nopackcol.setCellValueFactory(new PropertyValueFactory("numberofPacket"));
       nogpaackcol.setCellValueFactory(new PropertyValueFactory("numberOfGoodsPerPacket"));
       product.setItems(prlist);
       product.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(prlist.indexOf(newvalue));
            System.out.println("OK, the index is " + prlist.indexOf(newvalue));
        });
    }

    private void emptydata() {
        describe.clear();
        brand.clear();
        pname.clear();
        otherinfo.clear();
        nop.clear();
        nogpp.clear();
        noop.clear();
        wname.getSelectionModel().clearSelection();
    }
    
    private void clearA(){
        prlist.clear();
        stlist.clear();
        product.getItems().clear();
        Count = 0;
    }
    
    private void toggle() {
        pickone.getSelectedToggle().selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (box.isSelected()) {
                nop.setEditable(true);
                boxgroup.setOpacity(1);
                nogpp.setEditable(true);
                baggroup.setOpacity(0.33);
                noop.setEditable(false);
            }else if (bag.isSelected()) {
                baggroup.setOpacity(1);
                noop.setEditable(true);
                nop.setEditable(false);
                boxgroup.setOpacity(0.33);
                nogpp.setEditable(false);
            }
        });
    }

    private void fillwarename() {
       warelist = warehouseDAO.getWarename(employeeDAO.getBranchID(MainviewController.getBranch()));
       wname.setItems(warelist);
    }
    
    public String generateAccount(){
        String value = "308";
        for (int i = 0; i < 7; i++) {
            int number = 1 + (int)(Math.random() * 10);
            value += String.valueOf(number); 
        }
        return  value + "_Account";
    }
    
    public String generateStockAccount(){
        String value = "308";
        for (int i = 0; i < 7; i++) {
            int number = 1 + (int)(Math.random() * 10);
            value += String.valueOf(number); 
        }
        return  value + "_Stock";
    }
    
    private String captitalizeWord(String word){
        StringBuffer s = new StringBuffer();
        char ch = ' ';
        for (int i = 0; i < word.length(); i++) {
            if (ch == ' ' && word.charAt(i) != ' ') {
                s.append(Character.toUpperCase(word.charAt(i)));
            }else{
                s.append(word.charAt(i));
            }
            ch = word.charAt(i);
        }
        
        return s.toString().trim();
    }
    
    private String display_model(String _brand, String _describe, String _model ){
        return _brand + " " + _describe + " " + _model;
    }
    
    private Integer generateID(){
        int number = 1 + (int)(Math.random() * 1000);
        return  number;
    }
    
    private Integer test_ID(Integer _item, ObservableList<Product> _list){
        for (int i = 0; i < _list.size(); i++) {
            while (test_digit(_item, _list.get(i).getPrid())) {
                System.out.println("the ID " + _item + " already exist");
                _item = generateID();
            }  
        }
        System.out.println("ID is " + _item);
        return _item;
    }

    private Boolean test_digit(Integer a, Integer b){
        return Objects.equals(a, b);
    }
    

    

    
   
    
}
