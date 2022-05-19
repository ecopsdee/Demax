
package dee.controllers;

import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class RunningController implements Initializable {
 @FXML
    private JFXProgressBar progress;
    
    @FXML
    private VBox clos;
    Thread thread;
    private double xOffset = 0;
    private double yOffset = 0;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progress.setProgress(0.0);
        Thread thread = new Thread(task);
        thread.start();   
    }    
    
    private void loadwindow(String loc, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            parent.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            parent.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
        });
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
    }
   
    private void closed(Node nor) {
        Stage stage = (Stage)nor.getScene().getWindow();
        stage.close();
    }
      
    Task<Void> task = new Task<Void>(){

        @Override
        protected Void call() throws Exception  {
             for (int i = 0; i < 100; i++) {
                 final int counter = i;
                 Platform.runLater(()->{
                     progress.setProgress(counter / 100.0 );
                 });
                  Thread.sleep(100);
            }
            return null;
        }
        
        @Override
        protected  void succeeded(){
            closed(clos);      
            loadwindow("/dee/views/Login.fxml", "DeMak Inventory");     // this is the method that opens the login window
            super.succeeded();
        }
        
         @Override
        protected void failed() {
            Platform.runLater(()->{
                new Alert(Alert.AlertType.ERROR, "Thread Failed!").show();
            });
            super.failed();
        }
     }; 
      
    
}
