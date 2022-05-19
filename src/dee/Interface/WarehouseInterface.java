
package dee.Interface;

import dee.models.Warehouse;
import javafx.collections.ObservableList;


public interface WarehouseInterface {
    
    public Boolean createWarehouse(ObservableList<Warehouse> warehouse, Integer WHID, Integer BRID);
    
    public Boolean checkWarehouseId(Integer whID, Integer brID);
    
    public Integer getWarehouseId(String warename, Integer brID);
    
    public ObservableList<String> getWarename(Integer brID);
    
    public ObservableList<Warehouse>getWarehouse(Integer brID);
    
    public String warehousename(Integer whID);
}
