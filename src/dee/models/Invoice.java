
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;



public class Invoice{
    private Integer invoiceno;
    private SimpleStringProperty date;
    private SimpleStringProperty status;
    private SimpleIntegerProperty invoicenum;
    private SimpleIntegerProperty id;
    private SimpleStringProperty productname;
    private SimpleIntegerProperty quantity;
    private SimpleIntegerProperty unitprice;
    private SimpleIntegerProperty totalprice;
    private SimpleIntegerProperty profit;
    private SimpleIntegerProperty loss;
    private SimpleIntegerProperty totalprofit;
    private SimpleIntegerProperty totalloss;
    
    
    public Invoice(){
    
    }

    public Invoice(Integer id, String productname, Integer qty, Integer unitprice, Integer totalprice) {
        this.id = new SimpleIntegerProperty(id);
        this.productname = new SimpleStringProperty(productname);
        this.quantity = new SimpleIntegerProperty(qty);
        this.unitprice = new SimpleIntegerProperty(unitprice);
        this.totalprice = new SimpleIntegerProperty(totalprice);
    }
    
    public Invoice(Integer id, String name, Integer qty){
        this.id = new SimpleIntegerProperty(id);
        this.productname = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(qty);
    }

    public Invoice(String date, Integer invoicenum, String productname, Integer quantity, Integer unitprice, Integer totalprice) {
        this.date = new SimpleStringProperty(date);
        this.invoicenum = new SimpleIntegerProperty(invoicenum);
        this.productname = new SimpleStringProperty(productname);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitprice = new SimpleIntegerProperty(unitprice);
        this.totalprice = new SimpleIntegerProperty(totalprice);
    }

    public Invoice(String date, Integer id, String productname, Integer quantity, Integer unitprice, Integer totalprice, Integer profit, Integer loss, Integer totalprofit, Integer totalloss) {
        this.date = new SimpleStringProperty(date);
        this.id = new SimpleIntegerProperty(id);
        this.productname = new SimpleStringProperty(productname);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitprice = new SimpleIntegerProperty(unitprice);
        this.totalprice = new SimpleIntegerProperty(totalprice);
        this.profit = new SimpleIntegerProperty(profit);
        this.loss = new SimpleIntegerProperty(loss);
        this.totalprofit = new SimpleIntegerProperty(totalprofit);
        this.totalloss = new SimpleIntegerProperty(totalloss);
    }
    
    public Invoice(String date, Integer id, String productname, Integer quantity, Integer unitprice, Integer totalprice, Integer profit, Integer loss, Integer totalprofit, String status) {
        this.date = new SimpleStringProperty(date);
        this.id = new SimpleIntegerProperty(id);
        this.productname = new SimpleStringProperty(productname);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitprice = new SimpleIntegerProperty(unitprice);
        this.totalprice = new SimpleIntegerProperty(totalprice);
        this.profit = new SimpleIntegerProperty(profit);
        this.loss = new SimpleIntegerProperty(loss);
        this.totalprofit = new SimpleIntegerProperty(totalprofit);
        this.status = new SimpleStringProperty(status);
    }
    

    public int getId() {
        return id.get();
    }

    public String getProductname() {
        return productname.get();
    }

    public String getStatus() {
        return status.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public int getUnitprice() {
        return unitprice.get();
    }

    public int getTotalprice() {
        return totalprice.get();
    }

    public Integer getInvoiceno() {
        return invoiceno;
    }

    public String getDate() {
        return date.get();
    }

    public int getInvoicenum() {
        return invoicenum.get();
    }

    public int getProfit() {
        return profit.get();
    }

    public int getLoss() {
        return loss.get();
    }

    public int getTotalprofit() {
        return totalprofit.get();
    }

    public int getTotalloss() {
        return totalloss.get();
    }
    
    
    
}
