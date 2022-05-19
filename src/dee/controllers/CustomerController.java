
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.CustomerDAO;
import dee.DAO.EmployeeDAO;
import dee.models.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class CustomerController implements Initializable {

    @FXML
    private TableView<Customer> producttable;
    @FXML
    private TableColumn<Customer, Integer> custID;
    @FXML
    private TableColumn<Customer, String> namecol;
    @FXML
    private TableColumn<Customer, String> locationcol;
    @FXML
    private TableColumn<Customer, String> identitycol;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXButton _reload;
    @FXML
    private JFXButton _print;
    
    private static Integer custid;
    private static String name;
    private static String location;

    ObservableList<Customer> customerlist = FXCollections.observableArrayList();
    private CustomerDAO customerDAO; private EmployeeDAO employeeDAO;
    private Executor exec;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); initcol(); customerDAO = new CustomerDAO(); employeeDAO = new EmployeeDAO();
        exec = Executors.newCachedThreadPool(runnable -> {
           Thread t = new Thread(runnable);
           t.setDaemon(true);
           return t;
       });
    }    

    @FXML
    private void reload(ActionEvent event) {
        ViewCustomer(employeeDAO.getBranchID(MainviewController.getBranch()));
    }

    @FXML
    private void print(ActionEvent event) {
    }

    private void initcol() {
      custID.setCellValueFactory(new PropertyValueFactory("custid"));
      namecol.setCellValueFactory(new PropertyValueFactory("custfname"));
      locationcol.setCellValueFactory(new PropertyValueFactory("custorigin"));
      identitycol.setCellValueFactory(new PropertyValueFactory("custstatus"));
      producttable.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());
      producttable.setItems(customerlist);
    }

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void search(KeyEvent event) {
        FilteredList<Customer> filteredData = new FilteredList<>(customerlist, e -> true);  
            searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Customer>) stock -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return  true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase(); 
                    if (stock.getCustfname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (stock.getCustorigin().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    if (stock.getCuststatus().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Customer> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(producttable.comparatorProperty());
            producttable.setItems(filteredData);
        
    }
    
    private class RowSelectChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov, 
            Number oldVal, Number newVal) {
		int ix = newVal.intValue();
		if ((ix < 0) || (ix >= customerlist.size())) {
                    return; // invalid data
		}
		Customer custom = customerlist.get(ix);
                setCustid(custom.getCustid());
                setName(custom.getCustfname());
                setLocation(custom.getCustorigin());     
	    }
    }
    
    private void setupHelp(){
        searchField.setTooltip(new Tooltip("Search"));
        _reload.setTooltip(new Tooltip("Display Customers"));
        _print.setTooltip(new Tooltip("Print"));
    }
    
    private void ViewCustomer(Integer brID){
        customerlist.clear();
        Task<ObservableList<Customer>> connect = new Task<ObservableList<Customer>>(){
            @Override
            protected ObservableList<Customer> call() throws Exception {
                return customerDAO.getCustomer(brID);
            }
        };
        connect.setOnSucceeded(e ->{  
            try {
                customerlist = connect.get();
                producttable.setItems(customerlist);    
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }
    
    public static Integer getCustid() {
        return custid;
    }

    public static void setCustid(Integer custid) {
        CustomerController.custid = custid;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        CustomerController.name = name;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        CustomerController.location = location;
    }
    
}
