
package dee.DAO;

import dee.Interface.EmployeeInterface;
import dee.database.deeinventory;
import dee.models.Employee;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class EmployeeDAO implements EmployeeInterface{

    private deeinventory DbSet = deeinventory.getinstance();
    
    // create new Employee
    @Override
    public Boolean createEmployee(Employee employee, Integer brID) {
        Boolean status = false;
        try {
            String saveQuery = "insert into employee(Firstname,Lastname,Username,Password,Phone,Address,Type,BRId) values (?,?,?,?,?,?,?,?)";
            PreparedStatement saveStat = DbSet.multiQuery(saveQuery);
            saveStat.setString(1, employee.getFirstname());
            saveStat.setString(2, employee.getLastname());
            saveStat.setString(3, employee.getUsername());
            saveStat.setString(4, employee.getPassword());
            saveStat.setString(5, employee.getPhone());
            saveStat.setString(6, employee.getAddress());
            saveStat.setString(7, employee.getType());
            saveStat.setInt(8, brID);
            if (saveStat.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    @Override
    public void updateEmployee(Employee employee) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //check for the username using the branchID
    @Override
    public boolean checkUser(String username, Integer brID) {
        boolean user = false;
        try {
            String checkusername = "SELECT * FROM employee WHERE Username = '" + username + "' and BRId = '" + brID + "'";      
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if( username.equals(retrieve.getString("Username"))){
                   user = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(user);
        return user;
    }

    @Override
    public boolean checkPassword(String username, String password, Integer brID) {
        boolean pass = true;
        try {
            String checkpass = "SELECT * FROM employee WHERE Username = '" + username + "' and BRId = '" + brID + "'"; 
            System.out.println(checkpass);
            PreparedStatement check = DbSet.multiQuery(checkpass);
            ResultSet retrieve = check.executeQuery();
            System.out.println(retrieve);
            while(retrieve.next()){
               if( !password.equals(retrieve.getString("Password"))){  
                  // System.out.println( "Password is " + retrieve.getString("Password"));
                   pass = false;
               }    
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(pass);
        return pass;
    }
    
    @Override
    public Boolean updateEmployee(Integer ID) {
        Boolean status = false;
        try {
            String deletequery = "update employee set Username = '', Password = '', Type = '' where id = '" + ID +"'";
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            if (predelete.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    @Override
    public String getemployeeType(String username, Integer brID) {
        String getType = null;
        try {
            String Type = "SELECT Type FROM employee WHERE Username = '" + username +"' and BRId = '" + brID + "'";
            System.out.println(Type);
            PreparedStatement dbtype = DbSet.multiQuery(Type);
            ResultSet gettype = dbtype.executeQuery();
            System.out.println(gettype);
            while (gettype.next()) {                
                getType = gettype.getString("Type");
                System.out.println(getType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getType;
    }

    @Override
    public ObservableList<Employee> getEmployees(Integer brID) {
        ObservableList<Employee> emlist = FXCollections.observableArrayList();
        try {
            Integer count = 1;
            String emquery = "select * from employee where BRId = '" + brID + "'";
            System.out.println(emquery);
            PreparedStatement emstat = DbSet.multiQuery(emquery);
            ResultSet emres = emstat.executeQuery();  
            while (emres.next()) {
                Integer id = emres.getInt("id");
                String fName = emres.getString("Firstname");
                String lName = emres.getString("Lastname");
                String uName = emres.getString("Username");
                String pass = emres.getString("Password");
                String emphone = emres.getString("Phone");
                String addres = emres.getString("Address");
                String emType = emres.getString("Type");
                emlist.add(new Employee(id,fName, lName, uName, pass, emphone, addres, emType));
                System.out.println(emlist);
                count++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
             return emlist;
    }

    @Override
    public Boolean deleteEmployee(String username, Integer brID) {
        Boolean status = false;
        try {
            String deletequery = "delete from employee where username = '" + username +"' and BRId = '" + brID + "'";
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            if (predelete.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    @Override
    public Boolean RecordLoggedIn(String username, String Type, Integer brId) {
        Boolean status = false;
        try {
            String saveQuery = "insert into Logs(BRId,Username,Type) values (?,?,?)";
            PreparedStatement saveStat = DbSet.multiQuery(saveQuery);
            saveStat.setInt(1, brId);
            saveStat.setString(2, username);
            saveStat.setString(3, Type);
            System.out.println(saveStat);
            int save = saveStat.executeUpdate();
            if (save == 1) {
                 status = true;
            }  
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    @Override
    public Boolean RecordLoggedOut(String username, String date, Integer brID) {
        Boolean status = false;
        try {
            String saveQuery = "update Logs set TimeoutLog = '" + date +"' where Username  = '" + username +"' and BRId = '" + brID + "'";
            PreparedStatement saveStat = DbSet.multiQuery(saveQuery);
            int save = saveStat.executeUpdate();
            if (save == 1) {
                status = true;
            }  
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    @Override
    public ObservableList<Employee> displayLoggedUser(String date, Integer brID) {
        ObservableList<Employee> employeelist = FXCollections.observableArrayList();
        try {
            String getexpense = "select * from Logs where  DATE(TimeLog) = '" + date +"' and BRId = '" + brID + "'";
            PreparedStatement preexpense = DbSet.multiQuery(getexpense);
            ResultSet statexpense = preexpense.executeQuery();            
            while (statexpense.next()) {
                Timestamp issuedate = statexpense.getTimestamp("TimeLog");
                String emname = statexpense.getString("Username");
                String emtype = statexpense.getString("Type");
                employeelist.add(new Employee(0, issuedate.toString(), "", emname, "","", "", emtype));
            }   
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return employeelist;
    }

    @Override
    public ObservableList<Employee> displayLoggedUser(Integer brID) {
        ObservableList<Employee> employeelist = FXCollections.observableArrayList();
        try {
            String getexpense = "select * from Logs where BRId = '" + brID + "'";
            PreparedStatement preexpense = DbSet.multiQuery(getexpense);
            ResultSet statexpense = preexpense.executeQuery();            
            while (statexpense.next()) {
                Timestamp issuedate = statexpense.getTimestamp("TimeLog");
                String emname = statexpense.getString("Username");
                String typeem = statexpense.getString("Type");
                String outlog = statexpense.getString("TimeoutLog");
                employeelist.add(new Employee(0,issuedate.toString(), "", emname, "", "", outlog , typeem));
            }   
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return employeelist;
    }

    @Override
    public Integer getTime() {
        Integer time = 0;
        try {
            String getexpense = "select Time from Time where id =(select MAX(id) from Time) ";
            PreparedStatement preexpense = DbSet.multiQuery(getexpense);
            ResultSet statexpense = preexpense.executeQuery();            
            while (statexpense.next()) {
                time = statexpense.getInt("Time");
            }   
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return time;
    }

    @Override
    public Boolean deductTime(Integer time) {
        Boolean status = false;
        try {
            String saveQuery = "insert into Time(Time) values (?)";
            PreparedStatement saveStat = DbSet.multiQuery(saveQuery);
            saveStat.setInt(1, time);
            System.out.println(saveStat);
            int save = saveStat.executeUpdate();
            if (save == 1) {
                 status = true;
            }  
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    @Override
    public Integer getBranchID(String branch) {
       Integer id = 0;
        try {
            String getexpense = "select BRId from Branch where BRName = '" + branch + "'";
            PreparedStatement preexpense = DbSet.multiQuery(getexpense);
            ResultSet statexpense = preexpense.executeQuery();            
            while (statexpense.next()) {
                id = statexpense.getInt("BRId");
            }   
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return id;
    }
    
}
