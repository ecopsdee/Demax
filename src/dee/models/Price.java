
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Price {
    private Integer stId;
    private String stName;
    private Integer stPrice;
    private Integer count;
    
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty price;
    private SimpleIntegerProperty costprice;
    

    public Integer getStId() {
        return stId;
    }

    public String getStName() {
        return stName;
    }

    public Integer getStPrice() {
        return stPrice;
    }

    public Integer getCount() {
        return count;
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public int getPrice() {
        return price.get();
    }

    public int getCostprice() {
        return costprice.get();
    }

    public Price() {
    }

    public Price(Integer stId, String stName, Integer stPrice) {
        this.stId = stId;
        this.stName = stName;
        this.stPrice = stPrice;
    }

    public Price(Integer stId, Integer stPrice, Integer count) {
        this.stId = stId;
        this.stPrice = stPrice;
        this.count = count;
    }

    public Price(String name, Integer id, Integer price, Integer costprice) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name) ;
        this.price = new SimpleIntegerProperty(price);
        this.costprice = new SimpleIntegerProperty(costprice);
    }
    
    
    
}
