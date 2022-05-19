
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.CustomerDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.InvoiceDAO;
import dee.DAO.PriceDAO;
import dee.DAO.ProductDAO;
import dee.models.Customer;
import dee.models.Invoice;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class NewInvoiceController implements Initializable {
    
    @FXML
    private JFXToggleButton toggle;
    @FXML
    private Label GenInvoiceno;
    @FXML
    private JFXTextField invoiceno;
    @FXML
    private JFXTextField firstname;
    @FXML
    private JFXTextField origin;
    @FXML
    private Label custID;
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
    private TableColumn<Invoice, Integer> idcol;
    @FXML
    private TableColumn<Invoice, String> productcol;
    @FXML
    private TableColumn<Invoice, Integer> qtycol;
    @FXML
    private TableColumn<Invoice, Integer> unitpricecol;
    @FXML
    private TableColumn<Invoice, Integer> totalcol;
    @FXML
    private JFXTextField amountpaid;
    @FXML
    private Label balance;
    @FXML
    private Label totalprice;
    @FXML
    private HBox genInvoicebody;
    @FXML
    private HBox invoicebody;
    @FXML
    private VBox invoicepane;
    @FXML
    private TextArea receiptView;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private FontAwesomeIconView serach;
    @FXML
    private JFXButton _generateInvoice;
    @FXML
    private JFXButton _loadcustomer;
    @FXML
    private JFXButton _add;
    @FXML
    private JFXButton _calculatebalance;
    @FXML
    private JFXButton _viewreceipt;
    @FXML
    private JFXButton _refresh;
    @FXML
    private JFXButton _clear;
    @FXML
    private JFXButton _generatePartnerID;

    Integer pricetotal = 0; Integer Count = 0;
    private SimpleIntegerProperty index = new SimpleIntegerProperty();  private Integer QTY = 0; private Integer UPRICE = 0; private Integer TOTAL = 0;
    ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
    ObservableList<Customer> customerlist = FXCollections.observableArrayList(); 
    private ProductDAO productDAO; private PriceDAO priceDAO; private EmployeeDAO employeeDAO; private CustomerDAO customerDAO; private InvoiceDAO invoiceDAO;
    
    
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       setupHelp();  productDAO = new ProductDAO();  priceDAO = new PriceDAO(); employeeDAO = new EmployeeDAO(); customerDAO = new CustomerDAO(); invoiceDAO = new InvoiceDAO(); 
       selectIDs();  fillstock(employeeDAO.getBranchID(MainviewController.getBranch()));  initcol();
    }    

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }
   
    @FXML
    private void generateInvoice(ActionEvent event) {
        GenInvoiceno.setText(invoiceIDS());
    }

    @FXML
    private void generatePartnerID(ActionEvent event) {
        Integer gen_ID = 0;
        do {            
            gen_ID = Integer.parseInt(generatepartnerIDS());
        } while (customerDAO.checkCustomerID(gen_ID, employeeDAO.getBranchID(MainviewController.getBranch())));
        
        custID.setText(gen_ID.toString());
    }
  
    @FXML
    private void add(ActionEvent event) {
        if (validateC()) {
            if (checkStockqty(pname.getValue(), Integer.parseInt(qty.getText()), employeeDAO.getBranchID(MainviewController.getBranch()))) {
                if (add(pname.getValue(), Integer.parseInt(qty.getText()), Integer.parseInt(unitprice.getText()), Integer.parseInt(total.getText()), Count++ )) {
                    totalprice.setText(String.valueOf(pricetotal += TOTAL));
                    emptydata();
                }
            }else{
                Alert nosave = new Alert(Alert.AlertType.ERROR);
                nosave.setContentText("The Stock requested is more than what is in Store. \nPlease Restock!");
                nosave.showAndWait();
            }
        }    
    }
    
    @FXML
    private void calculatebalance(ActionEvent event) {
        if (amountpaid.getText().matches("\\d+") && totalprice.getText().matches("\\d+")) {
            balance.setText(String.valueOf(calculatebalance(Integer.parseInt(amountpaid.getText()), Integer.parseInt(totalprice.getText()))));
        }
    }

    @FXML
    private void refresh(ActionEvent event) {
        invoicetable.getItems().clear();
    }

    @FXML
    private void clear(ActionEvent event) {
        int i = index.get(); Integer pricesum = 0;
        if(i > -1){
            pricetotal -= invoicelist.get(i).getTotalprice();
            invoicelist.remove(i);
            Count -= 1;
            invoicetable.getSelectionModel().clearSelection();
            for (int j = 0; j < invoicelist.size(); j++) {
                pricesum += invoicelist.get(j).getTotalprice();
            }
            totalprice.setText(String.valueOf(pricesum));
            
        }
       
    }
    
    @FXML
    private void opencustomer(MouseEvent event) {
        loadcontrol("/dee/views/Customer.fxml", "Customer", serach);
    }
    
    @FXML
    private void loadcustomer(ActionEvent event) {
        autofilldata();
    }

    @FXML
    private void viewreceipt(ActionEvent event) {
        Alert _save = new Alert(Alert.AlertType.CONFIRMATION);
        _save.setContentText("View Receipt?");
        Optional<ButtonType> result = _save.showAndWait();
        if (result.get() == ButtonType.OK) {
                if (toggle.isSelected() == true) {
                    if (validateA() && !invoicelist.isEmpty()) {
                        if (check_customer_status(employeeDAO.getBranchID(MainviewController.getBranch()), Integer.parseInt(custID.getText()), "Customer")) {
                            if (check_user_ID(Integer.parseInt(custID.getText()), employeeDAO.getBranchID(MainviewController.getBranch()), "Customer", firstname.getText())) {
                                if (check_invoice(Integer.parseInt(invoiceno.getText()), employeeDAO.getBranchID(MainviewController.getBranch()) )) {
                                    if (send(employeeDAO.getBranchID(MainviewController.getBranch()), Integer.parseInt(custID.getText()), firstname.getText(), origin.getText(), "Customer", Integer.parseInt(invoiceno.getText()), Integer.parseInt(totalprice.getText()), Integer.parseInt(balance.getText()), invoicelist)   ) {
                                        generateReceipt(firstname.getText(), Integer.parseInt(custID.getText()), displaydateandtime(), origin.getText(),Integer.parseInt(invoiceno.getText()) , Integer.parseInt(totalprice.getText()), Integer.parseInt(amountpaid.getText()), Integer.parseInt(balance.getText()));
                                        Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                                        nosave.setContentText("data sent!");
                                        nosave.showAndWait();
                                        emptyalldata();
                                    }
                                }else{
                                    error_message("invalid_invoice");
                                }
                            }else{
                                error_message("invalid_customer_ID");
                            }
                        }else{
                            error_message("invalid_customer");
                        }           
                    }else{
                        error_message("incomplete_data"); 
                    }  
                }else{
                    if (validateB() && !invoicelist.isEmpty()  ) {
                        if (check_customer_status(employeeDAO.getBranchID(MainviewController.getBranch()), Integer.parseInt(custID.getText()), "Customer")) {
                            if (check_user_ID(Integer.parseInt(custID.getText()), employeeDAO.getBranchID(MainviewController.getBranch()), "Customer", firstname.getText())) {
                                if (check_invoice(Integer.parseInt(GenInvoiceno.getText()), employeeDAO.getBranchID(MainviewController.getBranch()) )) {
                                    if ((send(employeeDAO.getBranchID(MainviewController.getBranch()), Integer.parseInt(custID.getText()), firstname.getText(), origin.getText(), "Customer", Integer.parseInt(GenInvoiceno.getText()), Integer.parseInt(totalprice.getText()), Integer.parseInt(balance.getText()), invoicelist)   )) {
                                        generateReceipt(firstname.getText(), Integer.parseInt(custID.getText()), displaydateandtime(), origin.getText(),Integer.parseInt(GenInvoiceno.getText()) , Integer.parseInt(totalprice.getText()), Integer.parseInt(amountpaid.getText()), Integer.parseInt(balance.getText())); 
                                        Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                                        nosave.setContentText("data sent!");
                                        nosave.showAndWait();
                                        emptyalldata();

                                    }   
                                }else{
                                    error_message("invalid_invoice");
                                }
                            }else{
                                error_message("invalid_customer_ID");
                            }
                        }else{
                            error_message("invalid_customer");
                        }
                    }else{
                        error_message("incomplete_data"); 
                    } 
                } 
            
             
        }     
    }
    
    @FXML
    private void getstockprice(ActionEvent event) {
        if (pname.getValue() != null) {
            unitprice.setText(String.valueOf(getstockprice(pname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))));
        }
    }
 
    private void initcol() {
        receiptView.setEditable(false);
        index.set(-1);
        idcol.setCellValueFactory(new PropertyValueFactory("id"));
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

    private void fillstock(Integer brID) {
       pname.setItems(productDAO.getProduct(brID));
    }
   
    private void selectIDs() {
        toggle.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (toggle.isSelected() == true) {
                genInvoicebody.setOpacity(0);
                invoicebody.setOpacity(1);
                invoiceno.setEditable(true);
            } else {
                invoicebody.setOpacity(0);
                genInvoicebody.setOpacity(1);
                invoiceno.setEditable(false);
            }
        });
    }
    
    private void setTotalAmount(){
        qty.textProperty().addListener((observable,oldvalue,newvalue)->{
            try {
                if (newvalue.equals("") || !newvalue.matches("\\d+")) {
               
                }else{
                    QTY = Integer.parseInt(newvalue);
                    System.out.println("the value changed from "+ oldvalue + "to " + newvalue);
                    TOTAL = QTY * UPRICE;
                    total.setText(String.valueOf(TOTAL));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }  
        });
        unitprice.textProperty().addListener((observable,oldvalue,newvalue)->{
            try {
                if (newvalue.equals("") || !newvalue.matches("\\d+")) {
               
                }else{
                    UPRICE = Integer.parseInt(newvalue);
                    System.out.println("the value changed from "+ oldvalue + "to " + newvalue);
                    TOTAL = QTY * UPRICE;
                    total.setText(String.valueOf(TOTAL));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } 
            });
    }
    
    private String generatepartnerIDS(){
        int number = 1 + (int)(Math.random() * 1000);
        return String.valueOf(number);
        
    }
    
    private void setupHelp(){
        toggle.setTooltip(new Tooltip("Select a mode of Invoice Generation"));
        _generateInvoice.setTooltip(new Tooltip("Generate Invoice"));
        firstname.setTooltip(new Tooltip("Enter / Search Name"));
        origin.setTooltip(new Tooltip("Enter Location"));
        _generatePartnerID.setTooltip(new Tooltip("Generate Customer ID"));
        pname.setTooltip(new Tooltip("Select a Stock"));
        qty.setTooltip(new Tooltip("Enter quantity"));
        unitprice.setTooltip(new Tooltip("Enter unit price"));
        _loadcustomer.setTooltip(new Tooltip("Display the customer selected from the Search Module"));
        _add.setTooltip(new Tooltip("Add Items to the list"));
        amountpaid.setTooltip(new Tooltip("Enter the Amount paid"));
        _viewreceipt.setTooltip(new Tooltip("Generate/View Receipt"));
        _refresh.setTooltip(new Tooltip("Refresh"));
        _clear.setTooltip(new Tooltip("Delete selcted item from the list"));
        
   }
    
    private void autofilldata() {
        firstname.setText(CustomerController.getName());
        origin.setText(CustomerController.getLocation());
        custID.setText(String.valueOf(CustomerController.getCustid()));
    }
    
    private void loadcontrol(String fxml, String name, FontAwesomeIconView OPEN ){
        try {
            Parent employeebody = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(employeebody);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle(name);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(OPEN.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void generateReceipt(String username,Integer id, String date, String location, Integer invoice, Integer total, Integer amtpaid, Integer bal){
       
    }
    
    private void error_message(String _message){
        switch(_message){
            case "invalid_customer" : Alert err1 = new Alert(Alert.AlertType.ERROR);
            err1.setContentText("This customer is not supported for this operation!");
            err1.showAndWait();
            break;
            
            case "invalid_customer_ID" : Alert err2 = new Alert(Alert.AlertType.ERROR);
            err2.setContentText("This customer ID already exist! \nGenerate a new ID");
            err2.showAndWait();
            break;
            
            case "invalid_invoice" : Alert err3 = new Alert(Alert.AlertType.ERROR);
            err3.setContentText("This invoice number already exist! \nGenerate a new invoice number");
            err3.showAndWait();
            break;
            
            case "incomplete_data" : Alert err4 = new Alert(Alert.AlertType.ERROR);
            err4.setContentText("Incomplete data! \nCheck your information");
            err4.showAndWait();
            break;
            
        }
        
    }

    private void emptydata(){
      qty.clear();
      unitprice.clear();
      total.setText("");
      pname.getSelectionModel().clearSelection();

    }
    
    private void emptyalldata(){
      invoiceno.clear();
      GenInvoiceno.setText("");
      firstname.clear();
      origin.clear();
      custID.setText("");
      totalprice.setText("");
      amountpaid.clear();
      balance.setText("");
      invoicetable.getItems().clear();
    }
   
    private Boolean validateB(){
        return GenInvoiceno.getText().matches("\\d+") && !firstname.getText().isEmpty() && !origin.getText().isEmpty() &&  custID.getText().matches("\\d+") &&  amountpaid.getText().matches("\\d+")  &&  balance.getText().matches("\\d+");
    }
    
    private Boolean validateA(){
        return invoiceno.getText().matches("\\d+") && !firstname.getText().isEmpty() && !origin.getText().isEmpty() &&  custID.getText().matches("\\d+")  &&  amountpaid.getText().matches("\\d+")  &&  balance.getText().matches("\\d+") ;
    }
    
    private Boolean validateC(){
        return pname.getValue() != null && qty.getText().matches("\\d+") && unitprice.getText().matches("\\d+") && total.getText().matches("\\d+");
    }
    
    private Boolean check_user_ID(Integer custID, Integer brID, String status, String name){
        return customerDAO.checkCustomer(custID, brID, status, name);
    }
    
    private Boolean check_invoice(Integer invoice, Integer brID){
        return invoiceDAO.checkInvoice(invoice, brID);
    }
    
    private Boolean check_customer_status(Integer brID, Integer custID, String statusname){
        return customerDAO.checkCustomerStatus(custID, brID, statusname);
    }
    
    private Boolean add(String _pname, Integer _qty, Integer _uprice, Integer _total, Integer _count){  
        return invoicelist.add(new Invoice(_count, _pname, _qty, _uprice, _total ));    
    }
    
    private Boolean checkStockqty(String stocname, Integer qtyneeded, Integer brID){
        Boolean status = true;
        if (!stocname.isEmpty()) {
            if (qtyneeded >  productDAO.ProductRemaining(productDAO.getStockTablename(productDAO.getStockID(stocname, brID), brID))) {
                status = false;
            }
        }
        return status;
    }
    
    private Boolean send(Integer brID, Integer custID, String fname, String origin, String statusname, Integer invoice, Integer tprice, Integer balance, ObservableList<Invoice>_item){
        Boolean status = false;
        if (customerDAO.SendCustomer(new Customer(custID, fname, origin), brID, statusname, invoice) &&  invoiceDAO.SendInvoice(_item, invoice, false, custID, brID)   &&  invoiceDAO.sendInvoiceNuumber(invoice, statusname, custID, tprice, balance, brID )   ) {
           status = true;
        }
        return status;
    }
    
    private Integer getstockprice(String stockname, Integer brID){
        return  priceDAO.getStockPrice(productDAO.getStockID(stockname, brID));
    }
    
    private Integer calculatebalance(Integer amtpaid, Integer tOtal){
        Integer calbal = amtpaid - tOtal;
        if (calbal < 0) {
            calbal *= -1 ;
        }
        return calbal;
    }
    
    private String displaydateandtime(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }
    
    private String invoiceIDS(){
        String value = "";
        for (int i = 0; i < 6; i++) {
            int number = 1 + (int)(Math.random() * 10);
            value += String.valueOf(number); 
        }
        return value;
    }
    
    
}
