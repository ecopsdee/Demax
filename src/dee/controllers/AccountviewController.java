
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import dee.DAO.EmployeeDAO;
import dee.models.Employee;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;


public class AccountviewController implements Initializable {

    @FXML
    private Label time;
    @FXML
    private Label user;
    @FXML
    private JFXButton out;
    @FXML
    private Button warehousecreatetip;
    @FXML
    private Button warehouseordertip;
    @FXML
    private Button productcreatetip;
    @FXML
    private Button stockcreatetip;
    @FXML
    private Button goodsonreviewtip;
    @FXML
    private Button creidtgoodsreviewtip;
    @FXML
    private Button creidtgoodsreturn;
    @FXML
    private VBox body;
    @FXML
    private VBox subbody;
    @FXML
    private Button salesreviewtip;
    @FXML
    private VBox custbody;

    EmployeeDAO employeeDAO;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employeeDAO = new EmployeeDAO();
        user.setText(LoginController.getUsername());
        DisplayStocks();
    }    

    @FXML
    private void logout(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        employeeDAO.RecordLoggedOut(user.getText(), displaydateandtime(), employeeDAO.getBranchID(MainviewController.getBranch()));
        loadwindow("/dee/views/Login.fxml", "DeeMax");
    }


    @FXML
    private void warehousecreate(ActionEvent event) {
        loadcontrol("/dee/views/warehousecreation.fxml", " Create Warehouse");
    }

    @FXML
    private void warehouseorder(ActionEvent event) {
        loadcontrol("/dee/views/warehouseorder.fxml", "Warehouse Order");
    }

    @FXML
    private void imports(ActionEvent event) {
        loadcontrol("/dee/views/import.fxml", "Import");
    }

    @FXML
    private void productcreate(ActionEvent event) {
        loadcontrol("/dee/views/Products.fxml", "Create Product");
    }

    @FXML
    private void stockcreate(ActionEvent event) {
        loadcontrol("/dee/views/storeStock.fxml", "Create Stock");
    }


    @FXML
    private void goodsonreview(ActionEvent event) {
        loadcontrol("/dee/views/GoodsReview.fxml", "Goods Review");
    }

    @FXML
    private void creidtgoodsreview(ActionEvent event) {
        loadcontrol("/dee/views/partnerReview.fxml", "Credit Goods Review");
    }

    @FXML
    private void creidtgoodsreturn(ActionEvent event) {
        loadcontrol("/dee/views/CreditGoodsReview.fxml", "Credit Goods Return");
        
    }
    
     private void loadwindow(String loc, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadcontrol(String fxml, String name ){
          try {
            VBox employeebody = FXMLLoader.load(getClass().getResource(fxml));
            body.getChildren().setAll(employeebody);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void Subcontrol(String fxml ){
          try {
            VBox employeebody = FXMLLoader.load(getClass().getResource(fxml));
            subbody.getChildren().setAll(employeebody);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void Cuscontrol(String fxml ){
          try {
            VBox employeebody = FXMLLoader.load(getClass().getResource(fxml));
            custbody.getChildren().setAll(employeebody);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void DisplayStocks(){
        Subcontrol("/dee/views/EmDisplayStock.fxml");
        Cuscontrol("/dee/views/Customer.fxml");
    }

    @FXML
    private void salesreview(ActionEvent event) {
        loadcontrol("/dee/views/SalesReview.fxml", "SalesReview");
      
    }
    
    private static String displaydateandtime(){
        DateTimeFormatter datetime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }
    
}
