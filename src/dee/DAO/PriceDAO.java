
package dee.DAO;

import dee.database.deeinventory;
import dee.models.Invoice;
import dee.models.Price;
import dee.models.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class PriceDAO {
    
    deeinventory DbSet = deeinventory.getinstance(); 
    ProductDAO productDAO = new ProductDAO();
    
    
    public Boolean CreateStockPrice(ObservableList<Product> product, Integer count, Integer price,Integer costprice, Integer brID){
        Boolean status = false;
        String querycreate =  "insert into PriceTag(BRId,STId,Price,CostPrice,UpdateCount) values(?,?,?,?,?)";
        for (Product item : product) {
            try {
                if ( productDAO.checkStockID(productDAO.getStockID(item.getPrname(), brID), brID) && checkStock(productDAO.getStockID(item.getPrname(), brID), brID)) {
                    status = true;
                    System.out.println("the initial stock price already exist");
                }else if(productDAO.checkStockID(productDAO.getStockID(item.getPrname(), brID), brID)) {
                    PreparedStatement prequery = DbSet.multiQuery(querycreate);
                    prequery.setInt(1, brID);
                    prequery.setInt(2, item.getPrid());
                    prequery.setInt(3, price);
                    prequery.setInt(4, costprice);
                    prequery.setInt(5, count);
                    System.out.println(prequery);
                    if (prequery.executeUpdate() == 1) {
                        status = true;
                        System.out.println("the new initial price recorded ");
                    }
                }
                 
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }        
        }
            
        return status;
    }
    
    public Boolean UpdateStockPrice(Integer stID, Integer price, Integer costprice, Integer count, Integer brID){
        Boolean status = false;
        String querycreate =  "insert into PriceTag(BRId,STId,Price,CostPrice,UpdateCount) values(?,?,?,?,?)";
            try {
                PreparedStatement prequery = DbSet.multiQuery(querycreate);
                prequery.setInt(1, brID);
                prequery.setInt(2, stID);
                prequery.setInt(3, price);
                prequery.setInt(4, costprice);
                prequery.setInt(5, addCount(getCount(stID), count));
                System.out.println(prequery);
                if (prequery.executeUpdate() == 1) {
                    status = true;
                } 
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        return status;
    }
    
    private Integer addCount(Integer former, Integer later){      
        return former + later;
    }
    
    private Integer getCount(Integer stID){
        Integer ID = 0;
        try {
            String proquery = "select UpdateCount from PriceTag where STId = '" + stID +"'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getInt("UpdateCount");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    public Integer getStockPrice(Integer stockid){
        Integer stockID = 0;
        try {
            String queryid = "select Price from PriceTag where STId = '" + stockid + "' and UpdateCount = (select MAX(UpdateCount) from PriceTag where STId = '" + stockid + "')" ;
            System.out.println(queryid);
            ResultSet getprid = DbSet.exeQuery(queryid);
            while(getprid.next()){
                stockID = getprid.getInt("Price");
            }
            System.out.println("id is " + stockID);
        } catch (SQLException ex) {
           ex.printStackTrace();
        }  
        return stockID;
    }
    
    public Integer getCostPrice(Integer stockid){
        Integer stockID = 0;
        try {
            String queryid = "select CostPrice from PriceTag where STId = '" + stockid + "' and UpdateCount = (select MAX(UpdateCount) from PriceTag where STId = '" + stockid + "')" ;
            System.out.println(queryid);
            ResultSet getprid = DbSet.exeQuery(queryid);
            while(getprid.next()){
                stockID = getprid.getInt("CostPrice");
            }
            System.out.println("id is " + stockID);
        } catch (SQLException ex) {
           ex.printStackTrace();
        }  
        return stockID;
    }
    
    public ObservableList<Price> getPrice(Integer brID){
        ObservableList<Price> pricelist = FXCollections.observableArrayList();
        try {
            Integer count = 1; Integer ID = 0; Integer price = 0; String namestock = ""; Integer costprice = 0;
            String proquery = "select distinct STId from Storestock where BRId = '"  + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                ID = prostat.getInt("STId");
                namestock = productDAO.getStockName(ID, brID);
                
                String pricequery = "select Price, CostPrice from PriceTag where STId = '" + ID + "' and UpdateCount = (select MAX(UpdateCount) from PriceTag where STId = '" + ID + "')" ;
                System.out.println(pricequery);
                PreparedStatement proprice = DbSet.multiQuery(pricequery);
                ResultSet pricestat = proprice.executeQuery();  
                if (pricestat.next()) {
                   price = pricestat.getInt("Price");
                   costprice = pricestat.getInt("CostPrice");
                   System.out.println(price);
                }
                
                pricelist.add(new Price(namestock, count, price, costprice));
                count++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        return pricelist;
    }
    
    public Boolean checkStock(Integer stID, Integer brID){
        Boolean status = false;
        try {
            Integer count = 1; 
            String proquery = "select STId from PriceTag where BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                if (stID.equals(prostat.getInt("STId"))) {
                    status = true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println(status);
        return status;
    }
    
    public ObservableList<Product> getStock(Integer brID){
       ObservableList<Product> stolist = FXCollections.observableArrayList();
       try {
           String getquery = "select Model, AccountNumber from Storestock where BRId = '" + brID + "' order by Model "; 
           PreparedStatement propre = DbSet.multiQuery(getquery);
           ResultSet prostat = propre.executeQuery();  
           while (prostat.next()) {
               String model = prostat.getString("Model");
               Integer qty = productDAO.ProductRemaining(prostat.getString("AccountNumber"));
               Integer price = getStockPrice(productDAO.getStockID(model, brID));
               stolist.add(new Product(qty, model,price));
               System.out.println(stolist);
           }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return stolist;
    }
    
    public ObservableList<Invoice> getProfitandLossSheet(String date, Integer brID){
        ObservableList<Invoice> invoicelist = FXCollections.observableArrayList(); Integer stID = 0; Integer profit = 0; Integer loss = 0;
        try {
           String getsheet = "select issueTime,STId,UnitPrice,RemainQty,ReturnTotalPrice from Invoice where BRId = '" + brID + "' and DATE(issueTime) = '" + date + "'";
           System.out.println(getsheet);
           PreparedStatement preproduct = DbSet.multiQuery(getsheet); 
           ResultSet statproduct = preproduct.executeQuery(); 
            while (statproduct.next()) {                
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                stID = statproduct.getInt("STId");
                String pname = productDAO.getStockName(stID, brID);
                Integer costprice = getCostPrice(stID);
                Integer unitprice = statproduct.getInt("UnitPrice");
                if (Total(costprice, unitprice) > 0) {
                    profit = Total(costprice, unitprice);
                    loss = 0;
                }else if(Total(costprice, unitprice) < 0){
                    loss = Total(costprice, unitprice);
                    profit = 0;
                }
                Integer qty = statproduct.getInt("RemainQty");
                Integer totalrev = statproduct.getInt("ReturnTotalPrice");
                Integer totprofit = TotalProfit(qty, profit);
                Integer totloss = TotalProfit(qty, loss);
                System.out.println("profit = " + profit);
                System.out.println("loss = "+ loss);
                invoicelist.add(new Invoice(issuedate.toString(), costprice, pname, qty, unitprice, totalrev, profit, loss, totprofit, totloss));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoicelist;
   
    }
    
    public ObservableList<Invoice> getProfitandLossSheet(String dateA, String dateB, Integer brID){
        ObservableList<Invoice> invoicelist = FXCollections.observableArrayList(); Integer stID = 0; Integer profit = 0; Integer loss = 0;
        try {
           String getsheet = "select issueTime,STId,UnitPrice,RemainQty,ReturnTotalPrice from Invoice where BRId = '" + brID + "' and DATE(issueTime) >= '" + dateA + "' and DATE(issueTime) <= '" + dateB +  "'";
           System.out.println(getsheet);
           PreparedStatement preproduct = DbSet.multiQuery(getsheet); 
           ResultSet statproduct = preproduct.executeQuery(); 
            while (statproduct.next()) {                
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                stID = statproduct.getInt("STId");
                String pname = productDAO.getStockName(stID, brID);
                Integer costprice = getCostPrice(stID);
                Integer unitprice = statproduct.getInt("UnitPrice");
                if (Total(costprice, unitprice) > 0) {
                    profit = Total(costprice, unitprice);
                    loss = 0;
                }else if(Total(costprice, unitprice) < 0){
                    loss = Total(costprice, unitprice);
                    profit = 0;
                }
                Integer qty = statproduct.getInt("RemainQty");
                Integer totalrev = statproduct.getInt("ReturnTotalPrice");
                Integer totprofit = TotalProfit(qty, profit);
                Integer totloss = TotalProfit(qty, loss);
                System.out.println("profit = " + profit);
                System.out.println("loss = "+ loss);
                invoicelist.add(new Invoice(issuedate.toString(), costprice, pname, qty, unitprice, totalrev, profit, loss, totprofit, totloss));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoicelist;
   
    }
     
    public Integer Total(Integer cp, Integer sp){
        return sp - cp;
    }
    
    public Integer TotalProfit(Integer qty, Integer profit){
        return qty * profit;
    }

    
}
