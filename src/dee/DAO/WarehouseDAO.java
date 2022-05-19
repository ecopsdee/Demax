
package dee.DAO;

import dee.Interface.WarehouseInterface;
import dee.database.deeinventory;
import dee.models.Warehouse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class WarehouseDAO implements WarehouseInterface{
    
    private deeinventory DbSet = deeinventory.getinstance();
    

    @Override
    public Boolean createWarehouse(ObservableList<Warehouse> warehouse, Integer WHID, Integer BRID) {
        Boolean status = false;   
        String querycreate =  "insert into warehouse(WHId,BRId,WHName,Location,Otherinfo) values(?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
        for (Warehouse _item : warehouse) {
            try {
                prequery.setInt(1, _item.getIdcol());
                prequery.setInt(2, BRID);
                prequery.setString(3, _item.getWnamecol());
                prequery.setString(4, _item.getWcitycol());
                prequery.setString(5, _item.getWdescribecol());
                System.out.println(prequery);
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("not successful");
            }
        }
        System.out.println("succesful");
        return status;
        
    }
    
    @Override
    public Boolean checkWarehouseId(Integer whID, Integer brID){
        Boolean status = false;
        try {
            String checkware_id = "SELECT WHId FROM warehouse WHERE WHId = '" + whID + "' and BRId = '" + brID + "'";      
            System.out.println(checkware_id);
            PreparedStatement checkuname = DbSet.multiQuery(checkware_id);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("WHId")!= null){
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
    public Integer getWarehouseId(String warename, Integer brID) {
        Integer ID = 0;
        try {
            String queryid = "select WHId from warehouse where WHName = '" + warename + "'and BRId = '" + brID + "'"; 
            System.out.println(queryid);
            ResultSet getwareid = DbSet.exeQuery(queryid);
            while(getwareid.next()){
                ID = getwareid.getInt("WHId");
            }
            System.out.println("id is " + ID);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }

    @Override
    public ObservableList<String> getWarename(Integer brID) {
        ObservableList<String> warelist = FXCollections.observableArrayList();
        try {
            String warequery = "select WHName from warehouse where BRID = '" + brID + "' order by WHName";
            System.out.println(warequery);
            PreparedStatement warepre = DbSet.multiQuery(warequery);
            ResultSet warestat = warepre.executeQuery();  
            while (warestat.next()) {
                String warename = warestat.getString("WHName");
                warelist.add(warename);
                System.out.println(warelist);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return warelist;
    }

    @Override
    public ObservableList<Warehouse> getWarehouse(Integer brID) {
        ObservableList<Warehouse> warehouselist = FXCollections.observableArrayList(); Integer count = 1;
        try {
            String proquery = "select WHName,Location,Otherinfo from Warehouse where BRID = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String warename = prostat.getString("WHName");
                String warecity = prostat.getString("Location");
                String waredescribe = prostat.getString("Otherinfo");
                warehouselist.add(new Warehouse(count ,warename, "", warecity, waredescribe));
                count++;
                System.out.println(warehouselist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }     
        return warehouselist;
    }

    @Override
    public String warehousename(Integer whID) {
        String name = "";
         try {
            String proquery = "select WHName from Warehouse where WHId = '" + whID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                name = prostat.getString("WHName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return name;
    }

   
    
}
