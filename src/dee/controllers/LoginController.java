
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.database.deeinventory;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;


public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private MaterialDesignIconView minimize;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXButton _loginaction;
    
    String picA = "/image/cosmoslogo.png";
    private EmployeeDAO employeeDAO;  private static String Username; private static String PrintLogo;  private String BranchTitle = "Nnewi Branch"; private deeinventory DbSet;
    private Executor exec;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       setHelp(); DbSet = new deeinventory();  employeeDAO = new EmployeeDAO(); setPrintLogo(picA); 
       exec = Executors.newCachedThreadPool(runnable -> {
           Thread t = new Thread(runnable);
           t.setDaemon(true);
           return t;
       }); 
    }    

    @FXML
    private void closeAction(MouseEvent event) {
        Alert out = new Alert(Alert.AlertType.CONFIRMATION);
        out.setContentText("Save all work before closing the application.\n Unexcepted shutdown might result in loss of data");
        Optional<ButtonType> result = out.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        
    }

    @FXML
    private void loginAction(ActionEvent event) {
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return DbSet.checkconnection();
            }
        };
        
        connect.setOnSucceeded(e ->{  
            try {
                if (connect.get() != true) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Could not conect to the Server");
                    alert.showAndWait();
                }else{
                    authenticate(event);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect);
    }
    
    @FXML
    private void minusAction(MouseEvent event) {
        Stage stage = (Stage) ((MaterialDesignIconView) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    
    private void resetFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
     
    private boolean validateInput() {
        String errorMessage = "";

        if (usernameField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Please enter credentials!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            errorLabel.setText(errorMessage);
            return false;
        }
    }
    
    private void authenticate(Event event){
        if (validateInput()) {
            String uname = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (employeeDAO.checkUser(uname, employeeDAO.getBranchID(BranchTitle))) {
                
                if (employeeDAO.checkPassword(uname, password, employeeDAO.getBranchID(BranchTitle))) {
                    
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    String type = employeeDAO.getemployeeType(uname, employeeDAO.getBranchID(BranchTitle)); 
                    setUsername(usernameField.getText());
                    System.out.println("Username is " + getUsername());
                    switch (type) {
                       case "Admin":
                           if (employeeDAO.getTime() > 0) {
                               employeeDAO.RecordLoggedIn(getUsername(), type, employeeDAO.getBranchID(BranchTitle));
                               employeeDAO.deductTime(employeeDAO.getTime() - 1);
                               loadwindow("/dee/views/Mainview.fxml", "Admin Panel");
                           }else{
                               Alert alert = new Alert(Alert.AlertType.INFORMATION);
                               alert.setContentText("License Expired");
                               alert.showAndWait();
                           } 
                        break;

                        case "Sales":
                            if (employeeDAO.getTime() > 0) {
                                employeeDAO.RecordLoggedIn(getUsername(), type, employeeDAO.getBranchID(BranchTitle));
                                loadwindow("/dee/views/Salesview.fxml", "Sales Panel");
                            }else{
                               Alert alert = new Alert(Alert.AlertType.INFORMATION);
                               alert.setContentText("License Expired");
                               alert.showAndWait();
                            }
                        break;
                        
                        case "Account Officer":
                            if (employeeDAO.getTime() > 0) {
                                employeeDAO.RecordLoggedIn(getUsername(), type, employeeDAO.getBranchID(BranchTitle));
                                loadwindow("/dee/views/Accountview.fxml", "Account View Panel");
                            }else{
                               Alert alert = new Alert(Alert.AlertType.INFORMATION);
                               alert.setContentText("License Expired");
                               alert.showAndWait();
                            }         
                        break;
                    }
                } else {
                    resetFields();
                    errorLabel.setText("Wrong Password!");
                }
            } else {
                resetFields();
                errorLabel.setText("User doesn't exist!");
            }
        }
    }
    
    private void loadwindow(String loc, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setHelp(){
        _loginaction.setTooltip(new Tooltip("Login to the System")); 
        usernameField.setTooltip( new Tooltip("Enter your Username"));
        passwordField.setTooltip( new Tooltip("Enter your Password"));
    }

    public static String getUsername() {
        return Username;
    }

    public static void setUsername(String Username) {
        LoginController.Username = Username;
    }

    public static String getPrintLogo() {
        return PrintLogo;
    }

    public static void setPrintLogo(String PrintLogo) {
        LoginController.PrintLogo = PrintLogo;
    }
   

}
