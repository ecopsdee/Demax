
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Expense {
    public SimpleStringProperty describe;
    public SimpleIntegerProperty price;

    public Expense(String describe, Integer price) {
        this.describe = new SimpleStringProperty(describe);
        this.price = new SimpleIntegerProperty(price);
    }

    public String getDescribe() {
        return describe.get();
    }

    public Integer getPrice() {
        return price.get();
    }
    
    
    
}
