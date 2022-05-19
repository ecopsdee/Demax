
package dee.controllers;

import dee.DAO.EmployeeDAO;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;


public class MainviewController implements Initializable {

    @FXML
    private Label user;
    @FXML
    private VBox body;
    @FXML
    private Label branch;
    @FXML
    private Label date;
    @FXML
    private Label dashboard;
    @FXML
    private Label users;
    @FXML
    private Label stock;
    @FXML
    private Label sales;
    @FXML
    private Label report;
    @FXML
    private Label reviews;
    @FXML
    private Label logout;
    @FXML
    private Label _branch;
    @FXML
    private Label store;
    
    private static String Branch;
    EmployeeDAO employeeDAO;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); employeeDAO = new EmployeeDAO();
        user.setText(LoginController.getUsername());  date.setText(displaydateandtime());
        setBranch(branch.getText());
    }  

    @FXML
    private void dashboard(MouseEvent event) {
        loadcontrol("/dee/views/Dashboard.fxml", "Dashboard");
    }

    @FXML
    private void users(MouseEvent event) {
        loadcontrol("/dee/views/Userview.fxml", "User");
    }

    @FXML
    private void restock(MouseEvent event) {
        loadcontrol("/dee/views/Stockview.fxml", "Stock");
    }
    
    @FXML
    private void openbranch(MouseEvent event) {
        loadcontrol("/dee/views/Branchview.fxml", "Sales");
    }

    @FXML
    private void sales(MouseEvent event) {
        loadcontrol("/dee/views/Sales.fxml", "Sales");
    }

    @FXML
    private void reports(MouseEvent event) {
        loadcontrol("/dee/views/Reportview.fxml", "Report");
    }

    @FXML
    private void review(MouseEvent event) {
        loadcontrol("/dee/views/Reviewview.fxml", "Review");
    }

    @FXML
    private void logout(MouseEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        employeeDAO.RecordLoggedOut(user.getText(), displaydateandtime(), employeeDAO.getBranchID(MainviewController.getBranch()));
        loadwindow("/dee/views/Login.fxml", "Cosmology");
    }
    
    private void setupHelp(){
        dashboard.setTooltip(new Tooltip("Dashboard"));
        user.setTooltip(new Tooltip("Login User"));
        stock.setTooltip(new Tooltip("Stock"));
        _branch.setTooltip(new Tooltip("Branch"));
        sales.setTooltip(new Tooltip("Sales"));
        report.setTooltip(new Tooltip("Report"));
        reviews.setTooltip(new Tooltip("Reviews"));
        logout.setTooltip(new Tooltip("Logout"));
        users.setTooltip(new Tooltip("Users"));
        
    }
    
    private void loadwindow(String loc, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
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

    public static String getBranch() {
        return Branch;
    }

    public static void setBranch(String Branch) {
        MainviewController.Branch = Branch;
    }

    
    
    
}
