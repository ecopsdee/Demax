
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


public class ReviewviewController implements Initializable {

    @FXML
    private JFXButton cashreview;
    @FXML
    private JFXButton creditsalesreview;
    @FXML
    private JFXButton cashsalesreturn;
    @FXML
    private JFXButton others;
    
    private double xOffset = 0;
    private double yOffset = 0;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp();
    }    
    

    @FXML
    private void opencashreview(ActionEvent event) {
        loadcontrol("/dee/views/SalesReview.fxml", "Sales Review", cashreview);
    }

    @FXML
    private void opencreditsalesreview(ActionEvent event) {
        loadcontrol("/dee/views/partnerReview.fxml", "Credit Review", creditsalesreview);
    }

    @FXML
    private void opencashsalesreturn(ActionEvent event) {
        loadcontrol("/dee/views/GoodsReview.fxml", "Sales Returm", cashsalesreturn);
    }
    
    @FXML
    private void openothers(ActionEvent event){
    }
    
    private void setupHelp(){
        cashreview.setTooltip(new Tooltip("Cash Sales Review"));
        creditsalesreview.setTooltip(new Tooltip("Credit Sales Review"));
        cashsalesreturn.setTooltip(new Tooltip("Return Sales"));
        others.setTooltip(new Tooltip("Others"));
      
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
