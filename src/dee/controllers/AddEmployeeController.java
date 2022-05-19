
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.models.Employee;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class AddEmployeeController implements Initializable {

    @FXML
    private TextField firstField;
    @FXML
    private TextArea addressArea;
    @FXML
    private TextField lastField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField phoneField;
    @FXML
    private JFXComboBox<String> typeField;
    @FXML
    private VBox updateformbody;
    @FXML
    private TextField upfname;
    @FXML
    private TextField uplname;
    @FXML
    private TextField upuname;
    @FXML
    private TextField uppass;
    @FXML
    private TextField upphone;
    @FXML
    private JFXTextField uptype;
    @FXML
    private TextField upaddress;
    @FXML
    private TableView<Employee> employeetable;
    @FXML
    private TableColumn<Employee, Integer> sncol;
    @FXML
    private TableColumn<Employee, String> fnamecol;
    @FXML
    private TableColumn<Employee, String> lnamecol;
    @FXML
    private TableColumn<Employee, String> unamecol;
    @FXML
    private TableColumn<Employee, String> passcol;
    @FXML
    private TableColumn<Employee, String> phonecol;
    @FXML
    private TableColumn<Employee, String> typecol;
    @FXML
    private TableColumn<Employee, String> addcol;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private VBox embody1;
    @FXML
    private JFXButton _savebutton;
    @FXML
    private JFXButton _cancelbutton;
    @FXML
    private JFXButton _delete;
    @FXML
    private JFXButton _reload;
    @FXML
    private JFXComboBox<String> storeField;
    
    ObservableList<Employee> emlist = FXCollections.observableArrayList();
    ObservableList<String> emtype = FXCollections.observableArrayList();
    EmployeeDAO employeedao; Employee employee;
    
    
    
    
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       sethelp(); employeedao = new EmployeeDAO();
       fillcombox();
       initcol();
    }    

    @FXML
    private void handleSave(ActionEvent event) {
        if( validateA() ){
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Review before sending to the server.", ButtonType.OK);
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.get() == ButtonType.OK) {  
                if (Save(firstField.getText(), lastField.getText(), usernameField.getText(), passwordField.getText(), phoneField.getText(), typeField.getValue(),  addressArea.getText(), employeedao.getBranchID(MainviewController.getBranch()))) {
                    Alert ok = new  Alert(Alert.AlertType.INFORMATION);
                    ok.setContentText("Sent to server!");
                    ok.showAndWait();
                    Emptyfields();
                }   
            }       
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error! \nCheck and try again!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Emptyfields();
    }
    
    @FXML
    private void deleteEmployee(ActionEvent event) {
        if (validateB()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Remove Employee");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (employeedao.deleteEmployee(upuname.getText(), employeedao.getBranchID(MainviewController.getBranch()))) {
                    EmptyFields();
                }
            }
        }
    }

    @FXML
    private void reload(ActionEvent event) {
        initcol();
    }
    
    @FXML
    private void closeAction(MouseEvent event) {
       close.getScene().getWindow().hide();
    }
    
    private Boolean Save(String fieldfirst, String fieldlast, String fieldusername, String fielspassword, String fieldphone, String fieldtype, String areaaddress, Integer brID){  
        return employeedao.createEmployee(new Employee(fieldfirst, fieldlast, fieldusername, fielspassword, fieldphone, areaaddress, fieldtype), brID);
    }
    
    private Boolean validateA(){
        return !firstField.getText().isEmpty() && !lastField.getText().isEmpty() && !usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() && !phoneField.getText().isEmpty() && !typeField.getValue().isEmpty() && !addressArea.getText().isEmpty();
    }
    
    private Boolean validateB(){
        return !upfname.getText().isEmpty() && !uplname.getText().isEmpty() && !upuname.getText().isEmpty() && !uppass.getText().isEmpty() && !upphone.getText().isEmpty() && !uptype.getText().isEmpty() && !upaddress.getText().isEmpty();
    }
    
    private void sethelp(){
        firstField.setTooltip(new Tooltip("Enter the First Name"));
        lastField.setTooltip(new Tooltip("Enter the Last Name"));
        usernameField.setTooltip(new Tooltip("Enter the Username"));
        passwordField.setTooltip(new Tooltip("Enter the Password"));
        phoneField.setTooltip(new Tooltip("Enter the Phone Number"));
        typeField.setTooltip(new Tooltip("Select the User Type   "));
        addressArea.setTooltip(new Tooltip("Enter the Address"));
        _savebutton.setTooltip(new Tooltip("Save the User Details"));
        _cancelbutton.setTooltip(new Tooltip("Reset and Enter Again"));
        _delete.setTooltip(new Tooltip("Delete a User"));
        _reload.setTooltip(new Tooltip("Show all registered users of the System"));       
    }

    private void fillcombox() {
        //create a view to allow admin, create the roles of the accout, as well as 
        emtype.add(0, "Admin");
        emtype.add(1, "Sales");
        emtype.add(2, "Cashier");
        emtype.add(3, "Account Officer");
        typeField.setItems(emtype);
                
    }
    
    private void Emptyfields(){
        firstField.clear();
        lastField.clear();;
        usernameField.clear();
        passwordField.clear();
        phoneField.clear();
        typeField.setValue(null);
        addressArea.clear();
    }
    
    private void EmptyFields(){
        upfname.setText("");
        uplname.setText("");
        upuname.setText("");
        uppass.setText("");
        upphone.setText("");
        upaddress.setText("");
        uptype.setText("");
    }

    private void initcol() {
        employeetable.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());
        sncol.setCellValueFactory(new PropertyValueFactory("id"));
        fnamecol.setCellValueFactory(new PropertyValueFactory("Fname"));
        lnamecol.setCellValueFactory(new PropertyValueFactory("Lname"));
        unamecol.setCellValueFactory(new PropertyValueFactory("Uname"));
        passcol.setCellValueFactory(new PropertyValueFactory("Pass"));
        phonecol.setCellValueFactory(new PropertyValueFactory("Fone"));
        addcol.setCellValueFactory(new PropertyValueFactory("Addres"));
        typecol.setCellValueFactory(new PropertyValueFactory("emtype"));
       
        emlist = employeedao.getEmployees(employeedao.getBranchID(MainviewController.getBranch()));
        employeetable.setItems(emlist);

    }
    
    private class RowSelectChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
            int ix = newVal.intValue();
            if ((ix < 0) || (ix >= emlist.size())) {
                return; // invalid data
            }
            Employee employee = emlist.get(ix);
            upfname.setText(employee.getFname());
            uplname.setText(employee.getLname());
            upuname.setText(employee.getUname());
            uppass.setText(employee.getPass());
            upphone.setText(employee.getFone());
            upaddress.setText(employee.getAddres());
            uptype.setText(employee.getEmtype());
        }
    }
    
}
