
package dee.DAO;

import dee.Interface.importInterface;
import dee.database.deeinventory;
import dee.models.Import;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;


public class ImportDAO implements importInterface{
    
    deeinventory DbSet = deeinventory.getinstance();
    ProductDAO productDAO = new ProductDAO();
    WarehouseDAO warehouseDAO = new WarehouseDAO();

    @Override
    public boolean CreateImport(ObservableList<Import> imports, Integer brID) {
        Boolean status = false;
        for (Import item : imports) {
            try {
               System.out.println("this is " + productDAO.getId(productDAO.getProductTablename(item.getProductname(),item.getWarehouse(), brID)));
               String saveimport = "insert into " + productDAO.getProductTablename(item.getProductname(),item.getWarehouse(), brID)  + "(PRId,status,instock,outstock,remstock) values(?,?,?,?,?)";
               PreparedStatement preinsert = DbSet.multiQuery(saveimport);
               preinsert.setInt(1, productDAO.getProductId(item.getProductname(), warehouseDAO.getWarehouseId(item.getWarehouse(), brID)));
               preinsert.setString(2, item.getImportstatus());
               preinsert.setInt(3, item.getTotalqtyofgoods());
               preinsert.setInt(4, 0 );
               preinsert.setInt(5, Total(item.getTotalqtyofgoods(), RemainProduct(productDAO.getProductTablename(item.getProductname(), item.getWarehouse(), brID)))); 
               System.out.println(preinsert);
                if (preinsert.executeUpdate() == 1) {
                    status = true;
                }
               
           } catch (SQLException ex) {
              ex.printStackTrace();
           }
        }
       return status;
    }

    @Override
    public Integer RemainProduct(String account) {
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
    public Integer Total(Integer qty, Integer remain) {
        return  qty + remain;
    }

    
    
}
