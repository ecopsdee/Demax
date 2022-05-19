    
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.CustomerDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.GoodReturnDAO;
import dee.DAO.InvoiceDAO;
import dee.DAO.ProductDAO;
import dee.models.Invoice;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class GoodsReviewController implements Initializable {

    @FXML
    private VBox invoicepane;
    @FXML
    private ComboBox<Integer> invoice;
    @FXML
    private JFXTextField custID;
    @FXML
    private JFXTextField custname;
    @FXML
    private TableView<Invoice> Returntable;
    @FXML
    private TableColumn<Invoice, Integer> idcol;
    @FXML
    private TableColumn<Invoice, String> productcol;
    @FXML
    private TableColumn<Integer, Integer> qtycol;
    @FXML
    private Label totalqty;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXButton _Print;
    @FXML
    private JFXButton _Save;
    @FXML
    private JFXButton _Delete;

    private SimpleIntegerProperty index = new SimpleIntegerProperty();
    ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
    GoodReturnDAO goodReturnDAO; EmployeeDAO employeeDAO; CustomerDAO customerDAO; InvoiceDAO invoiceDAO; ProductDAO productDAO;
    
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       setupHelp(); goodReturnDAO = new GoodReturnDAO(); employeeDAO = new EmployeeDAO(); customerDAO = new CustomerDAO(); invoiceDAO = new InvoiceDAO(); productDAO = new ProductDAO();  initcol();
       fillinvoice(employeeDAO.getBranchID(MainviewController.getBranch()));
    }    

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void getdetails(ActionEvent event) {
        if (invoice.getValue() != null) {
            custID.setText(goodReturnDAO.getCustID(invoice.getValue()).toString());
            custname.setText(customerDAO.getcustomername(goodReturnDAO.getCustID(invoice.getValue()), employeeDAO.getBranchID(MainviewController.getBranch())));
            invoicelist = goodReturnDAO.getReturnList(invoice.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
            Returntable.setItems(invoicelist);
        }
        
    }

    @FXML
    private void Print(ActionEvent event) {
    }

    @FXML
    private void Delete(ActionEvent event) {
        if (validateA()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this information?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (delete(invoicelist, invoice.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()), Integer.parseInt(custID.getText())) ) {
                    emptyalldata(); 
                    Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                    nosave.setContentText("data deleted!");
                    nosave.showAndWait();
                }
            }
        } 
    }
    
    @FXML
    private void Save(ActionEvent event) {
        Alert _send = new Alert(Alert.AlertType.CONFIRMATION);
        _send.setContentText("Do you want to send to the server?");
        Optional<ButtonType> result = _send.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (validateA()) {
                if (checkQty(invoicelist, employeeDAO.getBranchID(MainviewController.getBranch()), invoice.getValue(), Integer.parseInt(custID.getText()))) {
                    if (save(invoicelist, employeeDAO.getBranchID(MainviewController.getBranch()), invoice.getValue(), Integer.parseInt(custID.getText()) )) {
                        Alert _ok = new Alert(Alert.AlertType.INFORMATION);
                        _ok.setContentText("sent to server!");
                        _ok.showAndWait();
                        emptyalldata();
                    }
                }else{
                    Alert _no = new Alert(Alert.AlertType.ERROR);
                    _no.setContentText("Error!");
                    _no.showAndWait();
                }

            }
        }
    }
    
    private void setupHelp(){
        _Print.setTooltip(new Tooltip("Print"));
        _Save.setTooltip(new Tooltip("Save a Selected Item"));
        _Delete.setTooltip(new Tooltip("Delete a Selected Item"));
        invoice.setTooltip(new Tooltip("Select an Invoice"));
    }

    private void initcol() {
        idcol.setCellValueFactory(new PropertyValueFactory("id"));
        productcol.setCellValueFactory(new PropertyValueFactory("productname"));
        qtycol.setCellValueFactory(new PropertyValueFactory("quantity"));
        Returntable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(invoicelist.indexOf(newvalue));
            System.out.println("OK, the index is " + invoicelist.indexOf(newvalue));
        });
        Returntable.setItems(invoicelist);
    }

    private void fillinvoice(Integer brID) {
        invoice.setItems(goodReturnDAO.getInvoice(brID));
    }

    private void emptyalldata() {
        invoice.getSelectionModel().clearSelection();
        custID.clear();
        invoicelist.clear();
        fillinvoice(employeeDAO.getBranchID(MainviewController.getBranch()));
    }

    private Boolean checkQty(ObservableList<Invoice> _itemlist, Integer brID, Integer invoice, Integer custID){
        Boolean status = true;
        for (Invoice _item : _itemlist) {
            if (_item.getQuantity() <= invoiceDAO.RemainQty(brID, invoice, productDAO.getStockID(_item.getProductname(), brID), custID)) {
            }else{
                status = false;
                break;
            }
        }      
        return status;
    }
    
    private Boolean save(ObservableList<Invoice> _item, Integer brID, Integer invoice, Integer custID){
        Boolean status = false;
        for (Invoice item : _item ) {
            if (updateinvoice(brID, invoice ,item.getQuantity() , productDAO.getStockID(item.getProductname(), brID), custID)  && goodReturnDAO.UpdateStockAccount(_item, invoice, brID) && goodReturnDAO.savereturn(_item, invoice, custID, brID) && goodReturnDAO.DeletePermsentreturn(invoice, brID)  ) {
                status = true;   
            }
        }
        return status;
    }
    
    private Boolean updateinvoice(Integer brID, Integer invoice, Integer qty, Integer stID, Integer cusID){
        return invoiceDAO.UpdateInvoice(totremain(totsum(qty, invoiceDAO.ReturnQty(brID, invoice, stID, cusID)), invoiceDAO.RemainQty(brID, invoice, stID, cusID)), totsum(qty, invoiceDAO.ReturnQty(brID, invoice, stID, cusID)), invoice, total(invoiceDAO.Unitprice(brID, invoice, stID, cusID), totremain(totsum(qty, invoiceDAO.ReturnQty(brID, invoice, stID, cusID)), invoiceDAO.RemainQty(brID, invoice, stID, cusID))));
    }
    
    private Boolean delete(ObservableList<Invoice> _item, Integer invoice, Integer brID, Integer custID){
        return goodReturnDAO.Deletesentreturn(_item, invoice, custID, brID) && goodReturnDAO.DeletePermsentreturn(invoice, brID) ;
    }
            
    private Boolean validateA(){
        return !invoicelist.isEmpty() && invoice.getValue() != null && custID.getText().matches("\\d+") && !custname.getText().isEmpty();
    }
   
    private Integer totsum(Integer totalA, Integer totalB){
        return totalA + totalB;
    }
    
    private Integer totremain(Integer total, Integer remain){
        return remain - total;
    }
    
    private Integer total(Integer unitprice, Integer remain ){
        return unitprice * remain;
    }
   
    
}
