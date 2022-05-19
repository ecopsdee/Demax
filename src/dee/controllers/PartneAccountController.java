
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.CustomerDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.PartnerDAO;
import dee.database.deeinventory;
import dee.models.Customer;
import dee.models.Partner;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;


public class PartneAccountController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXTextField firstname;
    @FXML
    private JFXTextField origin;
    @FXML
    private JFXTextField partnerID;
    @FXML
    private JFXTextField account;
    @FXML
    private JFXButton _generateAccount;
    @FXML
    private JFXButton generatePartnerID;
    @FXML
    private JFXButton _add;
    
    private CustomerDAO customerDAO;  private PartnerDAO partnerDAO; private EmployeeDAO employeeDAO; 
    private deeinventory DbSet;
    

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); DbSet = deeinventory.getinstance();   customerDAO = new CustomerDAO();   partnerDAO = new PartnerDAO();    employeeDAO = new EmployeeDAO();
    }    

    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void generatePartnerID(ActionEvent event) {
        Integer gen_ID = 0;
        do {            
            gen_ID = Integer.parseInt(generatePartnerID());
        } while (customerDAO.checkCustomerID(gen_ID, employeeDAO.getBranchID(MainviewController.getBranch())));
        
        partnerID.setText(gen_ID.toString());
    }

    @FXML
    private void add(ActionEvent event) {
        if (!firstname.getText().isEmpty() && !origin.getText().isEmpty() && !account.getText().isEmpty() && partnerID.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (send(captitalizeWord(firstname.getText()), captitalizeWord(origin.getText()), (account.getText() + "_Partner"), Integer.parseInt(partnerID.getText()),employeeDAO.getBranchID(MainviewController.getBranch()), "Partner", (account.getText() + "_BalanceSheet") )) {
                    clear();
                    Alert item = new Alert(Alert.AlertType.INFORMATION);
                    item.setContentText("Account created!");
                    item.showAndWait();
                }
            }      
        }
    }
    
    @FXML
    private void generateAccount(ActionEvent event) {
        account.setText(generateAccount());
    }
    
    
    private String generatePartnerID() {
        int number = 1 + (int)(Math.random() * 1000);
        String num = "";
        num += number;
        return num;
    }
    
    private String generateAccount(){
        String value = "398";
        for (int i = 0; i < 7; i++) {
            int number = 1 + (int)(Math.random() * 10);
            value += String.valueOf(number); 
        }
        return  value ;  
    }
    
    private Boolean send(String name, String location, String account, Integer custID, Integer brID, String detail, String balancesheet){
        Boolean status = false;
        if (DbSet.setupPartnerAccount(account) && DbSet.setupPartnerBalanceSheet(balancesheet) && partnerDAO.PopulatePartnerBalanceSheet(balancesheet)) {
            if (customerDAO.CreateCustomer(new Customer(custID, name, location), brID, detail)) {
                if (partnerDAO.SaveDistributor(new Partner(custID, brID, account, balancesheet))) {
                    status = true;
                }
            }
        }
        return status;
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
    
    private void setupHelp(){
        firstname.setTooltip(new Tooltip("Enter Name"));
        origin.setTooltip(new Tooltip("Origin"));
        _generateAccount.setTooltip(new Tooltip("Generate Account"));
        generatePartnerID.setTooltip(new Tooltip("Generate Partner ID"));
        _add.setTooltip(new Tooltip("Save"));
        
    }
    
    private void clear(){
        firstname.clear();
        origin.clear();
        partnerID.clear();
        account.clear();
    }

    
}
