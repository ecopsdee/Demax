
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class BranchviewController implements Initializable {

    @FXML
    private JFXButton createBranch;
    @FXML
    private JFXButton branchsales;
    @FXML
    private JFXButton branchreport;
    @FXML
    private JFXButton branchexpense;
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp();
    }    

    @FXML
    private void opencreatebranch(ActionEvent event) {
        loadcontrol("/dee/views/Branch.fxml", "Branch", createBranch);
    }
    
    @FXML
    private void openbranchsales(ActionEvent event) {
        loadcontrol("/dee/views/BranchSales.fxml", "Branch Sales", branchsales);
    }
    
    @FXML
    private void openbranchreport(ActionEvent event) {
        loadcontrol("/dee/views/BranchRepor.fxml", "Branch report", branchreport);
    }
    
    @FXML
    private void openbranchexpense(ActionEvent event) {
        loadcontrol("/dee/views/BranchExpense.fxml", "Branch Expense", branchexpense);
    }
    
    private void loadcontrol(String fxml, String name, JFXButton OPEN ){
        try {
            Parent employeebody = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(employeebody);
            employeebody.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            
            Stage stage = new Stage(StageStyle.UNDECORATED);
            employeebody.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            stage.setTitle(name);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(OPEN.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setupHelp(){
        createBranch.setTooltip(new Tooltip("Create Branch"));
        branchsales.setTooltip(new Tooltip("Branch Sales"));
        branchexpense.setTooltip(new Tooltip("Branch Expenses"));
        branchreport.setTooltip(new Tooltip("Branch Report"));
        
        
    }

  

   
    
}
