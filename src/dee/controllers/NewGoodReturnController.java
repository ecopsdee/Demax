
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.CustomerDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.GoodReturnDAO;
import dee.DAO.InvoiceDAO;
import dee.DAO.ProductDAO;
import dee.models.Invoice;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class NewGoodReturnController implements Initializable {

    @FXML
    private VBox invoicepane;
    @FXML
    private HBox custidbody;
    @FXML
    private Label custID;
    @FXML
    private JFXComboBox<String> pname;
    @FXML
    private JFXTextField qty;
    @FXML
    private TableView<Invoice> returntable;
    @FXML
    private TableColumn<Invoice, String> goodcol;
    @FXML
    private TableColumn<Invoice, Integer> QTYcol;
    @FXML
    private Label totalprice;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXComboBox<Integer> selectinvoice;
    @FXML
    private HBox custidbody1;
    @FXML
    private HBox custidbody11;
    @FXML
    private Label ownership;
    @FXML
    private Label custname;
    @FXML
    private MaterialDesignIconView close;

    private SimpleIntegerProperty index = new SimpleIntegerProperty(); ObservableList<Integer> invoiceNUM = FXCollections.observableArrayList();
    ObservableList<Invoice> allinvoice = FXCollections.observableArrayList(); ObservableList<Invoice> newinvoice = FXCollections.observableArrayList();
    GoodReturnDAO goodReturnDAO;  EmployeeDAO employeeDAO; ProductDAO productDAO; InvoiceDAO invoiceDAO; CustomerDAO customerDAO;
    Integer pricetotal = 0; Boolean status = false;
    @FXML
    private JFXButton _add;
    @FXML
    private JFXButton _cleargoodreturn;
    @FXML
    private JFXButton _Send;
    @FXML
    private JFXButton _refresh;
   
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); goodReturnDAO = new GoodReturnDAO(); employeeDAO = new EmployeeDAO(); productDAO = new ProductDAO(); invoiceDAO = new InvoiceDAO(); customerDAO = new CustomerDAO();
        initcol();
    }    

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void add(ActionEvent event) {
        add();
    }

    @FXML
    private void Send(ActionEvent event) {
        Alert _send = new Alert(Alert.AlertType.CONFIRMATION);
        _send.setContentText("Do you want to send to the server?");
        Optional<ButtonType> result = _send.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (validate() && !allinvoice.isEmpty()) {
                if (Send(selectinvoice.getValue(), Integer.parseInt(custID.getText()), employeeDAO.getBranchID(MainviewController.getBranch()))) {
                    Alert ok = new Alert(Alert.AlertType.CONFIRMATION);
                    ok.setContentText("sent to the server!");
                    ok.showAndWait();
                    clearalldata();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("error has occurred! \nplease check and retry");
                alert.showAndWait();
            } 
        }    
    }

    @FXML
    private void refresh(ActionEvent event) {
    }

    @FXML
    private void getinvoice(ActionEvent event) {
        if (date.getValue() != null) {
            getInvoiceList(date.getValue().toString(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }

    @FXML
    private void getallinvoicedetails(ActionEvent event) {
        if (selectinvoice != null) {
            getallinvoicedetails(selectinvoice.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
            fillstock(selectinvoice.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
            if (!custID.getText().isEmpty()) {
                getCustName(Integer.parseInt(custID.getText()), employeeDAO.getBranchID(MainviewController.getBranch()));
            }
        }
    }
    
    @FXML
    private void cleargoodreturn(ActionEvent event) {
        if (!allinvoice.isEmpty()) {
            int i = index.get(); 
            if(i > -1){
                allinvoice.remove(i);
                returntable.getSelectionModel().clearSelection();   
            } 
        } 
    }
    
    private void setupHelp(){
        date.setTooltip(new Tooltip("Select Starting Date"));
        selectinvoice.setTooltip(new Tooltip("Select Invoice"));
        pname.setTooltip(new Tooltip("Select Stock"));
        date.setTooltip(new Tooltip("Enter quantity"));
        _cleargoodreturn.setTooltip(new Tooltip("Delete a selected item in the list"));
        _Send.setTooltip(new Tooltip("Send Item to Server"));
        _refresh.setTooltip(new Tooltip("Refresh"));
        
    }
    
    private void initcol() {
        goodcol.setCellValueFactory(new PropertyValueFactory("productname"));
        QTYcol.setCellValueFactory(new PropertyValueFactory("quantity"));
        returntable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(allinvoice.indexOf(newvalue));
            System.out.println("OK, the index is " + allinvoice.indexOf(newvalue));
        });
        returntable.setItems(allinvoice);
        
        
    }
    
    private void fillstock(Integer invoice, Integer brID) {
      pname.setItems(invoiceDAO.getProduct(invoice, brID));
    }

    private void emptydata() {
      qty.clear();
      pname.getSelectionModel().clearSelection();
    }

    private void emptyalldata() {
        totalprice.setText("");
        custID.setText("");
        custname.setText("");
    }
    
    private void clearalldata(){
        selectinvoice.getSelectionModel().clearSelection();
        returntable.getItems().clear(); allinvoice.clear();
    }
    
    private void getInvoiceList(String Invoicedate, Integer brID){
       invoiceNUM = invoiceDAO.InvoiceDetails(Invoicedate, brID);
       selectinvoice.setItems(invoiceNUM);
    }
    
    private void getallinvoicedetails(Integer invoicenumber, Integer brID){
        Invoice invoice = invoiceDAO.getInvoicePersona(invoicenumber, brID);
        ownership.setText(invoice.getProductname());
        custID.setText(String.valueOf(invoice.getQuantity()));
    }
    
    private void getCustName(Integer custID, Integer brID){
        custname.setText(customerDAO.getcustomername(custID, brID));
    }
    
    private void add(){
        if ( date.getValue() != null && selectinvoice.getValue() != null && pname.getValue() != null && checkQty(employeeDAO.getBranchID(MainviewController.getBranch()), selectinvoice.getValue(), productDAO.getStockID(pname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), Integer.parseInt(custID.getText()), Integer.parseInt(qty.getText())) && qty.getText().matches("\\d+")) {
            allinvoice.add(new Invoice(0,  pname.getValue(), Integer.parseInt(qty.getText())));
            emptydata();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("an error has occurred! \nSuggestions \n1. Make sure that all information is correctly inserted \n2. Make sure the amount of goods you entered is not more than what the customer bought");
            alert.showAndWait();
        }
    }

    private Boolean Send(Integer invoice, Integer customerID, Integer brID){
       return goodReturnDAO.sendreturn(allinvoice, invoice, customerID, brID);
    }
    
    private Boolean checkQty(Integer brID, Integer invoice, Integer stID, Integer custID, Integer qty){
        return qty <= invoiceDAO.RemainQty(brID, invoice, stID, custID);
    }
    
    private Boolean validate(){
        return selectinvoice.getValue() != null && !custID.getText().isEmpty() & !custname.getText().isEmpty() & !ownership.getText().isEmpty();
    }
    
    private String displaydateandtime(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }

   
    
}
