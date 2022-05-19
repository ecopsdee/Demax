
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class WarehouseOrder {
    private SimpleIntegerProperty cartonnum;
    private SimpleStringProperty wname;
    private SimpleStringProperty pname;
    private SimpleStringProperty DATE;
    private String date;
   
    
    public WarehouseOrder(){
    }
    
    public WarehouseOrder( String date, String wname, String pname, Integer cartnumber) {
        this.DATE = new SimpleStringProperty(date);
        this.cartonnum = new SimpleIntegerProperty(cartnumber);
        this.wname = new SimpleStringProperty(wname);
        this.pname = new SimpleStringProperty(pname);
    }

    public WarehouseOrder(Integer cartonnum, String pname, String date) {
        this.cartonnum = new SimpleIntegerProperty(cartonnum);
        this.pname = new SimpleStringProperty(pname);
        this.date = date;
    }
 
    
    public int getCartonnum() {
        return cartonnum.get();
    }

    public String getWname() {
       return wname.get();
    }

    public String getPname() {
        return pname.get();
    }

    public String getDate() {
        return date;
    }

    public String getDATE() {
        return DATE.get();
    }
    
    
   
   
}
