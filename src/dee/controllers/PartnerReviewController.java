
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.CustomerDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.InvoiceDAO;
import dee.DAO.PartnerDAO;
import dee.DAO.ProductDAO;
import dee.DAO.WarehouseOrderDAO;
import dee.models.Customer;
import dee.models.Invoice;
import dee.models.Partner;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class PartnerReviewController implements Initializable {

    @FXML
    private ComboBox<Integer> invoiceno;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private TableView<Partner> invoicetable;
    @FXML
    private TableColumn<Partner, Integer> idcol;
    @FXML
    private TableColumn<Partner, String> productcol;
    @FXML
    private TableColumn<Partner, Integer> qtycol;
    @FXML
    private TableColumn<Partner, Integer> unitpricecol;
    @FXML
    private TableColumn<Partner, Integer> totalcol;
    @FXML
    private Label totalQty;
    @FXML
    private Label totalprice;
    @FXML
    private VBox partnerbody;
    @FXML
    private Label fname;
    @FXML
    private Label origin;
    @FXML
    private Label partID;
    @FXML
    private Label patdate;
    @FXML
    private JFXButton _print;
    @FXML
    private JFXButton _save;
    @FXML
    private JFXButton _refresh;
    @FXML
    private JFXButton _clear;
    
    Integer pricetotal = 0; Integer qtytotal = 0; String OrderSTA = ""; String OrderCOM = "";  Integer totalqty = 0;
    private SimpleIntegerProperty index = new SimpleIntegerProperty();
    ObservableList<Partner> partnerlist = FXCollections.observableArrayList(); ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
    private PartnerDAO partnerDAO; private InvoiceDAO invoiceDAO; private CustomerDAO customerDAO; private EmployeeDAO employeeDAO; private WarehouseOrderDAO productDAO; private ProductDAO proDAO;
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp();  partnerDAO = new PartnerDAO(); invoiceDAO = new InvoiceDAO(); customerDAO = new CustomerDAO();  employeeDAO = new EmployeeDAO(); productDAO = new WarehouseOrderDAO();  proDAO = new ProductDAO();
        initcol(); fillinvoice("Partner", employeeDAO.getBranchID(MainviewController.getBranch()));
        
    }    

    @FXML
    private void loadinformation(ActionEvent event) {
        if (invoiceno.getValue() != null) {
            loadinformation(invoiceno.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()));
        }
    }

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void print(ActionEvent event) {
        print();
    }

    @FXML
    private void save(ActionEvent event) {
        if (!partnerlist.isEmpty() && invoiceno.getValue() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Data");
            alert.setContentText("Are you sure you want to save this information?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (save(invoiceno.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()), "Partner", Integer.parseInt(partID.getText()), fname.getText(), origin.getText(), Integer.parseInt(totalQty.getText()), Integer.parseInt(totalprice.getText()), OrderSTA, OrderCOM, 1,totalqty,(Integer.parseInt(totalQty.getText()) - Integer.parseInt(totalprice.getText()) ), partnerDAO.getPartnerBalanceSheet(Integer.parseInt(partID.getText()), employeeDAO.getBranchID(MainviewController.getBranch())) )) {
                    Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                    nosave.setContentText("data Saved!");
                    nosave.showAndWait();
                    emptyalldata(); clearList(); invoicetable.getItems().clear();
                    fillinvoice("Partner", employeeDAO.getBranchID(MainviewController.getBranch()));
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
        if (!partnerlist.isEmpty() && invoiceno.getValue() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Data");
            alert.setContentText("Are you sure you want to delete this information?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (delete(invoiceno.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()), "Partner", Integer.parseInt(partID.getText()), fname.getText(), origin.getText(), Integer.parseInt(totalQty.getText()), Integer.parseInt(totalprice.getText()), OrderSTA, OrderCOM, 1,totalqty,(Integer.parseInt(totalQty.getText()) - Integer.parseInt(totalprice.getText()) ))) {
                    emptyalldata(); clearList(); invoicetable.getItems().clear();
                    Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                    nosave.setContentText("data deleted!");
                    nosave.showAndWait();
                    fillinvoice("Partner", employeeDAO.getBranchID(MainviewController.getBranch()));
                }
            }
        } 
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
    
    private void setupHelp(){
        invoiceno.setTooltip(new Tooltip("Select Invoice"));
        _print.setTooltip(new Tooltip("Print"));
        _save.setTooltip(new Tooltip("Save"));
        _refresh.setTooltip(new Tooltip("Delete selected Details "));
        _clear.setTooltip(new Tooltip("Refresh"));
    }

    private void fillinvoice(String partner, Integer brID) {
        invoiceno.setItems(invoiceDAO.getInvoicePartnerTempList(partner, brID));
    }
    
    private void loadinformation(Integer invoicenumber, Integer brID){
            Partner partner = partnerDAO.getBalanceDetails(invoicenumber, brID);
            partID.setText(String.valueOf(partner.getPartnerid()));
            patdate.setText(partner.getFirstname());
            totalprice.setText(String.valueOf(partner.getTotalPrice()));
            totalQty.setText(String.valueOf(partner.getTotalNumberofGood()));
            totalqty = partner.getOrderid();
            
            Customer getcustomer = customerDAO.getcustomerdetail(Integer.parseInt(partID.getText()), brID);
            fname.setText(getcustomer.getFname());
            origin.setText(getcustomer.getOrigin());

            Partner order = partnerDAO.getOrderDetails(invoicenumber, brID);
            OrderSTA = order.getTaborderstatus();
            OrderCOM = order.getTabdate();
            System.out.println(OrderSTA);
            System.out.println(OrderCOM);
            
            partnerlist = partnerDAO.getPartnerInvoiceDetails(invoicenumber, brID);
            invoicetable.setItems(partnerlist);
  
    }

    private void initcol() {
        index.set(-1);
        idcol.setCellValueFactory(new PropertyValueFactory("id"));
        productcol.setCellValueFactory(new PropertyValueFactory("stockname"));
        qtycol.setCellValueFactory(new PropertyValueFactory("numberofgoods"));
        unitpricecol.setCellValueFactory(new PropertyValueFactory("unitprice"));
        totalcol.setCellValueFactory(new PropertyValueFactory("totalprice"));
        invoicetable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(partnerlist.indexOf(newvalue));
            System.out.println("OK, the index is " + partnerlist.indexOf(newvalue));
        });  
        invoicetable.setItems(partnerlist);
       
    }
    
    private void emptyalldata() {
        invoiceno.getSelectionModel().clearSelection();
        fname.setText("");
        origin.setText("");      
        partID.setText("*");     
        totalQty.setText("");
        totalprice.setText("");
        patdate.setText("");
        fillinvoice("Partner", employeeDAO.getBranchID(MainviewController.getBranch()));
    }
    
    private void print() {
        if (partnerlist.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("no data to print ");
            alert.showAndWait(); 
        }else {
            String name = fname.getText();
           
        }
    }
    
    private void clearList() {
        partnerlist.clear();
    }
    
    private Boolean delete(Integer invoicenum, Integer brID, String detail, Integer custid, String name, String locate, Integer totprice, Integer balance, String orderstatus, String statuscomplete, Integer updatecount, Integer totqty, Integer amtpaid){
      Boolean status = false;
      if (customerDAO.DeleteCustomerReview(new Customer(custid, name, locate), brID, detail, invoicenum)) {
            if (customerDAO.deleteCustomerTempList(custid, brID, invoicenum)) {
                if (invoiceDAO.DeletesentInvoiceNuumber(invoicenum, detail, custid, totprice, balance, brID)) {
                    if (invoiceDAO.DeletePermSentInvoiceNumber(invoicenum, brID)) {
                        if (partnerDAO.DeleteSentCreditGoods(partnerlist, brID, invoicenum, custid)) {
                            if (partnerDAO.DeletePermSentCreditGoods(invoicenum, brID)) {
                                if (partnerDAO.DeleteSentOrder(new Partner(custid, 0, orderstatus, statuscomplete), invoicenum, brID)) {
                                    if (partnerDAO.DeletePermSentOrder(invoicenum, brID)) {
                                        if (partnerDAO.DeleteSentPartnerTransact(new Partner(0, updatecount, invoicenum, totqty, totprice, amtpaid, balance), invoicenum, custid, brID)) {
                                            if (partnerDAO.DeletePermSentPartnerTransact(invoicenum, brID)) {
                                                invoicelist = invoiceDAO.ConvertToInvoice(partnerlist);
                                                if (invoiceDAO.DeleteSentInvoice(invoicelist, invoicenum, status, custid, brID)) {
                                                    if (invoiceDAO.DeletePermSentInvoice(invoicenum, brID)) {
                                                        status = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
      
      return status;
    }
    
    private Boolean save(Integer invoicenum, Integer brID, String detail, Integer custid, String name, String locate, Integer totprice, Integer balance, String orderstatus, String statuscomplete, Integer updatecount, Integer totqty, Integer amtpaid, String accout){
        Boolean status = false;
            if (customerDAO.CreateCustomer(new Customer(custid, name, locate), brID, detail)) {
                if (invoiceDAO.saveInvoiceNuumber(invoicenum, detail, custid, totprice, balance, brID)) {
                    if (partnerDAO.SaveOrder(new Partner(custid, 0, orderstatus, statuscomplete), invoicenum, brID)) {
                        if (partnerDAO.SaveCreditGoods(partnerlist, brID, invoicenum, custid)) {
                            if (partnerDAO.SavePartnerTransact(new Partner(0, updatecount, invoicenum, totqty, totprice, amtpaid, balance), invoicenum, custid, brID)) {
                                if (customerDAO.deleteCustomerTempList(custid, brID, invoicenum)) {
                                    if (invoiceDAO.DeletePermSentInvoiceNumber(invoicenum, brID)) {
                                        if (partnerDAO.DeletePermSentCreditGoods(invoicenum, brID)) {
                                            if (partnerDAO.DeletePermSentOrder(invoicenum, brID)) {
                                                if (partnerDAO.DeletePermSentPartnerTransact(invoicenum, brID)) {
                                                    invoicelist = invoiceDAO.ConvertToInvoice(partnerlist);
                                                    if (invoiceDAO.SaveInvoice(invoicelist, invoicenum, status, custid, brID)) {
                                                        if (invoiceDAO.DeletePermSentInvoice(invoicenum, brID)) {
                                                            if (productDAO.deductStockAccount(invoicelist, invoicenum, brID)) {
                                                                if (partnerDAO.updatePartnerAccount(partnerlist, brID, invoicenum, custid, "Sold", orderstatus)) {
                                                                    if (partnerDAO.SavePartnerBalanceSheet(new Partner(0, updatecount, invoicenum, totqty, totprice, amtpaid, balance), accout)) {
                                                                        if (checkamount(amtpaid)) {
                                                                            if (proDAO.createAnalysis(brID, 0, "Head Office Sales", amtpaid)) {
                                                                                System.out.println("the analysis sent");
                                                                                status = true;
                                                                            }
                                                                            
                                                                        }else{
                                                                            status = true;
                                                                            System.out.println("the analysis not sent");
                                                                        }
                                                                    } 
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        return status;
    }
    
    private Boolean checkamount(Integer amountpaid){
        return amountpaid != 0;
    }
    

    
    
}
