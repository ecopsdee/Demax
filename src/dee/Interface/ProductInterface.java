
package dee.Interface;

import dee.models.Product;
import javafx.collections.ObservableList;


public interface ProductInterface {
    
    public Boolean createAnalysis(Integer brID, Integer sbrID, String update , Integer amount);
    
    public Boolean createProduct(ObservableList<Product> product, Integer brID);
    
    public Boolean createStock(ObservableList<Product> product, Integer brID);
    
    public Boolean populateProductAccount(String account, String model, String warehousename, Integer brID);
    
    public Boolean populateStockAccount(String account, Integer model, Integer brID);
    
    public Boolean checkProduct(String model, String warehouse, Integer brID);
    
    public Boolean checkProductID(Integer prID, Integer brID);
    
    public Boolean checkStockID(Integer stID, Integer brID);
    
    public Boolean checkStock(String model, Integer brID);
    
    public Integer getId(String account);
    
    public Integer getProductId(String prname, Integer whID);
    
    public Integer getStockID(String model, Integer brID);
    
    public ObservableList<String> getProduct(String WName, Integer brID);
    
    public String getProductTablename(String product, String warehouse, Integer brID);
    
    public String getProductName(Integer modelID, Integer whID);
    
    public String getStockTablename(Integer modelID, Integer brID);
    
    public String getStockName(Integer modelID, Integer brID);
    
    public String displayproductinfo(Integer whiID, Integer phID, Integer brID);
    
    public ObservableList<String> getProduct(Integer brID);
    
    public ObservableList<Product> getproductdetails(String getpname, String getwname, Integer brID);
    
    public ObservableList<Product> displayProduct(String warehouse);
    
    public ObservableList<Product> displayProduct(Integer brID);
    
    public ObservableList<Product> productoveralldetails(String tablename);
    
    public ObservableList<Product> displaystockdetails(String table);
    
    public Integer ProductRemaining(String table);
    
}
