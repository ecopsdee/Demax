
package dee.DAO;

import dee.database.deeinventory;
import dee.models.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class CustomerDAO {
    
    deeinventory DbSet = deeinventory.getinstance();
    
    public ObservableList<Customer> getCustomer(Integer brID){
       ObservableList<Customer> custlist = FXCollections.observableArrayList();
       try {
            String display = "select CustId, Name, Location, Status  from customer where BRId = '" + brID +"' order by Name";
            System.out.println(display);
            PreparedStatement presto = DbSet.multiQuery(display);
            ResultSet statsto = presto.executeQuery();
            while(statsto.next()){
                Integer Id = statsto.getInt("CustId");
                String firstname = statsto.getString("Name");
                String ORIGIN = statsto.getString("Location");
                String status = statsto.getString("Status");
                custlist.add(new Customer(ORIGIN,Id,firstname,status));
              }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return custlist;
    }
    
    public ObservableList<Customer> getCustomerTempList(Integer customerID, Integer brID){
       ObservableList<Customer> custlist = FXCollections.observableArrayList();
       try {
            String display = "select Name, Location  from customernp where CustId = '" + customerID + "' and BRId = '" + brID + "'";
            System.out.println(display);
            PreparedStatement presto = DbSet.multiQuery(display);
            ResultSet statsto = presto.executeQuery();
            while(statsto.next()){
                String firstname = statsto.getString("Name");
                String ORIGIN = statsto.getString("Location");
                custlist.add(new Customer(0, firstname, ORIGIN));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return custlist;
    }

    public Boolean CreateCustomer(Customer customers, Integer brID, String detail){
        Boolean status = false;
        String querycreate =  "insert into customer(BRId,CustId,Name,Location,Status) values(?,?,?,?,?)";
        try {
            PreparedStatement precreate = DbSet.multiQuery(querycreate);
            precreate.setInt(1, brID);
            precreate.setInt(2, customers.getId());
            precreate.setString(3, customers.getFname());
            precreate.setString(4, customers.getOrigin());
            precreate.setString(5, detail);
            Integer success = precreate.executeUpdate();
            if (success == 1) {
            status = true;
            }

        }catch (SQLException ex) {
        ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean checkCustomer(Integer CustomerID, Integer brID, String status, String name){
        Boolean check = true; String _status = ""; String _name = ""; Integer _custID = null;
        try {
            String checkcustomer = "SELECT CustId, Name, Status FROM customer WHERE CustId = '" + CustomerID + "' and BRId = '" + brID + "'";      
            System.out.println(checkcustomer);
            PreparedStatement checkuname = DbSet.multiQuery(checkcustomer);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               _custID = retrieve.getInt("CustId");
               _name = retrieve.getString("Name");
               _status = retrieve.getString("Status");  
            }  
            if (!Objects.equals(_custID, CustomerID) && !_status.equals(status) && !_name.equals(name)) {
                check = true;
            }else if (Objects.equals(_custID, CustomerID)  && ! _name.equals(name)) {
                check = false;
            }
        } catch (Exception ex) {
        }
        return check;
    }
    
    public Boolean checkCustomerID(Integer custID, Integer brID){
        Boolean status = false;
        try {
            String checkusername = "SELECT CustId FROM customer WHERE CustId = '" + custID + "' and BRId = '" + brID + "'";      
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("CustId")!= null){
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(status);
        return status;
    }
    
    public Boolean checkCustomerStatus(Integer CustomerID, Integer brID, String status){
        Boolean check = false; String user_status = "";
        try {
            String checkcustomer = "SELECT Status FROM customer WHERE CustId = '" + CustomerID + "' and BRId = '" + brID + "'";      
            System.out.println(checkcustomer);
            PreparedStatement checkuname = DbSet.multiQuery(checkcustomer);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               user_status = retrieve.getString("Status"); 
            }
            if (user_status.equals("")) {
                check = true;
            }else if(user_status.equals(status)){
                check = true;
            }
        } catch (Exception ex) {
        }
        return check;
    }
    
    public Boolean SendCustomer(Customer customers, Integer brID, String detail, Integer invoice){
        Boolean status = false;
        String querycreate =  "insert into customernp(BRId,InvoiceNumber,CustId,Name,Location,Status) values(?,?,?,?,?,?)";
        try {
            PreparedStatement precreate = DbSet.multiQuery(querycreate);
            precreate.setInt(1, brID);
            precreate.setInt(2, invoice);
            precreate.setInt(3, customers.getId());
            precreate.setString(4, customers.getFname());
            precreate.setString(5, customers.getOrigin());
            precreate.setString(6, detail);
            System.out.println(precreate);
            Integer success = precreate.executeUpdate();
            if (success == 1) {
                status = true;
            }
          
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean DeleteCustomerReview(Customer customers, Integer brID, String detail, Integer invoice){
        Boolean status = false;
        String querycreate =  "insert into CustomerNPDelete(BRId, InvoiceNumber,CustId,Name,Location, Status) values(?,?,?,?,?,?)";
        try {
            PreparedStatement precreate = DbSet.multiQuery(querycreate);
            precreate.setInt(1, brID);
            precreate.setInt(2, invoice);
            precreate.setInt(3, customers.getId());
            precreate.setString(4, customers.getFname());
            precreate.setString(5, customers.getOrigin());
            precreate.setString(6, detail);
            System.out.println(precreate);
            Integer success = precreate.executeUpdate();
            if (success == 1) {
                status = true;
            }
          
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean deleteCustomerTempList(Integer customerID, Integer brID, Integer invoice){
        Boolean status = false;
        try {
            String deletequery = "delete from customernp where CustId = '" + customerID + "' and BRId = '" + brID + "' and InvoiceNumber = '" + invoice + "'";
            System.out.println(deletequery);
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            int redelete = predelete.executeUpdate();
            if (redelete == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(status);
        return  status;
    }
    
    public String getcustomername(Integer customerID, Integer brID){
       String custlist = "";
       try {
            String display = "select Name from Customer where CustId = '" + customerID + "' and BRId = '" + brID + "'";
            System.out.println(display);
            PreparedStatement presto = DbSet.multiQuery(display);
            ResultSet statsto = presto.executeQuery();
            while(statsto.next()){
               custlist = statsto.getString("Name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return custlist;
    }
    
    public Customer getcustomerdetail(Integer customerID, Integer brID){
       String name = ""; String location = "";
       try {
            String display = "select Name, Location from CustomerNP where CustId = '" + customerID + "' and BRId = '" + brID + "'";
            System.out.println(display);
            PreparedStatement presto = DbSet.multiQuery(display);
            ResultSet statsto = presto.executeQuery();
            while(statsto.next()){
               name = statsto.getString("Name");
               location = statsto.getString("Location");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Customer(0, name, location);
    }
    
    
    
}
