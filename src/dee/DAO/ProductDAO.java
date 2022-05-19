
package dee.DAO;

import dee.Interface.ProductInterface;
import dee.controllers.MainviewController;
import dee.database.deeinventory;
import dee.models.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ProductDAO implements ProductInterface{
    
    deeinventory DbSet = deeinventory.getinstance();   
    WarehouseDAO warehouseDAO = new WarehouseDAO();
    EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    public Boolean createAnalysis(Integer brID, Integer sbrID, String update , Integer amount){
        Boolean status = false;
        try {
            String querycreate =  "insert into Analysis(BRId,SBRId,Status,Amount) values(?,?,?,?)";
            PreparedStatement prequery = DbSet.multiQuery(querycreate);
            prequery.setInt(1, brID);
            prequery.setInt(2, sbrID);
            prequery.setString(3, update);
            prequery.setInt(4, amount);
            if (prequery.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException e) {
        }
        return status;
    }
    
    //add a new product,  create the product account and populate the created account
    @Override
    public Boolean createProduct(ObservableList<Product> product, Integer brID) {
        Boolean status = false;
        String querycreate =  "insert into product(BRId,PRId,WHId,Description,Brand,Model,Otherinfo,AccountNumber,Type,NOP,NOGPP) values(?,?,?,?,?,?,?,?,?,?,?)";
        product.forEach((_item) -> {
            try {
                if (checkProduct(_item.getPrname(), _item.getWarename(),employeeDAO.getBranchID(MainviewController.getBranch()) )) {
                        System.out.println("the product exist in the warehouse");
                }else{
                    if (DbSet.setupWarehouseAccount(_item.getAccount())) {
                    PreparedStatement prequery = DbSet.multiQuery(querycreate);
                    prequery.setInt(1, brID); 
                    prequery.setInt(2, _item.getPrid());  //error..should create a productID
                    prequery.setInt(3, _item.getWhid());
                    prequery.setString(4, _item.getWhname());
                    prequery.setString(5, _item.getPrtable());
                    prequery.setString(6, _item.getPrname());
                    prequery.setString(7, _item.getDate());
                    prequery.setString(8, _item.getAccount());
                    prequery.setString(9, _item.getType());
                    prequery.setInt(10, _item.getNumberofPacket());
                    prequery.setInt(11, _item.getNumberOfGoodsPerPacket());
                    System.out.println(prequery);
                    
                    //populate the  product account
                    if (prequery.executeUpdate() == 1) {
                        if (populateProductAccount(_item.getAccount(), _item.getPrname(), _item.getWarename(), brID)) {
                            System.out.println("succesful");
                        }
                    }                  
                }  
                }
                
            } catch (SQLException e) {    
               e.printStackTrace();
            }
        });
        status = true;
        return  status;
    }
    
    //populate the newly created product account
    @Override
    public Boolean populateProductAccount(String accountnumber, String model, String warehousename, Integer brID) {
        Boolean status = false;
        try {
            String fullitem = "insert into " + accountnumber  + "(PRId,status,instock,outstock,remstock) values(?,?,?,?,?)";
            PreparedStatement preitem = DbSet.multiQuery(fullitem);
            preitem.setInt(1, getProductId(model, warehouseDAO.getWarehouseId(warehousename, brID)));
            preitem.setString(2, "Account Created");
            preitem.setInt(3, 0);
            preitem.setInt(4, 0);
            preitem.setInt(5, 0);
            System.out.println(preitem);
            if (preitem.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return  status;
    }
    
    //check if a product already exist
    @Override
    public Boolean checkProduct(String model, String WArehouse, Integer brId) {
        Boolean status = false;
        try {
            String checkusername = "SELECT PRId FROM Product WHERE Model = '" + model + "' and WHId = '" + warehouseDAO.getWarehouseId(WArehouse, brId) + "'";      
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("PRId")!= null){
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(status);
        return status;
    }
    
    @Override
    public Boolean checkProductID(Integer prID, Integer brID){
        Boolean status = false;
        try {
            String checkusername = "SELECT PRId FROM Product WHERE PRId = '" + prID + "' and BRId = '" + brID + "'";      
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("PRId")!= null){
                   System.out.println("PRID gotten is " + retrieve.getString("PRId") );
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(status);
        return status;
    }
    
    @Override
    public Boolean createStock(ObservableList<Product> product, Integer brID) {
        Boolean status = false;
        String querycreate =  "insert into Storestock(BRId,STId,Model,AccountNumber) values(?,?,?,?)";
        for (Product item : product) {
            try {
                if (checkStock(item.getPrname(), brID )) {
                    status = true;
                    System.out.println("the stock already exist in the store.");
                }else{
                    if (DbSet.setupStockQtyTable(item.getAccount())) {
                        PreparedStatement prequery = DbSet.multiQuery(querycreate);
                        prequery.setInt(1, brID);
                        prequery.setInt(2, item.getPrid());
                        prequery.setString(3, item.getPrname());
                        prequery.setString(4, item.getAccount());
                        System.out.println(prequery);
                    
                        //populate the  product account
                        if (prequery.executeUpdate() == 1) {
                            if (populateStockAccount(item.getAccount(), item.getPrid(), brID)) {
                                status = true;
                                System.out.println("succesful");
                            }
                        }                  
                    }  
                }
                
            } catch (SQLException e) {  
                status = false;
               e.printStackTrace();
            }
        }
        
        return  status;
    }

    @Override
    public Boolean populateStockAccount(String account, Integer stID, Integer brID) {
        Boolean status = false;
        try {
            String fullitem = "insert into " + account  + "(STId,status,invoicenumber,instock,outstock,remstock) values(?,?,?,?,?,?)";
            PreparedStatement preitem = DbSet.multiQuery(fullitem);
            preitem.setInt(1, stID);
            preitem.setString(2, "Account Created");
            preitem.setInt(3, 0);
            preitem.setInt(4, 0);
            preitem.setInt(5, 0);
            preitem.setInt(6, 0);
            System.out.println(preitem);
            if (preitem.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return  status;
    }

    //check if the stock is in the store
    @Override
    public Boolean checkStock(String model, Integer brID) {
        Boolean status = false;
        try {
            String checkusername = "SELECT STId FROM Storestock WHERE Model = '" + model + "' and BRId = '" +  brID + "'";      
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("STId")!= null){
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(status);
        return status;
    }
    
    // get a productID from the product
    @Override
    public Integer getProductId(String prname, Integer whID) {
        Integer ID = 0;
        try {
            String queryid = "select PRId from product where Model = '" + prname + "' and WHId = '" + whID + "'";
            System.out.println(queryid);
            ResultSet getprid = DbSet.exeQuery(queryid);
            while(getprid.next()){
                ID = getprid.getInt("PRId");
            }
            System.out.println("id is " + ID);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
   
    @Override
    public Boolean checkStockID(Integer stID, Integer brID){
        Boolean status = false;
        System.out.println("the stock check start");
        try {
            String proquery = "select STId from Storestock where BRId = '" + brID + "' and STId = '"+ stID + "'";
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

    //display all product in ascending order
    @Override
    public ObservableList<String> getProduct(String WName, Integer brID) {
        ObservableList<String> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select Model from product where WHId = '" + warehouseDAO.getWarehouseId(WName, brID) + "' order by Model";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String warename = prostat.getString("Model");
                prolist.add(warename);
                System.out.println(prolist);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return prolist;
    }

    //get the product account number
    @Override
    public String getProductTablename(String product, String warehouse, Integer brID) {
        String tablename = "";
        try {
            String queryname = "select AccountNumber from product where PRId = '" + getProductId(product, warehouseDAO.getWarehouseId(warehouse, brID)) + "' and WHId = '" + warehouseDAO.getWarehouseId(warehouse, brID) + "'";
            System.out.println(queryname);
            ResultSet getprid = DbSet.exeQuery(queryname);
            while(getprid.next()){
                tablename = getprid.getString("AccountNumber");
            }
            System.out.println("table name is " + tablename);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tablename;
    }
    
    //get product in ascending order
    @Override
    public ObservableList<String> getProduct(Integer brID) {
        ObservableList<String> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select distinct Model from product where BRId = '" + brID + "' order by Model";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String proname = prostat.getString("Model");
                prolist.add(proname);
                System.out.println(prolist);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return prolist;
    }
    
    @Override
    public String getStockTablename(Integer modelID, Integer brID) {
        String ID = "";
        try {
            String proquery = "select AccountNumber from Storestock where STId = '" + modelID + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getString("AccountNumber");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    @Override
    public String getProductName(Integer modelID, Integer whID) {
        String ID = "";
        try {
            String proquery = "select Model from Product where PRId = '" + modelID + "' and WHId = '" + whID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getString("Model");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return  ID;
    }
    
    @Override
    public String getStockName(Integer modelID, Integer brID) {
        String ID = "";
        try {
            String proquery = "select Model from Storestock where STId = '" + modelID + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getString("Model");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return  ID;
    }

    @Override
    public ObservableList<Product> getproductdetails(String getpname, String getwname, Integer brID) {
        ObservableList<Product> productlist = FXCollections.observableArrayList();
        try {
            Integer wareid = 0; Integer proid = 0; String protable = "";
            String prodquery = "select WHId,PRId,AccountNumber from product where PRName ='" + getpname + "' and WHName = '" + getwname +"' and BRId = '" + brID +"'";
            System.out.println(prodquery);
            PreparedStatement preprod = DbSet.multiQuery(prodquery);
            ResultSet getprod = preprod.executeQuery();
            while (getprod.next()) {
                wareid = getprod.getInt("WHId");
                proid = getprod.getInt("PRId");  
                protable = getprod.getString("PRTable");
            }
            productlist.add(new Product(wareid, proid, protable));      
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return productlist;
    }
    
    @Override
    public Integer getId(String account) {
       Integer ID = 0;
        try {
            String proquery = "select MAX(id) from " + account ;
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getInt("MAX(id)");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }

    @Override
    public Integer ProductRemaining(String account) {
        Integer ID = 0;
        try {
            String proquery = "select remstock from " + account + " where id = (select MAX(id) from " + account + ")";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getInt("remstock");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    @Override
    public Integer getStockID(String model, Integer brID) {
        Integer ID = 0;
        try {
            String proquery = "select STId from Storestock where Model = '" + model + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getInt("STId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }

    @Override
    public ObservableList<Product> displayProduct(String warehouse) {
        ObservableList<Product> productlist = FXCollections.observableArrayList(); Integer count = 1;
        try {
           String getproduct = "select Model, AccountNumber from Product where WHId = '" + warehouseDAO.getWarehouseId(warehouse,employeeDAO.getBranchID(MainviewController.getBranch())) +"'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                String prodname = statproduct.getString("Model");
                Integer TNOG = ProductRemaining(statproduct.getString("AccountNumber"));
                productlist.add(new Product(count, prodname, TNOG, 0));
                count++;
           }  
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productlist;
    }

    @Override
    public ObservableList<Product> displayProduct(Integer brID) {
        ObservableList<Product> prolist = FXCollections.observableArrayList(); Integer count = 1;
        try {
            DbSet = deeinventory.getinstance();
            String proquery = "select distinct PRName from product where BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String proname = prostat.getString("PRName");
                prolist.add(new Product(count, proname));
                System.out.println(prolist);
                count++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        return prolist;
    }

    @Override
    public ObservableList<Product> productoveralldetails(String tablename) {
        ObservableList<Product> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select * from " + tablename + " order by issueTime desc";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                Timestamp issuedate = prostat.getTimestamp("issueTime");
                String Status = prostat.getString("status");
                Integer TNOG = prostat.getInt("instock");
                Integer TNOGR = prostat.getInt("outstock");
                Integer remain = prostat.getInt("remstock");
                prolist.add(new Product(remain, TNOG, 0, 0, 0, TNOGR, issuedate.toString(), Status));
                System.out.println(prolist.toString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        return prolist;
    }
    
    @Override
    public ObservableList<Product> displaystockdetails(String table){
        ObservableList<Product> stockslist = FXCollections.observableArrayList();
        try {
           String getproduct = "select * from "+ table + " order by issueTime desc";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Timestamp Date = statproduct.getTimestamp("issueTime");
                String Status = statproduct.getString("status");
                Integer invoiceNO = statproduct.getInt("invoicenumber");
                Integer STNOG = statproduct.getInt("instock");
                Integer TSNOGR = statproduct.getInt("outstock");
                Integer remain = statproduct.getInt("remstock");
                stockslist.add(new Product(remain, STNOG, invoiceNO, 0, 0, TSNOGR, Date.toString(), Status));
                System.out.println(stockslist);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return stockslist;
    }

    @Override
    public String displayproductinfo(Integer whiID, Integer phID, Integer brID) {
        Integer result = null;
        try {
            String proquery = "select NOP,NOGPP from product where BRId = '" + brID + "' and WHId = '"+ whiID + "' and PRId = '" + phID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               result = (prostat.getInt("NOP") * prostat.getInt("NOGPP"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }

    



    

    
    
  
    
}
