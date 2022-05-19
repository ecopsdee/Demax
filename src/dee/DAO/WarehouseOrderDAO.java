
package dee.DAO;

import dee.Interface.WarehouseOrderInterface;
import dee.database.deeinventory;
import dee.models.Invoice;
import dee.models.WarehouseOrder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class WarehouseOrderDAO implements WarehouseOrderInterface{

    deeinventory DbSet = deeinventory.getinstance(); 
    WarehouseDAO warehouseDAO = new WarehouseDAO();
    ProductDAO productDAO = new ProductDAO();
    
    @Override
    public Boolean CreateOrder(ObservableList<WarehouseOrder> order, Integer brID) {
        Boolean status = false;
        String querycreate = "insert into WarehouseTakeout(BRId, WHId,PRId,Qty) values(?,?,?,?)";
        for (WarehouseOrder item : order) {
            try {
                PreparedStatement prequery = DbSet.multiQuery(querycreate);
                prequery.setInt(1, brID);
                prequery.setInt(2, warehouseDAO.getWarehouseId(item.getWname(), brID));
                prequery.setInt(3, productDAO.getProductId(item.getPname(), warehouseDAO.getWarehouseId(item.getWname(), brID)));
                prequery.setInt(4, item.getCartonnum());
                System.out.println(prequery);
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }
            } catch(SQLException ex) {
                status = false;
                ex.printStackTrace();
            }
        }
        System.out.println(status);
        return status;
    }

    @Override
    public Integer productRemain(Integer goodRem, Integer goodQty) {
        Integer ID = goodRem - goodQty;
        return ID;
    }

    @Override
    public Integer StockRemain(Integer goodRem, Integer goodQty) {
        Integer ID = goodRem + goodQty;
        return ID;
    }

    @Override
    public ObservableList<WarehouseOrder> getwarehouseorder(Integer brID) {
        ObservableList<WarehouseOrder> productlist = FXCollections.observableArrayList();
        try {
           String getproduct = "select PRId,WHId,issueTime, Qty from WarehouseTakeout where BRId = '" + brID + "'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                String prodname = productDAO.getProductName(statproduct.getInt("PRId"), statproduct.getInt("WHId"));
                String warename = warehouseDAO.warehousename(statproduct.getInt("WHId"));
                Timestamp Date = statproduct.getTimestamp("issueTime");
                Integer NOC = statproduct.getInt("Qty");
                productlist.add(new WarehouseOrder(Date.toString(),warename, prodname,NOC));
                System.out.println(productlist);
           }  
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productlist;   
    }

    @Override
    public ObservableList<WarehouseOrder> getwarehouseorder(String Date, Integer brID) {
        ObservableList<WarehouseOrder> productlist = FXCollections.observableArrayList();
        try {
           String getproduct = "select PRId ,WHId,issueTime, Qty  from WarehouseTakeout where DATE(issueTime) = '" + Date +"' and BRId = '" + brID + "'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                String prodname = productDAO.getProductName(statproduct.getInt("PRId"), statproduct.getInt("WHId"));
                String warename = warehouseDAO.warehousename(statproduct.getInt("WHId"));
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                Integer NOC = statproduct.getInt("Qty");
                productlist.add(new WarehouseOrder(issuedate.toString(),warename, prodname,NOC));
           }  
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productlist;   
    }
    
    @Override
    public ObservableList<WarehouseOrder> getwarehouseorder(String Date, String Date2, Integer brID) {
        ObservableList<WarehouseOrder> productlist = FXCollections.observableArrayList();
        try {
           String getproduct = "select PRId ,WHId,issueTime, Qty  from WarehouseTakeout where DATE(issueTime) >= '" + Date +"' and DATE(issueTime) <= '" + Date2 + "' and  BRId = '" + brID + "'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                String prodname = productDAO.getProductName(statproduct.getInt("PRId"), statproduct.getInt("WHId"));
                String warename = warehouseDAO.warehousename(statproduct.getInt("WHId"));
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                Integer NOC = statproduct.getInt("Qty");
                productlist.add(new WarehouseOrder(issuedate.toString(),warename, prodname,NOC));
           }  
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productlist;   
    }

    @Override
    public Boolean updateProductAccount(ObservableList<WarehouseOrder> stock, Integer invoice, Integer brID) {
        Boolean status = false;
        for (WarehouseOrder item : stock) {
            try {
                String saveQuery = "insert into " + productDAO.getProductTablename(item.getPname(), item.getWname(), brID) + "(PRId,status,instock,outstock,remstock) values (?,?,?,?,?)";
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, productDAO.getProductId(item.getPname(), warehouseDAO.getWarehouseId(item.getWname(), brID)));
                prequery.setString(2, "Take Out");
                prequery.setInt(3, 0);
                prequery.setInt(4, item.getCartonnum());
                prequery.setInt(5, productRemain(productDAO.ProductRemaining(productDAO.getProductTablename(item.getPname(), item.getWname(), brID)), item.getCartonnum()));
                System.out.println(prequery);
                if (prequery.executeUpdate() == 1) {
                    System.out.println("succesful"); 
                    status = true;
                }         
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
        return status;
    }

    @Override
    public Boolean updateStockAccount(ObservableList<WarehouseOrder> stock, Integer invoice, Integer brID) {
        Boolean status = false;
        for (WarehouseOrder item : stock) {
            try {
                String saveQuery = "insert into " + productDAO.getStockTablename(productDAO.getStockID(item.getPname(), brID), brID) + "(STId,status,invoicenumber,instock,outstock,remstock) values (?,?,?,?,?,?)";
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, productDAO.getStockID(item.getPname(), brID));
                prequery.setString(2, "New Stock");
                prequery.setInt(3, invoice);
                prequery.setInt(4, item.getCartonnum());
                prequery.setInt(5, 0);
                prequery.setInt(6, StockRemain(productDAO.ProductRemaining(productDAO.getStockTablename(productDAO.getStockID(item.getPname(), brID), brID)), item.getCartonnum()));
                System.out.println(prequery);
                if (prequery.executeUpdate() == 1) {
                    System.out.println("succesful"); 
                    status = true;
                }         
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
        return status;
    }

    @Override
    public Boolean deductStockAccount(ObservableList<Invoice> stock, Integer invoice, Integer brID) {
        Boolean status = false;
        for (Invoice item : stock) {
            try {
                String saveQuery = "insert into " + productDAO.getStockTablename(productDAO.getStockID(item.getProductname(), brID), brID) + "(STId,status,invoicenumber,instock,outstock,remstock) values (?,?,?,?,?,?)";
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, productDAO.getStockID(item.getProductname(), brID));
                prequery.setString(2, "Sold");
                prequery.setInt(3, invoice);
                prequery.setInt(4, 0);
                prequery.setInt(5, item.getQuantity());
                prequery.setInt(6, productRemain(productDAO.ProductRemaining(productDAO.getStockTablename(productDAO.getStockID(item.getProductname(), brID), brID)), item.getQuantity()));
                System.out.println(prequery);
                if (prequery.executeUpdate() == 1) {
                    System.out.println("succesful"); 
                    status = true;
                }         
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
        return status;
    }

   
}
