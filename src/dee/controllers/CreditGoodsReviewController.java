
package dee.controllers;

import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.models.Partner;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class CreditGoodsReviewController implements Initializable {

    @FXML
    private VBox invoicepane;
    @FXML
    private ComboBox<Integer> invoice;
    @FXML
    private JFXTextField custID;
    @FXML
    private TableView<Partner> Returntable;
    @FXML
    private TableColumn<Partner, Integer> idcol;
    @FXML
    private TableColumn<Partner, String> productcol;
    @FXML
    private TableColumn<Partner, Integer> qtycol;
    @FXML
    private Label totalqty;
    @FXML
    private MaterialDesignIconView close;

    private SimpleIntegerProperty index = new SimpleIntegerProperty();
    ObservableList<Partner> invoicelist = FXCollections.observableArrayList();
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initcol();
        fillinvoice();
    }    

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void getdetails(ActionEvent event) {
        if (invoice.getValue() != null) {
            loadinformation(invoice.getValue());
        }
    }

    @FXML
    private void print(ActionEvent event) {
    }

    @FXML
    private void Save(ActionEvent event) {
        
    }

    @FXML
    private void refresh(ActionEvent event) {
      Returntable.getItems().clear();
    }

    @FXML
    private void clear(ActionEvent event) { 
        if (!invoicelist.isEmpty()) {
            int i = index.get();  Integer qtysum = 0;
            if(i > -1){
                invoicelist.remove(i);
                Returntable.getSelectionModel().clearSelection();   
            }
            for (int j = 0; j < invoicelist.size(); j++) {
                qtysum += invoicelist.get(j).getNumberofgoods();
            }
            totalqty.setText(String.valueOf(qtysum));
        }  
    }
    
      private void initcol() {
        idcol.setCellValueFactory(new PropertyValueFactory("id"));
        productcol.setCellValueFactory(new PropertyValueFactory("stockname"));
        qtycol.setCellValueFactory(new PropertyValueFactory("numberofgoods"));
        Returntable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(invoicelist.indexOf(newvalue));
            System.out.println("OK, the index is " + invoicelist.indexOf(newvalue));
        });
        Returntable.setItems(invoicelist);
    }

    private void fillinvoice() {
       
    }

    private void emptyalldata() {
        invoice.getSelectionModel().clearSelection();
        custID.clear();
        fillinvoice();
    }
    
    private void loadinformation(Integer invoiceno){
        
    }

    
}
