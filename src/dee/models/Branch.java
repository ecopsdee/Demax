
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Branch {
    public SimpleStringProperty branchname;
    public SimpleStringProperty account;
    public SimpleStringProperty otherinfo;
    
    public SimpleStringProperty date;
    public SimpleStringProperty pname;
    public SimpleIntegerProperty qty;
    public SimpleIntegerProperty uprice;
    public SimpleIntegerProperty tprice;

    public Branch(String branchname, String account, String otherinfo, String balancesheet, Integer SBID) {
        this.branchname = new SimpleStringProperty(branchname);
        this.account = new SimpleStringProperty(account);
        this.otherinfo = new SimpleStringProperty(otherinfo);
        this.pname = new SimpleStringProperty(balancesheet);
        this.qty = new SimpleIntegerProperty(SBID);
    }

    public Branch(String pname, Integer qty, Integer uprice, Integer tprice) {
        this.pname = new SimpleStringProperty(pname);
        this.qty = new SimpleIntegerProperty(qty);
        this.uprice = new SimpleIntegerProperty(uprice);
        this.tprice = new SimpleIntegerProperty(tprice);
    }

    public Branch(String date, String pname, Integer qty, Integer uprice, Integer tprice) {
        this.date = new SimpleStringProperty(date);
        this.pname = new SimpleStringProperty(pname);
        this.qty = new SimpleIntegerProperty(qty);
        this.uprice = new SimpleIntegerProperty(uprice);
        this.tprice = new SimpleIntegerProperty(tprice);
    }

    public String getBranchname() {
        return branchname.get();
    }

    public String getAccount() {
        return account.get();
    }

    public String getOtherinfo() {
        return otherinfo.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getPname() {
        return pname.get();
    }

    public Integer getQty() {
        return qty.get();
    }

    public Integer getUprice() {
        return uprice.get();
    }

    public Integer getTprice() {
        return tprice.get();
    }
    
    
    
    
    
    
}
