
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class BranchController implements Initializable {

    @FXML
    private VBox mainbody;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXTextField branchname;
    @FXML
    private JFXTextField branchinfo;
    @FXML
    private JFXTextField branchaccount;
    @FXML
    private TableView<Branch> branchtable;
    @FXML
    private TableColumn<Branch, String> brnamecol;
    @FXML
    private TableColumn<Branch, String> braccountcol;
    @FXML
    private TableColumn<Branch, String> brinfocol;
    @FXML
    private JFXComboBox<String> pname;
    @FXML
    private JFXTextField qty;
    @FXML
    private JFXTextField unitprice;
    @FXML
    private TableView<Branch> branchstocktable;
    @FXML
    private TableColumn<Branch, String> pnamecol;
    @FXML
    private TableColumn<Branch, Integer> qtycol;
    @FXML
    private TableColumn<Branch, Integer> unitpricecol;
    @FXML
    private Label totalPRICE;
    @FXML
    private Label totalprice;
    @FXML
    private JFXComboBox<String> branch;
    @FXML
    private TableColumn<Branch, Integer> totpricecol;
    @FXML
    private JFXTextField balanceaccount;
    @FXML
    private TableColumn<Branch, String> brbalanceacc;
    @FXML
    private JFXButton _generateaccount;
    @FXML
    private JFXButton _addbranch;
    @FXML
    private JFXButton _savebranch;
    @FXML
    private JFXButton _deletebranch;
    @FXML
    private JFXButton _addbranchstock;
    @FXML
    private Button _savestock;
    @FXML
    private Button _deletestock;
    @FXML
    private Button _refreshtable;
    
    BranchDAO branchDAO; EmployeeDAO employeeDAO; ProductDAO productDAO; PriceDAO priceDAO;
    ObservableList<Branch> brlist = FXCollections.observableArrayList();
    ObservableList<Branch> stlist = FXCollections.observableArrayList();
    Integer SBID = 0; private Integer QTY = 0; private Integer UPRICE = 0; private Integer TOTAL = 0; Integer pricetotal = 0;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); setTotalAmount(); InitCol(); branchDAO = new BranchDAO(); employeeDAO = new EmployeeDAO(); productDAO = new ProductDAO(); priceDAO = new PriceDAO();
        fillcombo();
    }    

    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void generateAccount(ActionEvent event) {
        branchaccount.setText(generateBranchAccount());
        balanceaccount.setText(generateBalanceAccount());
    }

    @FXML
    private void addbranch(ActionEvent event) {
        if (validateA()) {
            SBID = generateID();
            while (branchDAO.checkBranchID(SBID)) {                
                SBID = generateID();
            }
            if (validateC(brlist, branchname.getText(), branchaccount.getText(), balanceaccount.getText())) {
                brlist.add(new Branch(branchname.getText(), branchaccount.getText(), branchinfo.getText(), balanceaccount.getText(), SBID));
                ClearA();
            }else{
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setContentText("Item already exist!");
                err.showAndWait();
            }
            
            
            
        }
    }

    @FXML
    private void savebranch(ActionEvent event) {
        if (!brlist.isEmpty()) {
            Alert save = new Alert(Alert.AlertType.CONFIRMATION);
            save.setContentText("Do you want to save?");
            Optional<ButtonType> result = save.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (save()) {
                    Alert ok = new Alert(Alert.AlertType.CONFIRMATION);
                    ok.setContentText("Sent to Server!");
                    ok.showAndWait();
                    ClearB();
                }
            }
        }
    }

    @FXML
    private void deletebranch(ActionEvent event) {
    }

    @FXML
    private void addbranchstock(ActionEvent event) {
        if (validateB()) {
            if (checkStockqty(pname.getValue(), QTY, employeeDAO.getBranchID(MainviewController.getBranch()))) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setContentText("The Stock requested is more than what is in Store. \nPlease Restock!");
                err.showAndWait();
            }else{
                if (!stlist.isEmpty()) {
                    if (checkitem(stlist, pname.getValue())) {
                        stlist.set(index(stlist, pname.getValue()), new Branch(pname.getValue(), total(lastQty(index(stlist, pname.getValue()), stlist), Integer.parseInt(qty.getText())), Integer.parseInt(unitprice.getText()), Total(Integer.parseInt(unitprice.getText()), total(lastQty(index(stlist, pname.getValue()), stlist), Integer.parseInt(qty.getText())))));
                        totalPRICE.setText(String.valueOf(total(stlist)));
                        ClearC(); ClearD();
                    }else{
                        additem(stlist, pname.getValue(), Integer.parseInt(qty.getText()), Integer.parseInt(unitprice.getText()), Integer.parseInt(totalprice.getText()));
                        totalPRICE.setText(String.valueOf(total(stlist)));
                        ClearC(); ClearD();
                    }
                }else{
                    additem(stlist, pname.getValue(), Integer.parseInt(qty.getText()), Integer.parseInt(unitprice.getText()), Integer.parseInt(totalprice.getText()));
                    totalPRICE.setText(String.valueOf(total(stlist)));
                    ClearC(); ClearD();
                }
                
                
                
                
            }
        }
    }

    @FXML
    private void savestock(ActionEvent event) {
        if (!stlist.isEmpty() && branch.getValue() != null) {
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
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setContentText("The Selected Branch is required for this operation!");
                    err.showAndWait();
                }
                
            }
        }
    }

    @FXML
    private void deletestock(ActionEvent event) {
    }

    @FXML
    private void refreshtable(ActionEvent event) {
        fillcombo();
    }
    
    @FXML
    private void getPrice(ActionEvent event) {
        if (pname.getValue() != null) {
            unitprice.setText(String.valueOf(getstockprice(pname.getValue(), employeeDAO.getBranchID(MainviewController.getBranch()))));
        }
    }
    
    private Boolean save(){
        return branchDAO.createBranch(brlist, employeeDAO.getBranchID(MainviewController.getBranch()));
    }
    
    private Boolean save(Integer brID, String branchNAME){
        return branchDAO.deductStockAccount(stlist, 0, brID) && branchDAO.populateBranchAccount(stlist, branchDAO.subBranchID(branchNAME, brID), brID) && branchDAO.UpdateBalanceSheet(Integer.parseInt(totalPRICE.getText()), branchDAO.getBranchBalanceSheet(branchDAO.subBranchID(branchNAME, brID), brID));
    }
    
    private Boolean validateA(){
        return !branchname.getText().isEmpty() && !branchinfo.getText().isEmpty() && !branchaccount.getText().isEmpty() && !balanceaccount.getText().isEmpty();
    }
    
    private Boolean validateB(){
        return pname.getValue() != null && qty.getText().matches("\\d+") && unitprice.getText().matches("\\d+") && totalprice.getText().matches("\\d+");
    }
    
    private Boolean validateC(ObservableList<Branch> _item, String _brname, String _braccount, String _brbalance){
        Boolean status = false;
        if (!_item.isEmpty()) {
            for (Branch item : _item) {
                if (!_brname.equals(item.getBranchname()) && !_braccount.equals(item.getAccount()) &&! _brbalance.equals(item.getPname()) ) {
                    status = true;
                }
            }
        }else{
            status = true;
        }
        return status;

    }
    
    private Boolean checkStockqty(String stocname, Integer qtyneeded, Integer brID){
        return qtyneeded >  productDAO.ProductRemaining(productDAO.getStockTablename(productDAO.getStockID(stocname, brID), brID));
    }
    
    private Boolean additem(ObservableList<Branch> _item, String _pname, Integer _qty, Integer _unitprice, Integer _totalprice){
        return _item.add(new Branch(_pname, _qty, _unitprice, _totalprice));
    }
    
    private Boolean checkitem(ObservableList<Branch> _item, String _pname){
        Boolean status = false;
        if (!_item.isEmpty()) {
            for (Branch item : _item) {
                if (_pname.equals(item.getPname())) {
                    status = true;
                }
            }
        }
        System.out.println("the model check is " + status);
        return status;
    }
    
    private String generateBranchAccount(){
        String value = "406";
        for (int i = 0; i < 7; i++) {
            int number = 1 + (int)(Math.random() * 10);
            value += String.valueOf(number); 
        }
        return  value + "_Branch" ;  
    }
    
    private String generateBalanceAccount(){
        String value = "406";
        for (int i = 0; i < 7; i++) {
            int number = 1 + (int)(Math.random() * 10);
            value += String.valueOf(number); 
        }
        return  value + "_BranchSheet" ;  
    }
    
    private Integer index(ObservableList<Branch> _item, String _pname){
        Integer indexnumber = null;
        for (int i = 0; i < _item.size(); i++) {
                if (_pname.equals(_item.get(i).getPname())) {
                    indexnumber = i;
                }
            }
        return indexnumber;
    }
    
    private Integer lastQty(Integer index, ObservableList<Branch> _item ){
        return _item.get(index).getQty();
    }
    
    private Integer total(Integer lastamount, Integer newamount){
        return  lastamount + newamount;
    }
    
    private Integer total(ObservableList<Branch> _item){
        Integer result = 0;
        result = _item.stream().map((item) -> item.getTprice()).reduce(result, Integer::sum);
        return result;
    }
    
    private Integer Total(Integer uprice, Integer qty){
        return  uprice * qty;
    }
    
    private Integer generateID(){
        int number = 1 + (int)(Math.random() * 1000);
        return  number;
    }
    
    private Integer getstockprice(String stockname, Integer brID){
        return  priceDAO.getStockPrice(productDAO.getStockID(stockname, brID));
    }
    
    private void setupHelp(){
        branchname.setTooltip(new Tooltip("Enter the Name of the Branch"));
        branchinfo.setTooltip(new Tooltip("Enter Additional Information about the Branch"));
        _generateaccount.setTooltip(new Tooltip("Generate an Branch Account Name & Branch Balance Sheet Account "));
        branchaccount.setTooltip(new Tooltip("A Branch Account Name"));
        balanceaccount.setTooltip(new Tooltip("Branch Balance Sheet Account"));
        _addbranch.setTooltip(new Tooltip("Add Branch Details to the list"));
        _savebranch.setTooltip(new Tooltip("Save Branch Details"));
        _deletebranch.setTooltip(new Tooltip("Delete a selected item from the list"));
        pname.setTooltip(new Tooltip("Select a Stock"));
        qty.setTooltip(new Tooltip("Enter the Quantity of Stock needed"));
        unitprice.setTooltip(new Tooltip("Enter the Unit Price of the selected Stock"));
        _addbranchstock.setTooltip(new Tooltip("Add Details to the list"));
        branch.setTooltip(new Tooltip("Select a Branch"));
        _savestock.setTooltip(new Tooltip("Save the Branch Product Details "));
        _deletestock.setTooltip(new Tooltip("Delete a selected Item from the list"));
        _refreshtable.setTooltip(new Tooltip("Reload all the Stocks and Branch"));
    }
    
    private void fillcombo(){
        pname.setItems(productDAO.getProduct(employeeDAO.getBranchID(MainviewController.getBranch())));
        branch.setItems(branchDAO.getBranch(employeeDAO.getBranchID(MainviewController.getBranch())));
    }
    
    private void ClearA(){
        branchname.clear();
        branchaccount.clear();
        balanceaccount.clear();
        branchinfo.clear();
    }
    
    private void ClearB(){
       branchtable.getItems().clear();
       brlist.clear();
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
        stlist.clear();
        totalPRICE.setText("");
        pricetotal = 0;
        TOTAL = 0;
        branch.getSelectionModel().clearSelection();
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
    
    private void InitCol(){
       brnamecol.setCellValueFactory(new PropertyValueFactory("branchname"));
       braccountcol.setCellValueFactory(new PropertyValueFactory("account"));
       brinfocol.setCellValueFactory(new PropertyValueFactory("otherinfo"));
       brbalanceacc.setCellValueFactory(new PropertyValueFactory("pname"));
       branchtable.setItems(brlist);
       
       pnamecol.setCellValueFactory(new PropertyValueFactory("pname"));
       qtycol.setCellValueFactory(new PropertyValueFactory("qty"));
       unitpricecol.setCellValueFactory(new PropertyValueFactory("uprice"));
       totpricecol.setCellValueFactory(new PropertyValueFactory("tprice"));
       branchstocktable.setItems(stlist);
       
    }

    
    
}
