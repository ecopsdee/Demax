
package dee.DAO;

import dee.database.deeinventory;
import dee.models.Expense;
import java.sql.PreparedStatement;
import javafx.collections.ObservableList;


public class ExpenseDAO {
    
    deeinventory DbSet = deeinventory.getinstance();
    
    public Boolean createExpenses(ObservableList<Expense> expenses, String user){
        Boolean status = false;
        String querycreate =  "insert into Expenses(EmployeeName,Description,Price) values(?,?,?)";
        for (Expense _item : expenses) {
            try {
                PreparedStatement prequery = DbSet.multiQuery(querycreate);
                prequery.setString(1, user);
                prequery.setString(2, _item.getDescribe());
                prequery.setInt(3, _item.getPrice());
                System.out.println(prequery);
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }     
            }catch (Exception e) {
                status = false;
            }
        }
        return status;
    }
    
    public Boolean createBranchExpenses(ObservableList<Expense> expenses, String user, Integer subID){
        Boolean status = false;
        String querycreate =  "insert into BranchExpenses( SBRId,EmployeeName,Description,Price) values(?,?,?,?)";
        for (Expense _item : expenses) {
            try {
                PreparedStatement prequery = DbSet.multiQuery(querycreate);
                prequery.setInt(1, subID);
                prequery.setString(2, user);
                prequery.setString(3, _item.getDescribe());
                prequery.setInt(4, _item.getPrice());
                System.out.println(prequery);
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }     
            }catch (Exception e) {
                status = false;
            }
        }
        return status;
    }
    
    
}
