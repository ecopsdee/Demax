
package dee.models;

import dee.DAO.EmployeeDAO;
import dee.DAO.WarehouseDAO;
import dee.database.deeinventory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Product {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty prid;
    private SimpleIntegerProperty whid;
    private SimpleStringProperty prname;
    private SimpleStringProperty whname;
    private SimpleStringProperty prtable;
    private SimpleStringProperty account;
    private SimpleStringProperty warename;
    private SimpleStringProperty type;
    private SimpleIntegerProperty numberofcartons;
    private SimpleIntegerProperty totalnumberofgoods;
    private SimpleIntegerProperty numberofPacket;
    private SimpleIntegerProperty numberOfGoodsPerPacket;
    private SimpleIntegerProperty numberOfCartondemaining;
    private SimpleIntegerProperty totalNumberOfGoodsRemaining;
    private SimpleStringProperty date;
    private String status;
    private Integer productID;
    private Integer warehouseID;
    private String producttable;
    private Integer numberOfpackets;
    private Integer numberOfgoodsPerPacket;
    private Integer numberOfcartonsRemaining;
    private Integer totalnumberOfgoodsRemaining;
    private deeinventory DbSet;
    private WarehouseDAO warehouse;
    private EmployeeDAO employeeDAO;

    public int getId() {
        return id.get();
    }

    public String getPrname() {
        return prname.get();
    }

    public String getWhname() {
        return whname.get();
    }

    public String getPrtable() {
        return prtable.get();
    }

    public Integer getProductID() {
        return productID;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public String getProducttable() {
        return producttable;
    }

    public Integer getNumberOfpackets() {
        return numberOfpackets;
    }

    public Integer getNumberOfgoodsPerPacket() {
        return numberOfgoodsPerPacket;
    }

    public Integer getNumberOfcartonsRemaining() {
        return numberOfcartonsRemaining;
    }

    public Integer getTotalnumberOfgoodsRemaining() {
        return totalnumberOfgoodsRemaining;
    }

    public int getNumberofcartons() {
        return numberofcartons.get();
    }

    public int getTotalnumberofgoods() {
        return totalnumberofgoods.get();
    }

    public int getNumberofPacket() {
        return numberofPacket.get();
    }

    public int getNumberOfGoodsPerPacket() {
        return numberOfGoodsPerPacket.get();
    }

    public int getNumberOfCartondemaining() {
        return numberOfCartondemaining.get();
    }

    public int getTotalNumberOfGoodsRemaining() {
        return totalNumberOfGoodsRemaining.get();
    }

    public int getPrid() {
        return prid.get();
    }

    public int getWhid() {
        return whid.get();
    }

    public String getWarename() {
        return warename.get();
    }

    public String getType() {
        return type.get();
    }
   
    public String getAccount() {
        return account.get();
    }
    
    public String getDate() {
        return date.get();
    }

    public String getStatus() {
        return status;
    }

   
    public Product(){
        
    }
    
    public Product(Integer id, String prname, String whname, String prtable ){
        this.id = new SimpleIntegerProperty(id);
        this.prname = new SimpleStringProperty(prname);
        this.whname = new SimpleStringProperty(whname);
        this.prtable = new SimpleStringProperty(prtable);
    }
    
    public Product(Integer WAREID, Integer PROID, String PROTABLE){
        this.warehouseID = WAREID;
        this.productID = PROID;
        this.producttable = PROTABLE;
    }
    
    public Product(Integer NOP, Integer NOGPP, Integer NOCR, Integer TNOGR){
        this.numberOfpackets = NOP;
        this.numberOfgoodsPerPacket = NOGPP;
        this.numberOfcartonsRemaining = NOCR;
        this.totalnumberOfgoodsRemaining = TNOGR;
    }

    public Product(Integer id, String prname, Integer numberofcartons, Integer totalnumberofgoods) {
        this.id = new SimpleIntegerProperty(id);
        this.prname = new SimpleStringProperty(prname);
        this.numberofcartons = new SimpleIntegerProperty(numberofcartons);
        this.totalnumberofgoods = new SimpleIntegerProperty(totalnumberofgoods);
    }

    public Product(Integer id, String prname) {
        this.id = new SimpleIntegerProperty(id);
        this.prname = new SimpleStringProperty(prname);
    }
    
    public Product(Integer id, String prname, Integer price) {
        this.id = new SimpleIntegerProperty(id);
        this.prname = new SimpleStringProperty(prname);
        this.prid = new SimpleIntegerProperty(price);
    }
    

    public Product(Integer numberofcartons, Integer totalnumberofgoods, Integer NumberofPacket, Integer NumberOfGoodsPerPacket, Integer NumberOfCartondemaining, Integer TotalNumberOfGoodsRemaining, String date, String status) {
        this.numberofcartons = new SimpleIntegerProperty(numberofcartons);
        this.totalnumberofgoods = new SimpleIntegerProperty(totalnumberofgoods);
        this.numberofPacket = new SimpleIntegerProperty(NumberofPacket);
        this.numberOfGoodsPerPacket = new SimpleIntegerProperty(NumberOfGoodsPerPacket);
        this.numberOfCartondemaining = new SimpleIntegerProperty(NumberOfCartondemaining);
        this.totalNumberOfGoodsRemaining = new SimpleIntegerProperty(TotalNumberOfGoodsRemaining);
        this.date = new SimpleStringProperty(date);
        this.status = status;
    }

    public Product(Integer id, Integer prid, Integer whid, String warename, String describe, String brand, String model, String otherinfo, String account, String type, Integer NOP, Integer NOGPP) {
        this.id = new SimpleIntegerProperty(id);
        this.prid = new SimpleIntegerProperty(prid);
        this.whid = new SimpleIntegerProperty(whid);
        this.warename = new SimpleStringProperty(warename);
        this.prname = new SimpleStringProperty(model);
        this.whname = new SimpleStringProperty(describe);
        this.prtable = new SimpleStringProperty(brand);
        this.date = new SimpleStringProperty(otherinfo);
        this.account = new SimpleStringProperty(account);
        this.type = new SimpleStringProperty(type);
        this.numberofPacket = new SimpleIntegerProperty(NOP);
        this.numberOfGoodsPerPacket = new SimpleIntegerProperty(NOGPP);
        
    }

    
}
