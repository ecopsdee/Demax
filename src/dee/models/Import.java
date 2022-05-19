
package dee.models;

import dee.database.deeinventory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;



public class Import {
    private  SimpleStringProperty warehouse;
    private  SimpleStringProperty productname;
    private  SimpleIntegerProperty totalqtyofgoods;
    
    private String importstatus;
    private  deeinventory DbSet;
   
    
    public Import(){
    
    }
    
    public Import( String warename ,String productname,String status, Integer totalqtyofgoods) {
        this.warehouse = new SimpleStringProperty(warename);
        this.productname = new SimpleStringProperty(productname);
        this.importstatus = status;
        this.totalqtyofgoods = new SimpleIntegerProperty(totalqtyofgoods);
    }
    
    public String getProductname() {
        return productname.get();
    }

    public int getTotalqtyofgoods() {
        return totalqtyofgoods.get();
    }

    public String getWarehouse() {
        return warehouse.get();
    }

    public String getImportstatus() {
        return importstatus;
    }
    
    
    
}
