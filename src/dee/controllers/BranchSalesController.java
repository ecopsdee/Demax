
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.BranchDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.PriceDAO;
import dee.DAO.ProductDAO;
import dee.models.Branch;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.layout.VBox;

public class BranchSalesController implements Initializable {

    @FXML
    private VBox mainbody;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXComboBox<String> pname;
    @FXML
    private JFXTextField qty;
    @FXML
    private JFXTextField unitprice;
    @FXML
    private Label totalprice;
    @FXML
    private JFXComboBox<String> branch;
    @FXML
    private TableView<Branch> branchstocktable;
    @FXML
    private TableColumn<Branch, String> pnamecol;
    @FXML
    private TableColumn<Branch, Integer> qtycol;
    @FXML
    private TableColumn<Branch, Integer> unitpricecol;
    @FXML
    private TableColumn<Branch, Integer> totpricecol;
    @FXML
    private Label totalPRICE;
    @FXML
    private JFXComboBox<String> Pname;
    @FXML
    private JFXTextField Qty;
    @FXML
    private JFXComboBox<String> fromBranch;
    @FXML
    private JFXComboBox<String> ToBranch;
    @FXML
    private TableView<Branch> transfertable;
    @FXML
    private TableColumn<Branch, String> pnamecol1;
    @FXML
    private TableColumn<Branch, Integer> qtycol1;
    @FXML
    private Label totalQTY;
    @FXML
    private JFXButton _addbranchstock;
    @FXML
    private JFXButton _savestock;
    @FXML
    private JFXButton _deletestock;
    @FXML
    private JFXButton _addTransfer;
    @FXML
    private JFXButton _savetransfer;
    @FXML
    private JFXButton _deletetransfer;
    
    ObservableList<Branch> brlist = FXCollections.observableArrayList();  ObservableList<Branch> trlist = FXCollections.observableArrayList();
    BranchDAO branchDAO; EmployeeDAO employeeDAO; ProductDAO productDAO; PriceDAO priceDAO;
    private Integer QTY = 0; private Integer UPRICE = 0; private Integer TOTAL = 0; Integer pricetotal = 0; Integer toqty = 0; Integer totqty = 0;

    
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       setupHelp(); branchDAO = new  BranchDAO(); employeeDAO = new EmployeeDAO(); productDAO = new ProductDAO(); priceDAO = new PriceDAO();
       fillcombo(); setTotalAmount(); Initcol();
    }    

    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void getPrice(ActionEvent event) {
        if (pname.getValue() != null) {
            unitprice.setText(String.valueOf(getstockprice(pname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))));
        }
    }

    @FXML
    private void addbranchstock(ActionEvent event) {
        if (validateB() && branch.getValue() != null) {
            if (checkStockqty(pname.getValue(), QTY, employeeDAO.getBranchID(MainviewController.getBranch()), branch.getValue())) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setContentText("The Stock requested is more than what is in Store. \nPlease Restock!");
                err.showAndWait();
            }else{
                brlist.add(new Branch(pname.getValue(), Integer.parseInt(qty.getText()), Integer.parseInt(unitprice.getText()), Integer.parseInt(totalprice.getText())));
                totalPRICE.setText(String.valueOf(pricetotal += TOTAL));
                ClearC(); ClearD();
            }
        }
    }

    @FXML
    private void savestock(ActionEvent event) {
        if (!brlist.isEmpty() && branch.getValue() != null) {
            Alert save = new Alert(Alert.AlertType.CONFIRMATION);
            save.setContentText("Do you want to save?");
            Optional<ButtonType> result = save.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (branchDAO.subBranchID(branch.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())) != 0) {
                    if (save(employeeDAO.getBranchID(MainviewController.getBranch()), branch.getValue())) {
                        Alert ok = new Alert(Alert.AlertType.INFORMATION);
                        ok.setContentText("Sent to Server");
                        ok.showAndWait();
                        ClearE();
                    }
                }else{
                    Alert ok = new Alert(Alert.AlertType.ERROR);
                    ok.setContentText("select a valid branch");
                    ok.showAndWait();
                }
            }
        }
    }

    @FXML
    private void deletestock(ActionEvent event) {
    }
    
    @FXML
    private void setProduct(ActionEvent event) {
        if (branch.getValue() != null) {
            pname.setItems(branchDAO.getProduct(employeeDAO.getBranchID(MainviewController.getBranch()), branchDAO.subBranchID(branch.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()) )));
        }
    }
    
    @FXML
    private void addTransfer(ActionEvent event) {
            if (validateC() && fromBranch.getValue() != null) {
            if (checkStockqty(Pname.getValue(), Integer.parseInt(Qty.getText()), employeeDAO.getBranchID(MainviewController.getBranch()), fromBranch.getValue())) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setContentText("The Stock requested is more than what is in Store. \nPlease Restock!");
                err.showAndWait();
            }else{
                toqty = Integer.parseInt(Qty.getText());
                trlist.add(new Branch(Pname.getValue(), toqty , 0, 0));
                totalQTY.setText(String.valueOf(totqty += toqty));
                ClearF();
            }
        }
    }

    @FXML
    private void getProduct(ActionEvent event) {
        if (fromBranch.getValue() != null) {
            Pname.setItems(branchDAO.getProduct(employeeDAO.getBranchID(MainviewController.getBranch()), branchDAO.subBranchID(fromBranch.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()) )));
        }
    }

    @FXML
    private void savetransfer(ActionEvent event) {
        if (fromBranch.getValue() != null && ToBranch.getValue() != null) {
            Alert save = new Alert(Alert.AlertType.CONFIRMATION);
            save.setContentText("Do you want to save?");
            Optional<ButtonType> result = save.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (save(branchDAO.subBranchID(fromBranch.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), branchDAO.subBranchID(ToBranch.getValue(), employeeDAO.getBranchID(MainviewController.getBranch())), employeeDAO.getBranchID(MainviewController.getBranch()))) {
                    Alert ok = new Alert(Alert.AlertType.INFORMATION);
                    ok.setContentText("Sent to Server");
                    ok.showAndWait();
                    ClearG();
                }
            }
        }
    }

    @FXML
    private void deletetransfer(ActionEvent event) {
    }
    
    private Boolean validateB(){
        return pname.getValue() != null && qty.getText().matches("\\d+") && unitprice.getText().matches("\\d+") && totalprice.getText().matches("\\d+");
    }
    
    private Boolean validateC(){
        return Pname.getValue() != null && Qty.getText().matches("\\d+") ;
    }
    
    private Boolean checkStockqty(String stocname, Integer qtyneeded, Integer brID, String branch){
        return qtyneeded >=  branchDAO.BranchStockRemain(branchDAO.getBranchStockAccount(branchDAO.subBranchID(branch, brID), brID, productDAO.getStockID(stocname, brID)));
    }
    
    private Boolean save(Integer brID, String branchNAME){
        return  branchDAO.populateStockSale(brlist, branchDAO.subBranchID(branchNAME, brID), brID) && branchDAO.BranchInvoice(brlist, 0, branchDAO.subBranchID(branchNAME, brID), 0, brID) && productDAO.createAnalysis(brID, branchDAO.subBranchID(branchNAME, brID), "Sales From " + branchDAO.getBranchName(branchDAO.subBranchID(branchNAME, brID), brID), Integer.parseInt(totalPRICE.getText()));
    }
    
    private Boolean save(Integer subID1, Integer subID2, Integer brID){
        return branchDAO.TransferBranchStock(trlist, subID1, subID2, brID);
    }
    
    private Integer getstockprice(String stockname, Integer brID){
        return  priceDAO.getStockPrice(productDAO.getStockID(stockname, brID));
    }
    
    private void setupHelp(){
        pname.setTooltip(new Tooltip("Select a Stock"));
        qty.setTooltip(new Tooltip("Enter the quantity"));
        unitprice.setTooltip(new Tooltip("Enter the Price"));
        _addbranchstock.setTooltip(new Tooltip("Add Item to list"));
        branch.setTooltip(new Tooltip("Select a Branch"));
        _savestock.setTooltip(new Tooltip("Save"));
        _deletestock.setTooltip(new Tooltip("Delete item from list"));
        Pname.setTooltip(new Tooltip("Select a Stock"));
        Qty.setTooltip(new Tooltip("Enter the quantity"));
        _addTransfer.setTooltip(new Tooltip("Add Item to list"));
        fromBranch.setTooltip(new Tooltip("Select a Branch"));
        ToBranch.setTooltip(new Tooltip("Select a Branch"));
        _savetransfer.setTooltip(new Tooltip("Save"));
        _deletetransfer.setTooltip(new Tooltip("Delete Item from list"));
    }
    
    private void fillcombo(){
        branch.setItems(branchDAO.getBranch(employeeDAO.getBranchID(MainviewController.getBranch())));
        fromBranch.setItems(branchDAO.getBranch(employeeDAO.getBranchID(MainviewController.getBranch())));
        ToBranch.setItems(branchDAO.getBranch(employeeDAO.getBranchID(MainviewController.getBranch())));
    }
    
    private void ClearC(){
        pname.getSelectionModel().clearSelection();
        qty.clear();
        unitprice.clear();
        totalprice.setText("");
    }
    
    private void ClearD(){
        QTY = 0;
        UPRICE = 0;
        TOTAL = 0;
    }
    
    private void ClearE(){
        branchstocktable.getItems().clear();
        brlist.clear();
        totalPRICE.setText("");
        pricetotal = 0;
        TOTAL = 0;
        branch.getSelectionModel().clearSelection();
        pname.getItems().clear();
        
    }
    
    private void ClearF(){
        Pname.getSelectionModel().clearSelection();
        Qty.clear();
    }
    private void ClearG(){
        transfertable.getItems().clear();
        trlist.clear();
        totalQTY.setText("");
        toqty = 0;
        totqty = 0;
        fromBranch.getSelectionModel().clearSelection();
        ToBranch.getSelectionModel().clearSelection();
        Pname.getItems().clear();
    }
    
    private void setTotalAmount(){
        qty.textProperty().addListener((observable,oldvalue,newvalue)->{
            try {
                if (newvalue.equals("") || !newvalue.matches("\\d+")) {
               
                }else{
                    QTY = Integer.parseInt(newvalue);
                    System.out.println("the value changed from "+ oldvalue + "to " + newvalue);
                    TOTAL = QTY * UPRICE;
                    totalprice.setText(String.valueOf(TOTAL));
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
                    totalprice.setText(String.valueOf(TOTAL));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } 
        });
    }
    
    private void Initcol(){
       pnamecol.setCellValueFactory(new PropertyValueFactory("pname"));
       qtycol.setCellValueFactory(new PropertyValueFactory("qty"));
       unitpricecol.setCellValueFactory(new PropertyValueFactory("uprice"));
       totpricecol.setCellValueFactory(new PropertyValueFactory("tprice"));
       branchstocktable.setItems(brlist);
       
       pnamecol1.setCellValueFactory(new PropertyValueFactory("pname"));
       qtycol1.setCellValueFactory(new PropertyValueFactory("qty"));
       transfertable.setItems(trlist);
       
    }

    

    
    
    
}
