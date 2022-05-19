
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dee.DAO.EmployeeDAO;
import dee.DAO.PriceDAO;
import dee.DAO.ProductDAO;
import dee.models.Customer;
import dee.models.Invoice;
import dee.models.Price;
import dee.models.Stock;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


public class MiniInvoiceController implements Initializable {

    @FXML
    private AnchorPane invoicepane;
    @FXML
    private JFXComboBox<String> pname;
    @FXML
    private JFXTextField qty;
    @FXML
    private JFXTextField unitprice;
    @FXML
    private Label total;
    @FXML
    private TableView<Invoice> invoicetable;
    @FXML
    private TableColumn<Invoice, String> productcol;
    @FXML
    private TableColumn<Invoice, Integer> qtycol;
    @FXML
    private TableColumn<Invoice, Integer> unitpricecol;
    @FXML
    private TableColumn<Invoice, Integer> totalcol;
    @FXML
    private Label totalprice;
    @FXML
    private JFXButton close;
    
    Integer pricetotal = 0; Integer Count = 0;
    private SimpleIntegerProperty index = new SimpleIntegerProperty(); // private Integer QTY = 0; private Integer UPRICE = 0; private Integer TOTAL = 0;
    IntegerProperty proQTY = new SimpleIntegerProperty(); IntegerProperty proUPRICE = new SimpleIntegerProperty();
    ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
    private PriceDAO priceDAO;
    private ProductDAO productDAO;
    private EmployeeDAO employeeDAO;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        priceDAO = new PriceDAO();  productDAO = new ProductDAO(); employeeDAO = new EmployeeDAO();  fillstock();  initcol();
    }    

    @FXML
    private void getstockprice(ActionEvent event) {
        if (pname.getValue() == null) {
            
        }else{
            unitprice.setText(String.valueOf(getstockprice(pname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))));
        }
        
    }

    @FXML
    private void add(ActionEvent event) {
        add();
    }

    @FXML
    private void clear(ActionEvent event) {
        if (invoicelist.isEmpty()) {
            
        }else{
            int i = index.get(); Integer pricesum = 0;
            if(i > -1){
                invoicelist.remove(i);
                Count -= 1;
                invoicetable.getSelectionModel().clearSelection();
                for (int j = 0; j < invoicelist.size(); j++) {
                    pricesum += invoicelist.get(j).getTotalprice();
                }
                totalprice.setText(String.valueOf(pricesum));
            }
        }
        
        
    }
    
    private Integer getstockprice(String stockname, Integer brID){
        Integer stPrice = 0;
        if (stockname == null) {
            
        }else{
            stPrice = priceDAO.getStockPrice(productDAO.getStockID(stockname, brID));
        }
        return stPrice;
    }
    
    private Boolean checkStockqty(String stocname, Integer qtyneeded){
        Boolean status = true;
        if (stocname == null && qtyneeded == null) {
            
        }else{
           
        }
       
        
        return status;
    }
    
    private void setTotalAmount(){
        NumberBinding proTOTAL = proQTY.multiply(proUPRICE);
        qty.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    Integer val = new Integer((String) newValue);
                    proQTY.setValue(val);
                } catch (Exception e) {
                }
                System.out.println("the value changed from "+ oldValue + "to " + newValue);
            }
        });
        unitprice.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    Integer val = new Integer((String) newValue);
                    proUPRICE.setValue(val);
                } catch (Exception e) { 
                }
                System.out.println("the value changed from "+ oldValue + "to " + newValue);
            }
        });
        proTOTAL.addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                total.setText(String.valueOf(newValue));
            }
        
        });
    }
    
    public void calculateTotal(){
        try {
            Integer QTY = Integer.parseInt(qty.getText());
            Integer UNITPRICE = Integer.parseInt(unitprice.getText());
            String TOTAL = "" + QTY * UNITPRICE;
            total.setText(TOTAL);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Check for correction");
            alert.showAndWait();
        }
    }
    
    private void initcol() {
        index.set(-1);
        productcol.setCellValueFactory(new PropertyValueFactory("productname"));
        qtycol.setCellValueFactory(new PropertyValueFactory("quantity"));
        unitpricecol.setCellValueFactory(new PropertyValueFactory("unitprice"));
        totalcol.setCellValueFactory(new PropertyValueFactory("totalprice")); 
        invoicetable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(invoicelist.indexOf(newvalue));
            System.out.println("OK, the index is " + invoicelist.indexOf(newvalue));
        });
        invoicetable.setItems(invoicelist);
        setTotalAmount();
    }

    private void fillstock() {
      
    }
    
    private void add(){
        try{
            String NAME = pname.getValue();
            if(NAME.isEmpty() || qty.getText().isEmpty() ||unitprice.getText().isEmpty() || total.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Incomplete Data");
                alert.showAndWait();
            }else{
                Count++;
                Integer QTY = Integer.parseInt(qty.getText());
                Integer UNITPRICE = Integer.parseInt(unitprice.getText());
                Integer TOTAL = Integer.parseInt(total.getText());
                if (checkStockqty(NAME, QTY)) {
                    invoicelist.add(new Invoice(Count, NAME, QTY, UNITPRICE, TOTAL));
                    totalprice.setText(String.valueOf(pricetotal += TOTAL));
                    emptydata();
                }else{
                    Alert nosave = new Alert(Alert.AlertType.ERROR);
                    nosave.setContentText("The Stock requested is more than what is in Store. \n Please Restock!");
                    nosave.showAndWait();
                }
            }
            }catch(Exception ex){
                Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                nosave.setTitle(null);
                nosave.setContentText("Check information because " + ex.getMessage());
                nosave.showAndWait();
            }
    }

    public void emptydata(){
      qty.clear();
      unitprice.clear();
      total.setText("");
      pname.getSelectionModel().clearSelection();

    }

    @FXML
    private void OK(ActionEvent event) {
        
    }
 
    
   

  
    
    
    
}
