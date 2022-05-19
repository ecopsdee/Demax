
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.WarehouseDAO;
import dee.models.Warehouse;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class WarehousecreationController implements Initializable {

    @FXML
    private JFXTextField Wname;
    @FXML
    private JFXTextField Wstate;
    @FXML
    private JFXTextField Wdescribe;
    @FXML
    private TableView<Warehouse> mywarehouse;
    @FXML
    private TableColumn<Warehouse, Integer> idcol;
    @FXML
    private TableColumn<Warehouse, String> Wnamecol;
    @FXML
    private TableColumn<Warehouse, String> Wstatecol;
    @FXML
    private TableColumn<Warehouse, String> Wdescribecol;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXButton _add;
    @FXML
    private JFXButton _clear;
    @FXML
    private JFXButton _save;
    
    ObservableList<Warehouse> list = FXCollections.observableArrayList();
    private final IntegerProperty index = new SimpleIntegerProperty();
    Integer Count = 0;
    private WarehouseDAO warehouseDAO; private EmployeeDAO employeeDAO;
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      warehouseDAO = new WarehouseDAO();  employeeDAO = new EmployeeDAO();
      initcol();
    }    


    @FXML
    private void add(ActionEvent event) {
        try {
            Integer gen_ID = 0;
            do {                
                gen_ID = generateID();
            } while (warehouseDAO.checkWarehouseId(gen_ID, employeeDAO.getBranchID(MainviewController.getBranch())));
            
            if( !Wname.getText().isEmpty() && !Wstate.getText().isEmpty()  && !Wdescribe.getText().isEmpty()){
                Count++;
                list.add(new Warehouse(gen_ID, captitalizeWord(Wname.getText().toLowerCase()), "", captitalizeWord(Wstate.getText().toLowerCase()), Wdescribe.getText()));
                emptydata();
                
            }else{             
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Eror!");
                alert.showAndWait();
            }      
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.showAndWait();
        }
        
    }


    @FXML
    private void save(ActionEvent event) {
        if (!list.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setHeaderText("Save Warehouse");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                list = mywarehouse.getItems();
                if (warehouseDAO.createWarehouse(list, 0, employeeDAO.getBranchID(MainviewController.getBranch()))) {
                    Alert nosave = new Alert(Alert.AlertType.INFORMATION);
                    nosave.setContentText("Save complete!");
                    nosave.showAndWait();
                    mywarehouse.getItems().clear(); 
                }else{
                    Alert nosave = new Alert(Alert.AlertType.ERROR);
                    nosave.setContentText("An error occurred, please try again!");
                    nosave.showAndWait();
                }             
            }
        }else{
            Alert nosave = new Alert(Alert.AlertType.INFORMATION);
            nosave.setContentText("Nothing to save");
            nosave.showAndWait();
        }
       
    }
    
    @FXML
    private void clear(ActionEvent event) {
        if (list.isEmpty()) {
            
        }else{
            int i = index.get();
            if(i > -1){
                list.remove(i);
                mywarehouse.getSelectionModel().clearSelection();   
                Count -= 1;
            }
        }  
    }
    
    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    private void refresh(ActionEvent event) {
        mywarehouse.getItems().clear();
    }

    private void initcol() {
       index.set(-1);
       idcol.setCellValueFactory(new PropertyValueFactory("idcol"));
       Wnamecol.setCellValueFactory(new PropertyValueFactory("wnamecol"));
       Wstatecol.setCellValueFactory(new PropertyValueFactory("wcitycol"));
       Wdescribecol.setCellValueFactory(new PropertyValueFactory("wdescribecol"));
       mywarehouse.setItems(list);
       mywarehouse.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(list.indexOf(newvalue));
            System.out.println("OK, the index is " + list.indexOf(newvalue));
        });
    }
    
    private void emptydata() {
       Wname.clear();
       Wstate.clear();
       Wdescribe.clear();
    }

    private String captitalizeWord(String word){
        StringBuffer s = new StringBuffer();
        char ch = ' ';
        for (int i = 0; i < word.length(); i++) {
            if (ch == ' ' && word.charAt(i) != ' ') {
                s.append(Character.toUpperCase(word.charAt(i)));
            }else{
                s.append(word.charAt(i));
            }
            ch = word.charAt(i);
        }
        
        return s.toString().trim();
    }

    private Integer generateID(){
        int number = 1 + (int)(Math.random() * 1000);
        return  number;
    }
    

    
  
}
