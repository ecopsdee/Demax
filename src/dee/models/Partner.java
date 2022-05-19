
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;



public class Partner {
    
    //partner details
    private Integer id;
    private Integer partnerid;
    private String firstname;
    private String lastname;
    private String Phone;
    private String Origin;
    
    //order details
    private Integer orderid;
    private String orderstatus;
    private String statuscomplete;
    
    //goods bought details
    private SimpleIntegerProperty tabid;
    private SimpleStringProperty stockname;
    private SimpleIntegerProperty numberofgoods;
    private SimpleIntegerProperty unitprice;
    private SimpleIntegerProperty totalprice;
    
    //other information
    private Integer invoiceNumber;
    private Integer TotalNumberofGood;
    private Integer TotalPrice;
    private Integer amountpaid;
    private Integer balance;
    
    //partner details for table display
    private SimpleIntegerProperty PartID;
    private SimpleStringProperty fname;
    private SimpleStringProperty lname;
    
    //order details for table display
    private SimpleIntegerProperty tabOrder;
    private SimpleStringProperty taborderstatus;
    private SimpleStringProperty tabdate;
    
     //goods other information for table display
    private SimpleStringProperty tabpartnername;
    private SimpleIntegerProperty tabinvoice;
    
    

    public Integer getId() {
        return id;
    }
    
    public Integer getPartnerid() {
        return partnerid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return Phone;
    }

    public String getOrigin() {
        return Origin;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public String getStatuscomplete() {
        return statuscomplete;
    }

    public String getStockname() {
        return stockname.get();
    }

    public int getNumberofgoods() {
        return numberofgoods.get();
    }

    public int getUnitprice() {
        return unitprice.get();
    }

    public int getTotalprice() {
        return totalprice.get();
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public Integer getTotalNumberofGood() {
        return TotalNumberofGood;
    }

    public Integer getTotalPrice() {
        return TotalPrice;
    }

    public Integer getAmountpaid() {
        return amountpaid;
    }

    public Integer getBalance() {
        return balance;
    }

    public int getPartID() {
        return PartID.get();
    }

    public String getFname() {
        return fname.get();
    }

    public String getLname() {
        return lname.get();
    }

    

    public int getTabOrder() {
        return tabOrder.get();
    }

    public String getTaborderstatus() {
        return taborderstatus.get();
    }

    public String getTabdate() {
        return tabdate.get();
    }

    public String getTabpartnername() {
        return tabpartnername.get();
    }

    public int getTabinvoice() {
        return tabinvoice.get();
    }

    public int getTabid() {
        return tabid.get();
    }

    
    
    
    public Partner() {
    }
    
    public Partner(Integer partnerid, String firstname, String lastname, String Phone, String Origin) {
        this.partnerid = partnerid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.Phone = Phone;
        this.Origin = Origin;
    }

    public Partner(Integer partnerid, Integer orderid, String orderstatus, String statuscomplete) {
        this.partnerid = partnerid;
        this.orderid = orderid;
        this.orderstatus = orderstatus;
        this.statuscomplete = statuscomplete;
    }

    public Partner(Integer tabid ,Integer id, String stockname, Integer numberofgoods, Integer unitprice, Integer totalprice) {
        this.tabid = new SimpleIntegerProperty(tabid);
        this.id = id;
        this.stockname = new SimpleStringProperty(stockname);
        this.numberofgoods = new SimpleIntegerProperty(numberofgoods);
        this.unitprice = new SimpleIntegerProperty(unitprice);
        this.totalprice = new SimpleIntegerProperty(totalprice);
    }
    
    public Partner(Integer partnerid, Integer orderid, Integer TotalNumberofGood, Integer TotalPrice, Integer amountpaid, Integer balance) {
        this.partnerid = partnerid;
        this.orderid = orderid;
        this.TotalNumberofGood = TotalNumberofGood;
        this.TotalPrice = TotalPrice;
        this.amountpaid = amountpaid;
        this.balance = balance;
    }

    //check on the usuage
    public Partner(Integer id, String Fname, Integer PartID , String Lname) {
        this.id = id;
        this.PartID = new SimpleIntegerProperty(PartID);
        this.fname = new SimpleStringProperty(Fname);
        this.lname = new SimpleStringProperty(Lname);
    } 

    public Partner(Integer id, String firstname, String lastname, Integer TotalNumberofGood, Integer TotalPrice, Integer Order) {
        this.partnerid = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.TotalNumberofGood = TotalNumberofGood;
        this.TotalPrice = TotalPrice;
        this.orderid = Order;
    }
    
    public Partner(Integer id, String stockname, Integer qty){
        this.id = id;
        this.stockname = new SimpleStringProperty(stockname);
        this.numberofgoods = new SimpleIntegerProperty(qty);
    }

    public Partner(Integer tabOrder, String taborderstatus, String tabdate) {
        this.tabOrder = new SimpleIntegerProperty(tabOrder);
        this.taborderstatus = new SimpleStringProperty(taborderstatus);
        this.tabdate = new SimpleStringProperty(tabdate);
    }

    public Partner(Integer totalprice, Integer tabOrder, String tabpartnername, Integer tabinvoice) {
        this.totalprice = new SimpleIntegerProperty(totalprice);
        this.tabOrder = new SimpleIntegerProperty(tabOrder);
        this.tabpartnername = new SimpleStringProperty(tabpartnername);
        this.tabinvoice = new SimpleIntegerProperty(tabinvoice);
    }

    public Partner(Integer TotalNumberofGood, Integer TotalPrice, Integer amountpaid, Integer balance, Integer invoice) {
        this.TotalNumberofGood = TotalNumberofGood;
        this.TotalPrice = TotalPrice;
        this.amountpaid = amountpaid;
        this.balance = balance;
        this.invoiceNumber = invoice;
    }

    public Partner(Integer partnerid, Integer orderid, Integer invoiceNumber, Integer TotalNumberofGood, Integer TotalPrice, Integer amountpaid, Integer balance) {
        this.partnerid = partnerid;
        this.orderid = orderid;
        this.invoiceNumber = invoiceNumber;
        this.TotalNumberofGood = TotalNumberofGood;
        this.TotalPrice = TotalPrice;
        this.amountpaid = amountpaid;
        this.balance = balance;
    }

    //check on the usuage
    public Partner(Integer tabid, String stockname, Integer numberofgoods, Integer unitprice, Integer totalprice, String Fname, String Lname) {
        this.lname = new SimpleStringProperty(Lname);
        this.fname = new SimpleStringProperty(Fname);
        this.stockname = new SimpleStringProperty(stockname);
        this.tabid = new SimpleIntegerProperty(tabid); 
        this.numberofgoods = new SimpleIntegerProperty(numberofgoods);
        this.unitprice = new SimpleIntegerProperty(unitprice);
        this.totalprice = new SimpleIntegerProperty(totalprice);
        
        
    }

    
    
    
}

