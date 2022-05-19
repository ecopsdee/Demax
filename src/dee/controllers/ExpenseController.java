
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.ExpenseDAO;
import dee.models.Expense;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ExpenseController implements Initializable {

    @FXML
    private VBox invoicepane;
    @FXML
    private MaterialDesignIconView close1;
    @FXML
    private HBox custidbody;
    @FXML
    private JFXTextField description;
    @FXML
    private HBox custidbody1;
    @FXML
    private JFXTextField price;
    @FXML
    private TableView<Expense> invoicetable;
    @FXML
    private TableColumn<Expense, String> describecol;
    @FXML
    private TableColumn<Expense, Integer> pricecol;
    @FXML
    private Label totalprice;
    @FXML
    private JFXButton _add;
    @FXML
    private JFXButton _Save;
    @FXML
    private JFXButton _clear;
    
    private final SimpleIntegerProperty index = new SimpleIntegerProperty();
    ObservableList<Expense> expenselist = FXCollections.observableArrayList();
    Integer pricetotal = 0; ExpenseDAO expenseDAO; EmployeeDAO employeeDAO;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       setupHelp(); initcol(); expenseDAO = new ExpenseDAO(); employeeDAO = new EmployeeDAO();
    }    

    @FXML
    private void closeaction(MouseEvent event) {
        close1.getScene().getWindow().hide();
    }

    @FXML
    private void add(ActionEvent event) {
        if (validateA()) {
            expenselist.add(new Expense(description.getText(), Integer.parseInt(price.getText())));
            pricetotal += Integer.parseInt(price.getText());
            totalprice.setText(String.valueOf(pricetotal));
            emptydata();
        }else{
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setContentText("Check the data!");
            err.showAndWait();
        }
    }

    @FXML
    private void Save(ActionEvent event) {
        if (!expenselist.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you want to save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (expenseDAO.createExpenses(expenselist, LoginController.getUsername())) {
                    Alert ok = new Alert(Alert.AlertType.CONFIRMATION);
                    ok.setContentText("sent to server! ");
                    ok.showAndWait();
                    emptyalldata();
                }
            }
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        if (!expenselist.isEmpty()) {
            int i = index.get(); Integer pricesum = 0;
            if(i > -1){
                expenselist.remove(i);
                invoicetable.getSelectionModel().clearSelection();   
            }
            for (int j = 0; j < expenselist.size(); j++) {
                pricesum += expenselist.get(j).getPrice();
            }
            totalprice.setText(String.valueOf(pricesum));
        }
    }
    
    private Boolean validateA(){
        return !description.getText().isEmpty() && price.getText().matches("\\d+");
    }
    
    private void setupHelp(){
        description.setTooltip(new Tooltip("Enter the Expense Details"));
        price.setTooltip(new Tooltip("Enter the Expense Price"));
        _Save.setTooltip(new Tooltip("Save Expense"));
        _clear.setTooltip(new Tooltip("Clear Selected Item from the List"));
        _add.setTooltip(new Tooltip("Add Item to the List"));
    }
    
    private void emptydata() {
        description.clear();
        price.clear();
    }
    
    private void emptyalldata(){
        invoicetable.getItems().clear(); expenselist.clear(); totalprice.setText(""); pricetotal = 0;
    }
    
    private void initcol() {
        index.set(-1);
        describecol.setCellValueFactory(new PropertyValueFactory("describe"));
        pricecol.setCellValueFactory(new PropertyValueFactory("price"));
        invoicetable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(expenselist.indexOf(newvalue));
        });
        
        invoicetable.setItems(expenselist);
    }
    
}
