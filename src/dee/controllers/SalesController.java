
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


public class SalesController implements Initializable {

    @FXML
    private JFXButton cashsales;
    @FXML
    private JFXButton creditsales;
    @FXML
    private JFXButton returnsales;
    @FXML
    private JFXButton expense;

    private double xOffset = 0;
    private double yOffset = 0;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp();
    }    

    @FXML
    private void opencashsales(ActionEvent event) {
        loadcontrol("/dee/views/newInvoice.fxml", "Create Cash Sales", cashsales);
    }

    @FXML
    private void opencreditsales(ActionEvent event) {
        loadcontrol("/dee/views/Partner.fxml", "Create Credit Sales", creditsales);
    }

    @FXML
    private void openreturnsales(ActionEvent event) {
        loadcontrol("/dee/views/NewGoodReturn.fxml", "Create Return Sales", returnsales);
    }
    
    @FXML
    private void openExpense(ActionEvent event) {
        loadcontrol("/dee/views/Expense.fxml", "Create Expense", expense);
    }
    
    private void setupHelp(){
        cashsales.setTooltip(new Tooltip("Cash Sales"));
        creditsales.setTooltip(new Tooltip("Credit Sales"));
        returnsales.setTooltip(new Tooltip("Return Sales"));
        expense.setTooltip(new Tooltip("Expense"));
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

    
    
    
    
}
