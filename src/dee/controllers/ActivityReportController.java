
package dee.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import dee.DAO.ActivityDAO;
import dee.DAO.BranchDAO;
import dee.DAO.EmployeeDAO;
import dee.DAO.ReportDAO;
import dee.models.Activity;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
import javafx.scene.layout.VBox;


public class ActivityReportController implements Initializable {

    @FXML
    private VBox displaybody;
    @FXML
    private MaterialDesignIconView close;
    @FXML
    private Label totalcollected;
    @FXML
    private Label totalexpense;
    @FXML
    private Label totalbalance;
    @FXML
    private JFXButton search;
    @FXML
    private TableView<Activity> producttable;
    @FXML
    private TableColumn<Activity, String> temcol;
    @FXML
    private TableColumn<Activity, Integer> totalcol;
    @FXML
    private TableColumn<Activity, Integer> expensecol;
    @FXML
    private TableColumn<Activity, Integer> balancecol;
    @FXML
    private JFXDatePicker dateA;

    ObservableList<Activity> activelist = FXCollections.observableArrayList();
    ActivityDAO activityDAO; BranchDAO branchDAO; EmployeeDAO employeeDAO; Executor exec;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupHelp(); initcol(); activityDAO = new ActivityDAO(); branchDAO = new BranchDAO(); employeeDAO = new EmployeeDAO();
        exec = Executors.newCachedThreadPool(runnable -> {
           Thread t = new Thread(runnable);
           t.setDaemon(true);
           return t;
       });
    }    

    @FXML
    private void closeaction(MouseEvent event) {
        close.getScene().getWindow().hide();
    }

    @FXML
    private void searchdata(ActionEvent event) {
        activelist = itemlist(activityDAO.getActivityAmount(dateA.getValue().toString()), activityDAO.getActivityExpense(dateA.getValue().toString()), employeeDAO.getBranchID(MainviewController.getBranch()));
        if (!activelist.isEmpty()) {
            producttable.setItems(activelist);
            totalcollected.setText(TotalAmount(activelist).toString());
            totalexpense.setText(TotalExpense(activelist).toString());
            totalbalance.setText(TotalBalance(activelist).toString());
        }  
    }

    @FXML
    private void print(ActionEvent event) {
        if (!activelist.isEmpty() && dateA.getValue() != null) {
            Alert print = new Alert(Alert.AlertType.CONFIRMATION);
            print.setContentText("Do you want to generate the print out?");
            Optional<ButtonType> result = print.showAndWait();
            if (result.get() == ButtonType.OK) {
                printActivity(dateA.getValue().toString(), StringConvert(totalcollected.getText()), StringConvert(totalexpense.getText()), StringConvert(totalbalance.getText()));
            }
        }
    }
    
    private ObservableList<Activity> itemlist(ObservableList<Activity> amountlist, ObservableList<Activity> expenselist, Integer brID){
        ObservableList<Activity> item = FXCollections.observableArrayList();
        if (!amountlist.isEmpty() ) {
            for (Activity _item : amountlist) {
                if (!item.isEmpty()) {
                    if (checkID(_item.getId(), item)) {
                        item.set(Index(_item.getId(), item), new Activity(_item.getId(), branchDAO.getBranchName(_item.getId(), brID), Total(LastAmount(Index(_item.getId(), item), item), _item.getAmount()), 0, Balance(Total(LastAmount(Index(_item.getId(), item), item), _item.getAmount()) , 0) ));                      
                    }else{
                        item.add(new Activity(_item.getId(), branchDAO.getBranchName(_item.getId(), brID), _item.getAmount(), _item.getExpense(), Balance(Total(0, _item.getAmount()), 0)));
                    }   
                }else{
                    item.add(new Activity(_item.getId(), branchDAO.getBranchName(_item.getId(), brID), _item.getAmount(), _item.getExpense(), Balance(Total(0, _item.getAmount()), 0)));
                }
            }
            if (!expenselist.isEmpty()) {
                for (Activity _expense : expenselist) {
                    if (!item.isEmpty()) {
                        if (checkID(_expense.getId(), item)) {
                            item.set(Index(_expense.getId(), item), new Activity(_expense.getId(), branchDAO.getBranchName(_expense.getId(), brID), Total(LastAmount(Index(_expense.getId(), item), item), _expense.getAmount()), Total(LastExpense(Index(_expense.getId(), item), item), _expense.getExpense()) , Balance(Total(LastAmount(Index(_expense.getId(), item), item), _expense.getAmount()) , Total(LastExpense(Index(_expense.getId(), item), item), _expense.getExpense())) ));                      
                        }else{
                            item.add(new Activity(_expense.getId(), branchDAO.getBranchName(_expense.getId(), brID), _expense.getAmount(), _expense.getExpense() , Balance(Total(0, 0), _expense.getExpense()) ));
                        }   
                    }else{
                        item.add(new Activity(_expense.getId(), branchDAO.getBranchName(_expense.getId(), brID), _expense.getAmount() , _expense.getExpense(), Balance(Total(0, 0), _expense.getExpense()) ));
                    }
                }
 
            }
            
        }else{
           if (!expenselist.isEmpty()) {
                for (Activity _expense : expenselist) {
                    if (!item.isEmpty()) {
                        if (checkID(_expense.getId(), item)) {
                            item.set(Index(_expense.getId(), item), new Activity(_expense.getId(), branchDAO.getBranchName(_expense.getId(), brID), Total(LastAmount(Index(_expense.getId(), item), item), _expense.getAmount()), Total(LastExpense(Index(_expense.getId(), item), item), _expense.getExpense()) , Balance(Total(LastAmount(Index(_expense.getId(), item), item), _expense.getAmount()) , Total(LastExpense(Index(_expense.getId(), item), item), _expense.getExpense())) ));                      
                        }else{
                            item.add(new Activity(_expense.getId(), branchDAO.getBranchName(_expense.getId(), brID), _expense.getAmount(), _expense.getExpense() , Balance(Total(0, 0), _expense.getExpense()) ));
                        }   
                    }else{
                        item.add(new Activity(_expense.getId(), branchDAO.getBranchName(_expense.getId(), brID), _expense.getAmount() , _expense.getExpense(), Balance(Total(0, 0), _expense.getExpense()) ));
                    }
                }
 
            }
            
        }
        return item;
    }
    
    private Boolean checkID(Integer id, ObservableList<Activity> itemlist){
        Boolean status = false;
        for (Activity item : itemlist) {
            if (id == item.getId()) {
                status = true;
            }
        }
        return status;
    }
    
    private Integer Balance(Integer amount, Integer expense){
        Integer balance = amount - expense;
        if (balance < 0) {
            balance *= 1;
        }
        return balance;
    }
    
    private Integer Index(Integer id, ObservableList<Activity> itemlist){
        Integer index = null;
        for (int i = 0; i < itemlist.size(); i++) {
            if (id == itemlist.get(i).getId()) {
                index = i;
            }
        }
       return index;
    }
    
    private Integer LastAmount(Integer index, ObservableList<Activity> itemlist ){
        return itemlist.get(index).getAmount();
    }
    
    private Integer LastExpense(Integer index, ObservableList<Activity> itemlist ){
        return itemlist.get(index).getExpense();
    }
    
    private Integer Total(Integer lastamount, Integer newamount){
        return  lastamount + newamount;
    }
    
    private Integer TotalAmount(ObservableList<Activity> amountlist){
        Integer totamount = 0;
        for (Activity _item : amountlist) {
            totamount += _item.getAmount();
        }              
        return totamount;
    }
    
    private Integer TotalExpense(ObservableList<Activity> amountlist){
        Integer totexpense = 0;
            for (Activity _item : amountlist) {
                totexpense += _item.getExpense();
            }
        return totexpense;
    }
    
    private Integer TotalBalance(ObservableList<Activity> amountlist){
        Integer totbalance = 0;
            for (Activity _item : amountlist) {
                totbalance += _item.getBalance();
            }
        return totbalance;
    }
    
    private Integer StringConvert(String digit){
        Integer total = 0;
        if (digit.matches("\\d+")) {
            total = Integer.parseInt(digit);
        }
        return total;
    }
  
    public static String DATE(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }
    
    private void setupHelp(){
        
    }
    
    private void initcol(){
        temcol.setCellValueFactory(new PropertyValueFactory("branch"));
        totalcol.setCellValueFactory(new PropertyValueFactory("amount"));
        expensecol.setCellValueFactory(new PropertyValueFactory("expense"));
        balancecol.setCellValueFactory(new PropertyValueFactory("balance"));
        producttable.setItems(activelist);
    }
    
    private void printActivity(String dateA, Integer revenue, Integer expense, Integer balance){
    ReportDAO rD = new ReportDAO();
        Task<Boolean> connect = new Task<Boolean>(){
            @Override
            protected Boolean call() throws Exception {
                return rD.printActivity(activelist, LoginController.getUsername(), DATE(), dateA, revenue, expense, balance, LoginController.getPrintLogo());
            }
        };
        exec.execute(connect);
    }
    
}
