
package dee.DAO;

import dee.database.deeinventory;
import dee.models.Activity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// this is my activity class
public class ActivityDAO {
    
    deeinventory DbSet = deeinventory.getinstance();
    
    /*this is the method for getting all amount for the day*/
    public ObservableList<Activity> getActivityAmount(String DateA){
        ObservableList<Activity> prolist = FXCollections.observableArrayList();
        try {
            String revenuequery = "select SBRId,Amount from Analysis where DATE(issueTime) = '" + DateA +"' order by id desc " ;
            System.out.println(revenuequery);
            PreparedStatement prerevenue = DbSet.multiQuery(revenuequery);
            ResultSet statrevenue = prerevenue.executeQuery();
            while(statrevenue.next()){
                prolist.add(new Activity(statrevenue.getInt("SBRId"), "Revenue", statrevenue.getInt("Amount"), 0, 0));
            }
   
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Activity> getActivityExpense(String DateA){
        ObservableList<Activity> prolist = FXCollections.observableArrayList();
        try {
            String expensequery = "select Price from Expenses where DATE(issueTime) = '" + DateA +"' order by id desc " ;
            System.out.println(expensequery);
            PreparedStatement preexpense = DbSet.multiQuery(expensequery);
            ResultSet statexpense = preexpense.executeQuery();
            while(statexpense.next()){
                prolist.add(new Activity(0, "Expense", 0, statexpense.getInt("Price"), 0)); 
            }  
            
            //this expenses for subbranches
            String branchexpense = "select SBRId, Price from BranchExpenses where DATE(issueTime) = '" + DateA +"' order by id desc " ;
            System.out.println(branchexpense);
            PreparedStatement prebranchexpense = DbSet.multiQuery(branchexpense);
            ResultSet statbranchexpense = prebranchexpense.executeQuery();
            while(statbranchexpense.next()){
                prolist.add(new Activity(statbranchexpense.getInt("SBRId"), "Expense", 0, statbranchexpense.getInt("Price"), 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    
    
}
