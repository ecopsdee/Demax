
package dee.Interface;

import dee.models.Invoice;
import dee.models.WarehouseOrder;
import javafx.collections.ObservableList;


public interface WarehouseOrderInterface {
    
    public Boolean CreateOrder(ObservableList<WarehouseOrder> order, Integer brID);
    
    public Boolean updateProductAccount(ObservableList<WarehouseOrder> stock, Integer invoice, Integer brID); 
    
    public Boolean updateStockAccount(ObservableList<WarehouseOrder> stock, Integer invoice, Integer brID); 
    
    public Boolean deductStockAccount(ObservableList<Invoice> stock, Integer invoice, Integer brID); 
    
    public Integer productRemain(Integer goodRem, Integer goodQty);
    
    public Integer StockRemain(Integer goodRem, Integer goodQty);
    
    public ObservableList<WarehouseOrder>getwarehouseorder(Integer brID);
    
    public ObservableList<WarehouseOrder>getwarehouseorder(String Date, Integer brID);
    
    public ObservableList<WarehouseOrder>getwarehouseorder(String Date, String Date2, Integer brID);
    
}
