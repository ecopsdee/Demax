
package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class DeeMain extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("/dee/views/Login.fxml"));
       root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
       Scene scene = new Scene(root);
       stage.setTitle("Demax");
       stage.initStyle(StageStyle.UNDECORATED);
//       stage.setMaximized(true);
       stage.setScene(scene);
       stage.show();
    }
    
     public static void main(String[] args) {
          launch(args);
     }
    
}
