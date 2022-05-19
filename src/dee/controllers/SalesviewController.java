
package dee.controllers;

import dee.DAO.EmployeeDAO;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;


public class SalesviewController implements Initializable {

    @FXML
    private Label user;
    @FXML
    private VBox body;
    @FXML
    private Label branch;
    @FXML
    private Label date;
    
    EmployeeDAO employeeDAO;
    @FXML
    private HBox usera;
    @FXML
    private HBox dashboard;
    @FXML
    private HBox sales;
    @FXML
    private HBox logout;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employeeDAO = new EmployeeDAO();
        user.setText(LoginController.getUsername());
    }    

    @FXML
    private void dashboard(MouseEvent event) {
        loadcontrol("/dee/views/Dashboard.fxml", "Dashboard");
    }

    @FXML
    private void sales(MouseEvent event) {
        loadcontrol("/dee/views/Sales.fxml", "Sales");
    }

    @FXML
    private void logout(MouseEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        employeeDAO.RecordLoggedOut(user.getText(), displaydateandtime(), employeeDAO.getBranchID(MainviewController.getBranch()));
        loadwindow("/dee/views/Login.fxml", "DeeMax");
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

    private static String displaydateandtime(){
        DateTimeFormatter datetime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }

  
    
}
