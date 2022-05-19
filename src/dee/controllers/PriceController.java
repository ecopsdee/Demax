
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.EmployeeDAO;
import dee.DAO.PriceDAO;
import dee.DAO.ProductDAO;
import dee.models.Invoice;
import dee.models.Price;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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


public class PriceController implements Initializable {

    @FXML
    private MaterialDesignIconView close;
    @FXML
    private TableView<Price> Itemtable;
    @FXML
    private TableColumn<Price, Integer> snitemcol;
    @FXML
    private TableColumn<Price, String> modelitemcol;
    @FXML
    private TableColumn<Price, Integer> priceitemcol;
    @FXML
    private TableColumn<Price, Integer> priceitemcol1;
    @FXML
    private JFXTextField costprice;
    @FXML
    private Label model;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXDatePicker dateA;
    @FXML
    private JFXDatePicker dateB;
    @FXML
    private Label totrevenue;
    @FXML
    private Label totprofit;
    @FXML
    private Label totloss;
    @FXML
    private TableView<Invoice> profitlosstable;
    @FXML
    private TableColumn<Invoice, String> datecol;
    @FXML
    private TableColumn<Invoice, String> productcol;
    @FXML
    private TableColumn<Invoice, Integer> costpricecol;
    @FXML
    private TableColumn<Invoice, Integer> unitpricecol;
    @FXML
    private TableColumn<Invoice, Integer> profitcol;
    @FXML
    private TableColumn<Invoice, Integer> losscol;
    @FXML
    private TableColumn<Invoice, Integer> qtycol;
    @FXML
    private TableColumn<Invoice, Integer> totalcol;
    @FXML
    private TableColumn<Invoice, Integer> totprofitcol;
    @FXML
    private TableColumn<Invoice, Integer> totlosscol;
    @FXML
    private JFXButton _handleRefesh;
    @FXML
    private JFXButton _updateprice;
    @FXML
    private JFXButton _getprofitlosssheet;
   
    
    ObservableList<Price> pricelist = FXCollections.observableArrayList();  ObservableList<Invoice> profitlosslist = FXCollections.observableArrayList();
    private IntegerProperty index = new SimpleIntegerProperty();
    private PriceDAO priceDAO;  private EmployeeDAO employeeDAO; private ProductDAO productDAO;
    private Executor exec;
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); priceDAO = new PriceDAO(); employeeDAO = new EmployeeDAO(); productDAO = new ProductDAO(); InitCol();
        exec = Executors.newCachedThreadPool(runnable -> {
           Thread t = new Thread(runnable);
           t.setDaemon(true);
           return t;
       });
    }    

    @FXML
    private void closeAction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void handleRefesh(ActionEvent event) {
        refresh();
    }

    @FXML
    private void updateprice(ActionEvent event) {
        if (model.getText().isEmpty() || price.getText().isEmpty()) {
            
        }else if ( price.getText().matches("\\d+") && costprice.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (updatePrice(productDAO.getStockID(model.getText(), employeeDAO.getBranchID(MainviewController.getBranch())), Integer.parseInt(price.getText()),Integer.parseInt(costprice.getText()), 1, employeeDAO.getBranchID(MainviewController.getBranch()))) {
                    Alert update = new Alert(Alert.AlertType.INFORMATION);
                    update.setContentText("Update complete! ");
                    update.showAndWait();
                    EmptyFields();
                }else{
                    Alert update = new Alert(Alert.AlertType.ERROR);
                    update.setContentText("Error occured! please try again ");
                    update.showAndWait();
                
                }
            }  
        }
    }
    
    @FXML
    private void getprofitlosssheet(ActionEvent event) {
        clear();
        if(dateA.getValue() != null && dateB.getValue() != null){
            getProfitaandLoss(dateA.getValue().toString(), dateB.getValue().toString(), employeeDAO.getBranchID(MainviewController.getBranch()));
            
        }else if (dateA.getValue() != null && dateB.getValue() == null) {
            getProfitaandLoss(dateA.getValue().toString(), employeeDAO.getBranchID(MainviewController.getBranch()));
            getTotals(profitlosslist);
        }
    }
    
    private void getProfitaandLoss(String date, Integer brID){
        Task<ObservableList<Invoice>> connect = new Task<ObservableList<Invoice>>(){
            @Override
            protected ObservableList<Invoice> call() throws Exception {
                return priceDAO.getProfitandLossSheet(date, brID);
            }
        };
        connect.setOnSucceeded(e ->{  
            try {
                profitlosslist = connect.get();
                profitlosstable.setItems(profitlosslist);  
                getTotals(profitlosslist);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }
    
    private void getProfitaandLoss(String date,String date2 ,Integer brID){
        Task<ObservableList<Invoice>> connect = new Task<ObservableList<Invoice>>(){
            @Override
            protected ObservableList<Invoice> call() throws Exception {
                return priceDAO.getProfitandLossSheet(date, date2, brID);
            }
        };
        connect.setOnSucceeded(e ->{  
            try {
                profitlosslist = connect.get();
                profitlosstable.setItems(profitlosslist); 
                getTotals(profitlosslist);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        exec.execute(connect); 
    }
    
    private void getTotals(ObservableList<Invoice> item){
        Integer Totalrev = 0; Integer totProfit = 0; Integer loss = 0;
        for (Invoice invoice : item) {
            Totalrev += invoice.getTotalprice();
            totProfit += invoice.getTotalprofit();
            loss += invoice.getTotalloss();
            totrevenue.setText(String.valueOf(Totalrev));
            totprofit.setText(String.valueOf(totProfit));
            totloss.setText(String.valueOf(loss));
        }
        
    }
    
    private void clear(){
        totrevenue.setText("");
        totprofit.setText("");
        totloss.setText("");
    }
    
    private void EmptyFields(){
        model.setText("");
        price.clear();
        costprice.clear();
    }
    
    private void refresh(){
        Task<ObservableList<Price>> connect = new Task<ObservableList<Price>>(){
            @Override
            protected ObservableList<Price> call() throws Exception {
                return priceDAO.getPrice(employeeDAO.getBranchID(MainviewController.getBranch()));
            }
        };
        connect.setOnSucceeded(e ->{  
            try {
                pricelist = connect.get();
                Itemtable.setItems(pricelist);  
                
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        exec.execute(connect); 
    }
    
    private void InitCol(){
        index.set(-1);
        snitemcol.setCellValueFactory(new PropertyValueFactory("id"));
        modelitemcol.setCellValueFactory(new PropertyValueFactory("name"));
        priceitemcol.setCellValueFactory(new PropertyValueFactory("price"));
        priceitemcol1.setCellValueFactory(new PropertyValueFactory("costprice"));
        Itemtable.setItems(pricelist);
        Itemtable.getSelectionModel().selectedIndexProperty().addListener(new UpdatePriceListener());
        Itemtable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<?> observable, Object oldvalue, Object newvalue) -> {
            index.set(pricelist.indexOf(newvalue));
            System.out.println("OK, the index is " + pricelist.indexOf(newvalue));
        });
        
        datecol.setCellValueFactory(new PropertyValueFactory("date"));
        productcol.setCellValueFactory(new PropertyValueFactory("productname"));
        costpricecol.setCellValueFactory(new PropertyValueFactory("id"));
        unitpricecol.setCellValueFactory(new PropertyValueFactory("unitprice"));
        profitcol.setCellValueFactory(new PropertyValueFactory("profit"));
        losscol.setCellValueFactory(new PropertyValueFactory("loss"));
        qtycol.setCellValueFactory(new PropertyValueFactory("quantity"));
        totalcol.setCellValueFactory(new PropertyValueFactory("totalprice"));
        totprofitcol.setCellValueFactory(new PropertyValueFactory("totalprofit"));
        totlosscol.setCellValueFactory(new PropertyValueFactory("totalloss"));
        profitlosstable.setItems(profitlosslist);
        

        
    }
    
    private void setupHelp(){
        price.setTooltip(new Tooltip("Unit Price"));
        costprice.setTooltip(new Tooltip("Cost Price"));
        _updateprice.setTooltip(new Tooltip("Update Price"));
        _handleRefesh.setTooltip(new Tooltip("Display Price List"));
        dateA.setTooltip(new Tooltip("Select Starting Date"));
        dateB.setTooltip(new Tooltip("Select Ending Date"));
        _getprofitlosssheet.setTooltip(new Tooltip("Display Profit & Loss Details"));
    }

    private class UpdatePriceListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
            int ix = newVal.intValue();
            if ((ix < 0) || (ix >= pricelist.size())) {
                return; // invalid data
            }
            Price PRICE = pricelist.get(ix);
            price.setText(String.valueOf(PRICE.getPrice()));
            costprice.setText(String.valueOf(PRICE.getCostprice()));
            model.setText(PRICE.getName());
        }
    }

    private Boolean updatePrice(Integer stID, Integer price, Integer costprice, Integer count, Integer brID){
        Boolean status = false;
        if ( priceDAO.UpdateStockPrice(stID, price, costprice, count, brID)) {
            status = true;
        }
        return status;  
    }
    
    
    
}
