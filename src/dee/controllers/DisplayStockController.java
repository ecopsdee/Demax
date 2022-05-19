
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.PriceDAO;
import dee.models.Product;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class DisplayStockController implements Initializable {

    @FXML
    private VBox displaybody;
    @FXML
    private TableView<Product> producttable;
    @FXML
    private TableColumn<Product, String> productname;
    @FXML
    private TableColumn<Product, Integer> quantitycol;
    @FXML
    private TableColumn<Product, Integer> pricecol;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXButton _reload;
    @FXML
    private JFXButton _print;
    
     
    ObservableList<Product> stocklist = FXCollections.observableArrayList();
    PriceDAO priceDAO;  private EmployeeDAO employeeDAO;
    private Executor exec;
    
  
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); priceDAO = new PriceDAO(); employeeDAO = new EmployeeDAO();
        initcol();
        exec = Executors.newCachedThreadPool(runnable -> {
           Thread t = new Thread(runnable);
           t.setDaemon(true);
           return t;
       });
    }    

    @FXML
    private void reload(ActionEvent event) {
        loadstock();
    }
    
    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }
    
    @FXML
    private void print(ActionEvent event) {
        print();
    }
    
    @FXML
    private void search(KeyEvent event) {
        FilteredList<Product> filteredData = new FilteredList<>(stocklist, e -> true);  
            searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Product>) stock -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return  true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase(); 
                    if (stock.getPrname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Product> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(producttable.comparatorProperty());
            producttable.setItems(filteredData);
    }
    
    private void setupHelp(){
        searchField.setTooltip(new Tooltip("Search"));
        _reload.setTooltip(new Tooltip("Display Stocks"));
        _print.setTooltip(new Tooltip("Print"));
    }

    private void initcol() {
        productname.setCellValueFactory(new PropertyValueFactory("prname"));
        quantitycol.setCellValueFactory(new PropertyValueFactory("id"));
        pricecol.setCellValueFactory(new PropertyValueFactory("prid"));
        producttable.setItems(stocklist);
    }
    
    private  void loadstock(){
        Task<ObservableList<Product>> connect = new Task<ObservableList<Product>>(){
            @Override
            protected ObservableList<Product> call() throws Exception {
                return priceDAO.getStock(employeeDAO.getBranchID(MainviewController.getBranch()));
                
            }
        };
         
        connect.setOnSucceeded(e ->{  
            try {
                stocklist = connect.get();
                producttable.setItems(stocklist);    
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
        
    }

    private  void print(){
        if (stocklist.isEmpty()) {
           Alert nodata = new Alert(Alert.AlertType.ERROR);
           nodata.setContentText("No data to Save");
           nodata.showAndWait();
        }else{
            
        }
    }
    
    private String displaydateandtime(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }

    

    
}
