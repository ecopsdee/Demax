
package dee.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;


public class deeinventory {
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1/sunchohipo";
  //  private static final String URL = "jdbc:mysql://127.0.0.1/sunchohipo_dev";
    private static  final String USER = "root" ;
    private static final String PASS = "sunchohipo";
    private static Connection con;
    private static ResultSet rs;
    private static Statement stat;
    private static PreparedStatement pst;
    private static deeinventory deeDB = null;
   

    public deeinventory() {
        
    }

    public Boolean checkconnection() {
        Boolean status = true;
        if (!connection()) {
            status = false;
        }
        return status;
    }
    
    private Boolean connection() {
        Boolean status = true;
        try {  
            Class.forName(driver);
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("your Deeinventory Database is connected");
        }catch (ClassNotFoundException | SQLException ex) {
            status = false;
            System.out.println("database not connected because" + ex.getMessage());
        }
        return status;
    }
    
    private void connect() {
        try {  
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("your Deeinventory Database is connected");
        }catch (SQLException ex) {
        }
    }
    
    public static deeinventory getinstance(){
         if( deeDB == null){
                deeDB = new deeinventory();
            }
            return deeDB;
    }
    
    public PreparedStatement multiQuery(String query){
        try {
           pst = con.prepareStatement(query);
             
        }catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }
        return pst;
    }
    
    public ResultSet exeQuery(String query){
        try {
         stat = con.createStatement();
         rs = stat.executeQuery(query);
        }catch (SQLException ex) {
         ex.printStackTrace();
         ex.getMessage();
    }
        return rs;
    }
    
    public Boolean setupWarehouseAccount(String Tablename){
        Boolean status = false;
        try {
            stat = con.createStatement();
            DatabaseMetaData dmd = con.getMetaData();
            ResultSet tables  = dmd.getTables(null, null, Tablename.toUpperCase(), null);
            if(tables.next()){
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setHeaderText("Error");
                warn.setContentText("Cannot Create the Account \n Sorry The Account " + Tablename + " already exists. ");
                warn.showAndWait();
            }else{
                stat.execute("create table " + Tablename + "("
                +"id int  unsigned  not null auto_increment primary key,\n"        
                + "issueTime timestamp default CURRENT_TIMESTAMP,\n"
                +"PRId int  unsigned default null,\n"
                + "status varchar(100) not null ,\n"
                + "instock int not null,\n" 
                + "outstock int not null,\n" 
                +"remstock int not null ,\n"
                +"FOREIGN KEY(PRId) REFERENCES product(PRId)" 
                + ")");
                
                  System.out.println(Tablename + "sucessfully created!");
                   status = true;
            }
          
        } catch (SQLException ex) {
            ex.printStackTrace();
           JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
     
    public Boolean setupStockQtyTable(String Tablename){
        Boolean status = false;
        try {
            stat = con.createStatement();
            DatabaseMetaData dmd = con.getMetaData();
            ResultSet tables  = dmd.getTables(null, null, Tablename.toUpperCase(), null);
            if(tables.next()){
            Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setHeaderText("Error");
                warning.setContentText("Cannot Create the Table \n Sorry The Stock Table " + Tablename + " already exists. ");
                warning.showAndWait();
            }else{
                stat.execute("create table " + Tablename + "("
                + "id int  unsigned auto_increment not null primary key,\n"
                + "issueTime timestamp default CURRENT_TIMESTAMP,\n"
                + "STId int  unsigned  default null, \n"
                + "status varchar(100) not null,\n"
                + "invoicenumber int not null ,\n"
                + "instock int not null,\n" 
                + "outstock int not null,\n" 
                + "remstock int not null ,\n"
                + "FOREIGN KEY(STId) REFERENCES Storestock(STId)," 
                + "FOREIGN KEY(invoicenumber) REFERENCES InvoiceDetail(InvoiceNumber)" 
                + ")");
                
                System.out.println(Tablename + "sucessfully created!");
                status = true;
            }
         
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
    public Boolean setupPartnerAccount(String Tablename){
        Boolean status = false;
        try {
            stat = con.createStatement();
            DatabaseMetaData dmd = con.getMetaData();
            ResultSet tables  = dmd.getTables(null, null, Tablename.toUpperCase(), null);
            if(tables.next()){
            Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setHeaderText("Error");
                warning.setContentText("Cannot Create the Table \n Sorry The Stock Table " + Tablename + " already exists. ");
                warning.showAndWait();
            }else{
                stat.execute("create table " + Tablename + "("
                + "id int  unsigned auto_increment not null primary key,\n"
                + "issueTime timestamp default CURRENT_TIMESTAMP,\n"
                + "status varchar(100) not null,\n"
                + "TransactionSummary varchar(100) not null,\n"
                + "invoicenumber int not null ,\n"       
                + "STId int  unsigned  default null, \n"    
                + "Qty int not null,\n" 
                + "UnitPrice int not null,\n" 
                +"TotalPrice int not null ,\n"
                +"FOREIGN KEY(STId) REFERENCES Storestock(STId)," 
                + "FOREIGN KEY(invoicenumber) REFERENCES InvoiceDetail(InvoiceNumber)" 
                + ")");
                
                System.out.println(Tablename + "sucessfully created!");
                status = true;
            }
         
        } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
            }
        return status;
     }
    
    public Boolean setupPartnerBalanceSheet(String Tablename){
        Boolean status = false;
        try {
            stat = con.createStatement();
            DatabaseMetaData dmd = con.getMetaData();
            ResultSet tables  = dmd.getTables(null, null, Tablename.toUpperCase(), null);
            if(tables.next()){
            Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setHeaderText("Error");
                warning.setContentText("Cannot Create the Table \n Sorry The Stock Table " + Tablename + " already exists. ");
                warning.showAndWait();
            }else{
                stat.execute("create table " + Tablename + "("
                + "id int  unsigned auto_increment not null primary key,\n"
                + "issueTime timestamp default CURRENT_TIMESTAMP,\n"
                + "Payment int not null,\n" 
                + "Charges int not null ,\n"
                + "Balance int not null\n" 
                + ")");
                
                System.out.println(Tablename + "sucessfully created!");
                status = true;
            }
         
        } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
            }
        return status;
     }
    
    public Boolean setupBranchAccount(String Tablename){
        Boolean status = false;
        try {
            stat = con.createStatement();
            DatabaseMetaData dmd = con.getMetaData();
            ResultSet tables  = dmd.getTables(null, null, Tablename.toUpperCase(), null);
            if(tables.next()){
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setContentText("Cannot Create the Account \n Sorry The Account " + Tablename + " already exists. ");
                warn.showAndWait();
            }else{
                stat.execute("create table " + Tablename + "("
                +"id int  unsigned  not null auto_increment primary key,\n"        
                + "issueTime timestamp default CURRENT_TIMESTAMP,\n"
                + "PRId int  unsigned default null,\n"
                + "Qty int not null,\n" 
                + "UnitPrice int not null,\n" 
                + "TotalPrice int not null,\n" 
                + "FOREIGN KEY(PRId) REFERENCES product(PRId)" 
                + ")");
                
                System.out.println(Tablename + "sucessfully created!");
                status = true;
            }
          
        } catch (SQLException ex) {
            ex.printStackTrace();
           JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
    public Boolean setupBranchBalanceSheet(String Tablename){
        Boolean status = false;
        try {
            stat = con.createStatement();
            DatabaseMetaData dmd = con.getMetaData();
            ResultSet tables  = dmd.getTables(null, null, Tablename.toUpperCase(), null);
            if(tables.next()){
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setContentText("Cannot Create the Account \n Sorry The Account " + Tablename + " already exists. ");
                warn.showAndWait();
            }else{
                stat.execute("create table " + Tablename + "("
                +"id int  unsigned  not null auto_increment primary key,\n"        
                + "issueTime timestamp default CURRENT_TIMESTAMP,\n"
                + "Payment int not null,\n" 
                + "Charges int not null ,\n"
                + "Balance int not null\n" 
                + ")");
                
                System.out.println(Tablename + "sucessfully created!");
                status = true;
            }
          
        } catch (SQLException ex) {
            ex.printStackTrace();
           JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
    public Boolean setupBranchStockTable(String Tablename){
        Boolean status = false;
        try {
            stat = con.createStatement();
            DatabaseMetaData dmd = con.getMetaData();
            ResultSet tables  = dmd.getTables(null, null, Tablename.toUpperCase(), null);
            if(tables.next()){
            Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setHeaderText("Error");
                warning.setContentText("Cannot Create the Table \n Sorry The Stock Table " + Tablename + " already exists. ");
                warning.showAndWait();
            }else{
                stat.execute("create table " + Tablename + "("
                + "id int  unsigned auto_increment not null primary key,\n"
                + "issueTime timestamp default CURRENT_TIMESTAMP,\n"
                + "STId int  unsigned  default null, \n"
                + "status varchar(100) not null,\n"
                + "invoicenumber int not null ,\n"
                + "instock int not null,\n" 
                + "outstock int not null,\n" 
                + "remstock int not null ,\n"
                + "FOREIGN KEY(STId) REFERENCES Storestock(STId)," 
                + "FOREIGN KEY(invoicenumber) REFERENCES InvoiceDetail(InvoiceNumber)" 
                + ")");
                
                System.out.println(Tablename + "sucessfully created!");
                status = true;
            }
         
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error found",JOptionPane.ERROR_MESSAGE);
        }
        return status;
    }
    
}
