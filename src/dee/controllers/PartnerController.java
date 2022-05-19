
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.CustomerDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.InvoiceDAO;
import dee.DAO.PartnerDAO;
import dee.DAO.PriceDAO;
import dee.DAO.ProductDAO;
import dee.models.Customer;
import dee.models.Invoice;
import dee.models.Partner;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
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


public class PartnerController implements Initializable {

    @FXML
    private VBox busass;
    @FXML
    private JFXTextField firstname;
    @FXML
    private JFXTextField origin;
    @FXML
    private JFXComboBox<String> pname;
    @FXML
    private JFXTextField qty;
    @FXML
    private JFXTextField unitprice;
    @FXML
    private Label total;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private TableView<Partner> invoicetable;
    @FXML
    private TableColumn<Partner, String> productcol;
    @FXML
    private TableColumn<Partner, Integer> qtycol;
    @FXML
    private TableColumn<Partner, Integer> unitpricecol;
    @FXML
    private TableColumn<Partner, Integer> totalcol;
    @FXML
    private Label totalprice;
    @FXML
    private JFXTextField amountpaid;
    @FXML
    private Label balance;
    @FXML
    private TableView<Partner> customerlist;
    @FXML
    private TableColumn<Partner, Integer> nocol;
    @FXML
    private TableColumn<Partner, String> fnamecol;
    @FXML
    private TableColumn<Partner, String> lnamecol;
    @FXML
    private TableColumn<Partner, Integer> partnerID1;
    @FXML
    private Label totalQty;
    @FXML
    private JFXToggleButton toggle;
    @FXML
    private HBox invoicebody;
    @FXML
    private JFXTextField invoiceno1;
    @FXML
    private HBox genInvoicebody;
    @FXML
    private Label GenInvoiceno;
    @FXML
    private JFXButton _generateInvoice;
    @FXML
    private JFXButton _add;
    @FXML
    private JFXButton _calculatebalance;
    @FXML
    private JFXButton _printreceipt;
    @FXML
    private JFXButton _print;
    @FXML
    private JFXButton _send;
    @FXML
    private JFXButton _refresh;
    @FXML
    private JFXButton _clear;
    
    Integer pricetotal = 0; Integer qtytotal = 0; Integer partnerID = null; private Integer QTY = 0; private Integer UPRICE = 0; private Integer TOTAL = 0;
    private SimpleIntegerProperty index = new SimpleIntegerProperty();
    ObservableList<Partner> partnerlist = FXCollections.observableArrayList(); ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
    ObservableList<Partner> distributorlist = FXCollections.observableArrayList();
    private ProductDAO productDAO; private PriceDAO priceDAO; private EmployeeDAO employeeDAO; private PartnerDAO partnerDAO; private InvoiceDAO invoiceDAO; private CustomerDAO customerDAO;
    
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       setupHelp(); productDAO = new ProductDAO();  priceDAO = new PriceDAO(); employeeDAO = new EmployeeDAO(); partnerDAO = new PartnerDAO(); invoiceDAO = new InvoiceDAO(); customerDAO = new CustomerDAO();
       initcol(); selectIDs(); fillstock(employeeDAO.getBranchID(MainviewController.getBranch()));
       loadpartners( employeeDAO.getBranchID(MainviewController.getBranch()), "Partner");
    }    

    @FXML
    private void add(ActionEvent event) {
        if( validateC()  ){
            if (checkStockqty(pname.getValue(), Integer.parseInt(qty.getText()), employeeDAO.getBranchID(MainviewController.getBranch()))) {
                partnerlist.add(new Partner(0,1, pname.getValue(), Integer.parseInt(qty.getText()), Integer.parseInt(unitprice.getText()), Integer.parseInt(total.getText())));
                pricetotal += Integer.parseInt(total.getText());
                qtytotal += Integer.parseInt(qty.getText());
                totalprice.setText(String.valueOf(pricetotal)); 
                totalQty.setText(String.valueOf(qtytotal)); 
                emptydata();
            }else{
                Alert nosave = new Alert(Alert.AlertType.ERROR);
                nosave.setContentText("The Stock requested is more than what is in Store. \n Please Restock!");
                nosave.showAndWait();
            }  
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.showAndWait();
        }     
    }

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void print(ActionEvent event) {
        
    }
    
    @FXML
    private void send(ActionEvent event) {
            if (validate()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("do you want to save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if ( partnerID != null && check_customer_status(employeeDAO.getBranchID(MainviewController.getBranch()), partnerID, "Partner")) {
                    if (validateA()) {
                        if (send(Integer.parseInt(invoiceno1.getText()), partnerID , orderStatus(Integer.parseInt(amountpaid.getText()),Integer.parseInt(balance.getText()), Integer.parseInt(totalprice.getText()) ), orderComplete(Integer.parseInt(amountpaid.getText()),Integer.parseInt(balance.getText()), Integer.parseInt(totalprice.getText())), "Partner", Integer.parseInt(totalprice.getText()), Integer.parseInt(balance.getText()), employeeDAO.getBranchID(MainviewController.getBranch()), Integer.parseInt(totalQty.getText()), Integer.parseInt(amountpaid.getText()), 1, firstname.getText(), origin.getText())) { 
                            emptyalldata();
                            Alert finish = new Alert(Alert.AlertType.INFORMATION);
                            finish.setContentText("data sent!");
                            finish.showAndWait();
                        }
                    }else if (validateB()){
                        if (send(Integer.parseInt(GenInvoiceno.getText()), partnerID , orderStatus(Integer.parseInt(amountpaid.getText()),Integer.parseInt(balance.getText()), Integer.parseInt(totalprice.getText()) ), orderComplete(Integer.parseInt(amountpaid.getText()),Integer.parseInt(balance.getText()), Integer.parseInt(totalprice.getText())), "Partner", Integer.parseInt(totalprice.getText()), Integer.parseInt(balance.getText()), employeeDAO.getBranchID(MainviewController.getBranch()), Integer.parseInt(totalQty.getText()), Integer.parseInt(amountpaid.getText()), 1 , firstname.getText(), origin.getText())) {
                            emptyalldata();
                            Alert finish = new Alert(Alert.AlertType.INFORMATION);
                            finish.setContentText("data sent!");
                            finish.showAndWait();
                        }
                    }
                }else{
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setContentText("This customer is not supported for this operation!");
                    err.showAndWait();
                }
                
                
                
            }         
        }  
    }

    @FXML
    private void refresh(ActionEvent event) {
        invoicetable.getItems().clear();
    }

    @FXML
    private void clear(ActionEvent event) {
        if (!partnerlist.isEmpty()) {
            int i = index.get();  Integer qtysum = 0; Integer pricesum = 0;
            if(i > -1){
                partnerlist.remove(i);
                invoicetable.getSelectionModel().clearSelection();
                for (int j = 0; j < partnerlist.size(); j++) {
                    qtysum += partnerlist.get(j).getNumberofgoods();
                    pricesum += partnerlist.get(j).getTotalprice();
                }
                totalQty.setText(String.valueOf(qtysum));
                totalprice.setText(String.valueOf(pricesum));
            }
        }  
    }

    @FXML
    private void calculatebalance(ActionEvent event) {
        if (amountpaid.getText().matches("\\d+") && totalprice.getText().matches("\\d+")) {
            balance.setText(calbal(Integer.parseInt(amountpaid.getText()), Integer.parseInt(totalprice.getText())).toString());
        }
       
    }
    
    @FXML
    private void getstockprice(ActionEvent event) {
        if (pname.getValue() != null) {
            unitprice.setText(String.valueOf(getstockprice(pname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))));
        }
    }
    
    @FXML
    private void generateInvoice(ActionEvent event) {
        String value = "";
        for (int i = 0; i < 6; i++) {
            int number = 1 + (int)(Math.random() * 10);
            value += String.valueOf(number); 
        }
        GenInvoiceno.setText(value);
    }

    @FXML
    private void printreceipt(ActionEvent event) {
    }
    
    private void setupHelp(){
        toggle.setTooltip(new Tooltip("Select a mode of Invoice Generation"));
        _generateInvoice.setTooltip(new Tooltip("Generate Invoice"));
        firstname.setTooltip(new Tooltip("Enter Name"));
        origin.setTooltip(new Tooltip("Enter Location"));
        pname.setTooltip(new Tooltip("Select a Stock"));
        qty.setTooltip(new Tooltip("Enter Quantity"));
        unitprice.setTooltip(new Tooltip("Enter Unit Price"));
        total.setTooltip(new Tooltip("Total"));
        _add.setTooltip(new Tooltip("Add Item to list"));
        amountpaid.setTooltip(new Tooltip("Enter Amount Paid"));
        _calculatebalance.setTooltip(new Tooltip("Generate Balance"));
        _printreceipt.setTooltip(new Tooltip("Generate Receipt"));
        _print.setTooltip(new Tooltip("Print"));
        _send.setTooltip(new Tooltip("Save"));
        _refresh.setTooltip(new Tooltip("Refresh"));
        _clear.setTooltip(new Tooltip("Delete selected item from the list"));
    }
    
    private void initcol() {
        index.set(-1);
        productcol.setCellValueFactory(new PropertyValueFactory("stockname"));
        qtycol.setCellValueFactory(new PropertyValueFactory("numberofgoods"));
        unitpricecol.setCellValueFactory(new PropertyValueFactory("unitprice"));
        totalcol.setCellValueFactory(new PropertyValueFactory("totalprice"));
        invoicetable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(partnerlist.indexOf(newvalue));
            System.out.println("OK, the index is " + partnerlist.indexOf(newvalue));
        });  
        invoicetable.setItems(partnerlist);
        nocol.setCellValueFactory(new PropertyValueFactory("id"));
        fnamecol.setCellValueFactory(new PropertyValueFactory("fname"));
        lnamecol.setCellValueFactory(new PropertyValueFactory("lname"));
        partnerID1.setCellValueFactory(new PropertyValueFactory("PartID"));
        customerlist.setItems(distributorlist);
        customerlist.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());
        setTotalAmount();
        
    }

    private void fillstock(Integer brID) {
       pname.setItems(productDAO.getProduct(brID));
    }

    private void emptydata() {
       qty.clear();
       unitprice.clear();
       total.setText("");
       pname.getSelectionModel().clearSelection();   
    }

    private void emptyalldata() {
      GenInvoiceno.setText("");
      invoiceno1.clear();
      firstname.clear();
      origin.clear();
      totalprice.setText("");
      totalQty.setText("");
      amountpaid.setText("");
      balance.setText("");
      partnerID = null;
      invoicetable.getItems().clear();
    }

    private void loadpartners(Integer brID, String partner) {
        distributorlist = partnerDAO.getPartnerList(partner, brID);
        customerlist.setItems(distributorlist);
    }

    private void selectIDs() {
        toggle.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (toggle.isSelected() == true) {
                genInvoicebody.setOpacity(0);
                invoicebody.setOpacity(1);
                invoiceno1.setEditable(true);
            } else {
                invoicebody.setOpacity(0);
                genInvoicebody.setOpacity(1);
                invoiceno1.setEditable(false);
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
    
    private Integer calbal(Integer amtpaid, Integer totalprice){
        Integer _balance = amtpaid - totalprice;
        if (_balance < 0) {
            _balance *= -1;
        }
        return _balance;
    }
    
    private Integer getstockprice(String stockname, Integer brID){
        Integer stPrice = 0;
        if (stockname.isEmpty()) {
            
        }else{
             stPrice = priceDAO.getStockPrice(productDAO.getStockID(stockname, brID));
        }  
        return stPrice;
    }
    
    private String orderStatus(Integer amtpaid, Integer balance, Integer totalprice){
        String status = "";
        if (amtpaid == 0 && balance == 0 ) {
            status = "pending";
        } else if((calbal(amtpaid, totalprice)) > 0){
            status = "pending";
        }else if(calbal(amtpaid, totalprice) == 0){
            status = "success";
        }      
      
        return status;
    }
    
    private String orderComplete(Integer amtpaid, Integer balance, Integer totalprice){
        String status = "";
        if(orderStatus(amtpaid,balance, totalprice ).equals("success")){
            status = "success";
        }else{
            status = "";
        }      
        return status;
    }
    
    private Boolean checkStockqty(String stocname, Integer qtyneeded, Integer brID){
        Boolean status = true;
        if (stocname.isEmpty()) {
            
        }else{      
            if (qtyneeded >  productDAO.ProductRemaining(productDAO.getStockTablename(productDAO.getStockID(stocname, brID), brID))) {
                status = false;
            }
        }
        return status;
    }
  
    private Boolean send(Integer invoiceno, Integer CUSTID, String orderstatus, String statuscomplete, String owner, Integer totprice, Integer bal, Integer brID, Integer totqty, Integer amtpaid, Integer updatecount, String name, String locate ){
        Boolean status = false;
        if (invoiceDAO.sendInvoiceNuumber(invoiceno, owner, CUSTID, totprice, bal, brID)) {
            if (partnerDAO.SendOrder(new Partner(CUSTID, 0, orderstatus, statuscomplete), invoiceno, brID)) {
                if (partnerDAO.SendCreditGoods(partnerlist, brID, invoiceno, CUSTID)) {
                    if (partnerDAO.SendPartnerTransact(new Partner(0, updatecount, invoiceno, totqty, totprice, amtpaid, bal), invoiceno, CUSTID, brID)) {
                        if (customerDAO.SendCustomer(new Customer(CUSTID, name, locate), brID, owner,invoiceno)) {
                            invoicelist = invoiceDAO.ConvertToInvoice(partnerlist);
                            if (invoiceDAO.SendInvoice(invoicelist, invoiceno, status, CUSTID, brID)) {
                                status = true;
                            }
                            
                        }
                    }
                }
            }
        }
        return status;
    }
   
    private Boolean validate(){
        return !partnerlist.isEmpty() && !firstname.getText().isEmpty() && !origin.getText().isEmpty() && amountpaid.getText().matches("\\d+") && balance.getText().matches("\\d+") ;
    }
    
    private Boolean validateA(){
        return toggle.isSelected() == true && invoiceno1.getText().matches("\\d+");
    }
    
    private Boolean validateB(){
        return GenInvoiceno.getText().matches("\\d+");
    }
    
    private Boolean validateC(){
        return pname.getValue() != null && qty.getText().matches("\\d+") && unitprice.getText().matches("\\d+") && total.getText().matches("\\d+");
    }
    
    private Boolean check_customer_status(Integer brID, Integer custID, String statusname){
        return customerDAO.checkCustomerStatus(custID, brID, statusname);
    }
    
    private class RowSelectChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov, 
            Number oldVal, Number newVal) {
            int ix = newVal.intValue();
            if ((ix < 0) || (ix >= distributorlist.size())) {
                return; // invalid data
            }
            Partner custom = distributorlist.get(ix);
            firstname.setText(custom.getFname());
            origin.setText(custom.getLname());
            partnerID = custom.getPartID();
        }
    }
    
    
    
   
    
}
