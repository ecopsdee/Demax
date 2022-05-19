
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Activity {
    
    private SimpleIntegerProperty id;
    private SimpleStringProperty branch;  
    private SimpleIntegerProperty amount;
    private SimpleIntegerProperty expense;
    private SimpleIntegerProperty balance;

    public Activity(Integer id, String branch, Integer total, Integer expense, Integer balance) {
        this.id = new SimpleIntegerProperty(id);
        this.branch = new SimpleStringProperty(branch);
        this.amount = new SimpleIntegerProperty(total);
        this.expense = new SimpleIntegerProperty(expense);
        this.balance = new SimpleIntegerProperty(balance);
    }

    public int getId() {
        return id.get();
    } 
    
    public String getBranch() {
        return branch.get();
    }

    public int getAmount() {
        return amount.get();
    }

    public Integer getExpense() {
        return expense.get();
    }

    public Integer getBalance() {
        return balance.get();
    }
    
    
    
    
}
