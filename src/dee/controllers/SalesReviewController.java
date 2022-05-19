
package dee.controllers;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.CustomerDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.InvoiceDAO;
import dee.DAO.ProductDAO;
import dee.DAO.WarehouseOrderDAO;
import dee.models.Customer;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class SalesReviewController implements Initializable {

    @FXML
    private ComboBox<Integer> invoice;
    @FXML
    private Label custID;
    @FXML
    private Label name;
    @FXML
    private Label location;
    @FXML
    private TableView<Invoice> Returntable;
    @FXML
    private TableColumn<Invoice, String> productcol;
    @FXML
    private TableColumn<Invoice, Integer> qtycol;
    @FXML
    private TableColumn<Invoice, Integer> unitpricecol;
    @FXML
    private TableColumn<Invoice, Integer> tootalcol;
    @FXML
    private Label totalqty;
    @FXML
    private Label date;
    @FXML
    private VBox invoicepane;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private Label balance;

    Integer pricetotal = 0; Integer Count = 0;
    private SimpleIntegerProperty index = new SimpleIntegerProperty(); 
    ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
    ObservableList<Integer> invoicenumbers = FXCollections.observableArrayList();
    ObservableList<Customer> customerlist = FXCollections.observableArrayList(); 
    private InvoiceDAO invoiceDAO = new InvoiceDAO(); private CustomerDAO customerDAO = new CustomerDAO(); private EmployeeDAO employeeDAO; private WarehouseOrderDAO productDAO; private ProductDAO proDAo;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        invoiceDAO = new InvoiceDAO(); customerDAO = new CustomerDAO(); employeeDAO =  new EmployeeDAO(); productDAO = new WarehouseOrderDAO(); proDAo = new ProductDAO();
        initcol();
        displayInvoice("Customer", employeeDAO.getBranchID(MainviewController.getBranch()));
    }    

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void getdetails(ActionEvent event) {
        if (invoice.getValue() != null) {
            getdetails(invoice.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }

    @FXML
    private void Save(ActionEvent event) {    
        if (!invoicelist.isEmpty() && invoice.getValue() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Data");
            alert.setContentText("Are you sure you want to save this information?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (save(invoice.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()), "Customer", Integer.parseInt(custID.getText()), name.getText(), location.getText(), Integer.parseInt(totalqty.getText()), Integer.parseInt(balance.getText()), Integer.parseInt(totalqty.getText()), "Customer" )) {
                    Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                    nosave.setContentText("data Saved!");
                    nosave.showAndWait();
                    emptyalldata(); clearList(); Returntable.getItems().clear();
                    displayInvoice("Customer", employeeDAO.getBranchID(MainviewController.getBranch()));
                }else{
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setContentText("Error occurred! please try again");
                    err.showAndWait();
                }
            }
        }   
    }

    @FXML
    private void refresh(ActionEvent event) {
        refresh();
    }

    @FXML
    private void clear(ActionEvent event) {
    }
    
    private void initcol() {
        index.set(-1);
        productcol.setCellValueFactory(new PropertyValueFactory("productname"));
        qtycol.setCellValueFactory(new PropertyValueFactory("quantity"));
        unitpricecol.setCellValueFactory(new PropertyValueFactory("unitprice"));
        tootalcol.setCellValueFactory(new PropertyValueFactory("totalprice")); 
        Returntable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(invoicelist.indexOf(newvalue));
            System.out.println("OK, the index is " + invoicelist.indexOf(newvalue));
        });
        Returntable.setItems(invoicelist);
    }

    private void displayInvoice(String customer, Integer brID) {
       invoicenumbers.clear();
       invoicenumbers = invoiceDAO.getInvoiceTempList(customer, brID);
       invoice.setItems(invoicenumbers);
    }
    
    private void getdetails(Integer invoicenum, Integer brID){
        invoicelist = invoiceDAO.displayInvoiceTempList(invoicenum, brID);
        Invoice getbaldetail = invoiceDAO.getInvoiceTemplist(invoicenum, brID);
        
        custID.setText(String.valueOf(getbaldetail.getQuantity()));
        date.setText(getbaldetail.getProductname());
        totalqty.setText(String.valueOf(getbaldetail.getId()));
        balance.setText(String.valueOf(getbaldetail.getUnitprice()));
     
        Customer getCustomer = customerDAO.getcustomerdetail(Integer.parseInt(custID.getText()), brID);
        name.setText(getCustomer.getFname());
        location.setText(getCustomer.getOrigin());
        
        Returntable.setItems(invoicelist);
    }
    
    private Boolean save(Integer invoicenum, Integer brID, String detail, Integer custid, String name, String locate, Integer totprice, Integer balance, Integer totFee, String _status){
       Boolean status = false;
        if (customerDAO.checkCustomer(custid, brID, _status, name)) {
            if ( customerDAO.CreateCustomer(new Customer(custid, name, locate), brID, detail) && customerDAO.deleteCustomerTempList(custid, brID, invoicenum) &&  invoiceDAO.saveInvoiceNuumber(invoicenum, detail, custid, totprice, balance, brID) &&  invoiceDAO.DeletePermSentInvoiceNumber(invoicenum, brID) &&  invoiceDAO.SaveInvoice(invoicelist, invoicenum, status, custid,  brID) && invoiceDAO.DeletePermSentInvoice(invoicenum, brID) && productDAO.deductStockAccount(invoicelist, invoicenum, brID) &&  invoiceDAO.DeletePermSentInvoice(invoicenum, brID) &&    proDAo.createAnalysis(brID, 0, "Head Office Sales", totFee)) {
                status = true;
            }  
        }else{
            if (customerDAO.CreateCustomer(new Customer(custid, name, locate), brID, detail) &&  customerDAO.deleteCustomerTempList(custid, brID, invoicenum) &&  invoiceDAO.saveInvoiceNuumber(invoicenum, detail, custid, totprice, balance, brID) &&  invoiceDAO.DeletePermSentInvoiceNumber(invoicenum, brID) &&  invoiceDAO.SaveInvoice(invoicelist, invoicenum, status, custid,  brID) && invoiceDAO.DeletePermSentInvoice(invoicenum, brID) && productDAO.deductStockAccount(invoicelist, invoicenum, brID) &&  invoiceDAO.DeletePermSentInvoice(invoicenum, brID)  ) {
                if (proDAo.createAnalysis(brID, 0, "Head Office Sales", totFee)) {
                    status = true;
                }
            }
        }
        
       return status;       
    }
    
    private void emptyalldata(){
      name.setText("");
      location.setText("");
      date.setText("");
      custID.setText("");
      totalqty.setText("");
    }
    
    private void refresh(){
        if (!invoicelist.isEmpty() && invoice.getValue() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Data");
            alert.setContentText("Are you sure you want to delete this information?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (delete(invoice.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()), "Customer", Integer.parseInt(custID.getText()), name.getText(), location.getText(), Integer.parseInt(totalqty.getText()), Integer.parseInt(balance.getText()))) {
                    emptyalldata(); clearList(); Returntable.getItems().clear();
                    Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                    nosave.setContentText("data deleted!");
                    nosave.showAndWait();
                    displayInvoice("Customer", employeeDAO.getBranchID(MainviewController.getBranch()));
                }
            }
        } 
        
    }

    public void clearList(){
        invoicelist.clear();
        customerlist.clear();
    }
   
    public Boolean delete(Integer invoicenum, Integer brID, String detail, Integer custid, String name, String locate, Integer totprice, Integer balance){
        Boolean status = false;
        if (customerDAO.DeleteCustomerReview(new Customer(custid, name, locate), brID, detail, invoicenum)) {
            if (customerDAO.deleteCustomerTempList(custid, brID, invoicenum)) {
                if (invoiceDAO.DeletesentInvoiceNuumber(invoicenum, detail, custid, totprice, balance, brID)) {
                    if (invoiceDAO.DeletePermSentInvoiceNumber(invoicenum, brID)) {
                        if (invoiceDAO.DeleteSentInvoice(invoicelist, invoicenum, status, custid, brID)) {
                            if (invoiceDAO.DeletePermSentInvoice(invoicenum, brID)) {
                                status = true;
                            }
                        }
                    }
                }
            }
        }
        return status;
    }
    
}
