
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


public class DashboardController implements Initializable {

    @FXML
    private JFXButton activity;
    @FXML
    private JFXButton viewproduct;
    @FXML
    private JFXButton calculator;

    private double xOffset = 0;
    private double yOffset = 0;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp();
    }    

    @FXML
    private void openactivity(ActionEvent event) {
        loadcontrol("/dee/views/ActivityReport.fxml", "Activity Report", activity);
    }

    @FXML
    private void openviewproduct(ActionEvent event) {
        loadcontrol("/dee/views/DisplayStock.fxml", "Stock", viewproduct);
    }

    @FXML
    private void opencalculator(ActionEvent event) {
        loadcontrol("/dee/views/Customer.fxml", "Customer", calculator);
    }
    
    private void setupHelp(){
        activity.setTooltip(new Tooltip("Activity"));
        viewproduct.setTooltip(new Tooltip("Product"));
        calculator.setTooltip(new Tooltip("Customer"));
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
