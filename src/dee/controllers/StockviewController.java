
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class StockviewController implements Initializable {

    @FXML
    private JFXButton addwarehouse;
    @FXML
    private JFXButton addproduct;
    @FXML
    private JFXButton productprice;
    @FXML
    private JFXButton addimports;
    @FXML
    private JFXButton addwarehouseorder;

    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openwarehouse(ActionEvent event) {
        loadcontrol("/dee/views/warehousecreation.fxml", "Create Warehouse", addwarehouse);
    }

    @FXML
    private void openproduct(ActionEvent event) {
        loadcontrol("/dee/views/Products.fxml", "Create Product", addproduct);
    }

    @FXML
    private void addprice(ActionEvent event) {
        loadcontrol("/dee/views/Price.fxml", "Stock", productprice);
    }

    @FXML
    private void imports(ActionEvent event) {
        loadcontrol("/dee/views/import.fxml", "Create Import", addimports);
    }

    @FXML
    private void warehouseorder(ActionEvent event) {
        loadcontrol("/dee/views/warehouseorder.fxml", "Create Warehouse Order", addwarehouseorder);
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
