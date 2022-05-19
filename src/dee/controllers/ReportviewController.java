
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


public class ReportviewController implements Initializable {

    @FXML
    private JFXButton warehouse;
    @FXML
    private JFXButton product;
    @FXML
    private JFXButton invoice;
    @FXML
    private JFXButton creditsales;
    @FXML
    private JFXButton logsession;
    @FXML
    private JFXButton branch;
    
    private double xOffset = 0;
    private double yOffset = 0;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp();
    }    

    @FXML
    private void openwarehouse(ActionEvent event) {
        loadcontrol("/dee/views/Warehousereport.fxml", "Warehouse report", warehouse);
    }

    @FXML
    private void openproduct(ActionEvent event) {
        loadcontrol("/dee/views/Productreport.fxml", "Product report", product);
    }

    @FXML
    private void addinvoice(ActionEvent event) {
        loadcontrol("/dee/views/Invoicereport.fxml", "Product report", invoice);
    }

    @FXML
    private void opencreditsales(ActionEvent event) {
        loadcontrol("/dee/views/Creditreport.fxml", "Product report", creditsales);
    }

    @FXML
    private void openlogsession(ActionEvent event) {
       
    }
    
    @FXML
    private void openbranch(ActionEvent event) {
        loadcontrol("/dee/views/Branchreport.fxml", "Product report", branch);
    }
    
    private void setupHelp(){
        warehouse.setTooltip(new Tooltip("Warehouse Report"));
        product.setTooltip(new Tooltip("Stock Report"));
        invoice.setTooltip(new Tooltip("Invoice Report"));
        creditsales.setTooltip(new Tooltip("Credit Sales Report"));
        logsession.setTooltip(new Tooltip("Log Report"));
        branch.setTooltip(new Tooltip("Branch Report"));
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
