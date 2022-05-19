
package dee.DAO;

import dee.database.deeinventory;
import dee.models.Branch;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class BranchDAO {
    
    //note: the STID would have been used instead of PRID...it is not important since the PRID in this case points to the Stock table and not the product table
    
    deeinventory DbSet = deeinventory.getinstance();
    ProductDAO productDAO = new ProductDAO();
    
    public Boolean createBranch(ObservableList<Branch> branch,Integer BRID) {
        Boolean status = false;   
        String querycreate =  "insert into SubBranch(BRId,SBRId,name,account,BalanceSheet,otherinfo) values(?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
        for (Branch _item : branch) {
            try {
                if (checkBranch(_item.getBranchname(), BRID)) {
                    
                }else{
                    if (DbSet.setupBranchAccount(_item.getAccount()) && DbSet.setupBranchBalanceSheet(_item.getPname())) {
                        if (populatenewBranch(_item.getAccount(), _item.getQty(), BRID) && populatenewBalanceSheet(_item.getPname(), _item.getQty(), BRID)) {
                            prequery.setInt(1, BRID);
                            prequery.setInt(2, _item.getQty());
                            prequery.setString(3, _item.getBranchname());
                            prequery.setString(4, _item.getAccount());
                            prequery.setString(5, _item.getPname());
                            prequery.setString(6, _item.getOtherinfo());
                            if (prequery.executeUpdate() == 1) {
                                status = true;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return status;
        
    }
    
    public boolean checkBranchID(Integer SBID) {
        boolean user = false;
        try {
            String checkusername = "SELECT * FROM SubBranch WHERE SBRId = '" + SBID + "'";     
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if( SBID == retrieve.getInt("SBRId")){
                   user = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return user;
    }
    
    public Boolean checkBranch(String branchname,  Integer brId) {
        Boolean status = false;
        try {
            String checkbranch = "SELECT SBRId FROM SubBranch WHERE name = '" + branchname + "' and BRId = '" + brId + "'";      
            PreparedStatement checkuname = DbSet.multiQuery(checkbranch);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("SBRId")!= null){
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean checkBalanceSheetAccount(String account, Integer brID){
        Boolean status = false;
        try {
            String checkusername = "SELECT BalanceSheet FROM SubBranch WHERE BalanceSheet = '" + account + "' and BRId = '" + brID + "'";
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("BalanceSheet")!= null){
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(status);
        
        return status; 
    }
    
    public Boolean checkBranchAccount(String account, Integer brID){
        Boolean status = false;
        try {
            String checkusername = "SELECT account FROM SubBranch WHERE account = '" + account + "' and BRId = '" + brID + "'";
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("account")!= null){
                   System.out.println(retrieve.getString("account"));
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(status);
        
        return status; 
    }
    
    public Boolean checkBranchStock(Integer stID, Integer brID, Integer sbrID){
        Boolean status = false;
        try {
            String checkusername = "SELECT STId FROM BranchStock WHERE STId = '" + stID + "' and BRId = '" + brID + "' and SBRId = '" + sbrID +"'";
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("STId")!= null){
                   System.out.println(retrieve.getString("STId"));
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(status);
        
        return status; 
    }
    
    public Boolean deductStockAccount(ObservableList<Branch> stock, Integer invoice, Integer brID) {
        Boolean status = false;
        for (Branch item : stock) {
            try {
                String saveQuery = "insert into " + productDAO.getStockTablename(productDAO.getStockID(item.getPname(), brID), brID) + "(STId,status,invoicenumber,instock,outstock,remstock) values (?,?,?,?,?,?)";
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, productDAO.getStockID(item.getPname(), brID));
                prequery.setString(2, "Sent to Branch");
                prequery.setInt(3, invoice);
                prequery.setInt(4, 0);
                prequery.setInt(5, item.getQty());
                prequery.setInt(6, productRemain(productDAO.ProductRemaining(productDAO.getStockTablename(productDAO.getStockID(item.getPname(), brID), brID)), item.getQty()));
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }         
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
        return status;
    }
    
    public Boolean populateBranchAccount(ObservableList<Branch> stock,Integer subID, Integer brID) {
        Boolean status = false;
        for (Branch item : stock) {
            try {
                if (checkBranchStock(productDAO.getStockID(item.getPname(), brID), brID, subID)) {
                    if (populateBranchStock(getBranchStockAccount(subID, brID, productDAO.getStockID(item.getPname(), brID)), productDAO.getStockID(item.getPname(), brID), item.getQty())) {
                            String saveQuery = "insert into " + getBranchAccount(subID, brID) + "(PRId,Qty,UnitPrice,TotalPrice) values (?,?,?,?)";
                            PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                            prequery.setInt(1, productDAO.getStockID(item.getPname(), brID));
                            prequery.setInt(2, item.getQty());
                            prequery.setInt(3, item.getUprice());
                            prequery.setInt(4, item.getTprice());
                            if (prequery.executeUpdate() == 1) {
                                status = true;
                            }
                        }
                }else{
                    String table = generateBranchStockAccount();
                    if (DbSet.setupBranchStockTable(table) && createBranchStock(brID, subID, productDAO.getStockID(item.getPname(), brID), table) && populatenewBranchStock(table)) {
                        if (populateBranchStock(table, productDAO.getStockID(item.getPname(), brID), item.getQty())) {
                            String saveQuery = "insert into " + getBranchAccount(subID, brID) + "(PRId,Qty,UnitPrice,TotalPrice) values (?,?,?,?)";
                            PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                            prequery.setInt(1, productDAO.getStockID(item.getPname(), brID));
                            prequery.setInt(2, item.getQty());
                            prequery.setInt(3, item.getUprice());
                            prequery.setInt(4, item.getTprice());
                            if (prequery.executeUpdate() == 1) {
                                status = true;
                            }
                        }
                    }
                    
                }                     
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
        return status;
    }
    
    public Boolean TransferBranchStock(ObservableList<Branch> stock, Integer subID1, Integer subID2, Integer brID){
        Boolean status = false;
        for (Branch item : stock) {
            try {
                if (subID2 != 0) {
                    if (checkBranchStock(productDAO.getStockID(item.getPname(), brID), brID, subID2)) {
                        if (deducteBranchStock(item.getPname(), item.getQty(), subID1, brID, getBranchName(subID2, brID)) && populateBranchStock(item.getPname(), item.getQty(), subID2, brID, getBranchName(subID1, brID))   ) {
                            String save = "insert into Transfer(BRId, FromSBRId, ToSBRId, STId, Status, Qty) values (?,?,?,?,?,?)";
                            PreparedStatement prequery = DbSet.multiQuery(save);
                            prequery.setInt(1, brID);
                            prequery.setInt(2, subID1);
                            prequery.setInt(3, subID2);
                            prequery.setInt(4, productDAO.getStockID(item.getPname(), brID));
                            prequery.setString(5, "Transfer of Stock");
                            prequery.setInt(6, item.getQty());
                            if (prequery.executeUpdate() == 1) {
                                status = true;
                            }
                        }
                    }else{
                        String table = generateBranchStockAccount();
                        if (DbSet.setupBranchStockTable(table) && createBranchStock(brID, subID2, productDAO.getStockID(item.getPname(), brID), table) && populatenewBranchStock(table)) {
                            if (deducteBranchStock(item.getPname(), item.getQty(), subID1, brID, getBranchName(subID2, brID)) && populateBranchStock(item.getPname(), item.getQty(), subID2, brID, getBranchName(subID1, brID))   ) {
                                String save = "insert into Transfer(BRId, FromSBRId, ToSBRId, STId, Status, Qty) values (?,?,?,?,?,?)";
                                PreparedStatement prequery = DbSet.multiQuery(save);
                                prequery.setInt(1, brID);
                                prequery.setInt(2, subID1);
                                prequery.setInt(3, subID2);
                                prequery.setInt(4, productDAO.getStockID(item.getPname(), brID));
                                prequery.setString(5, "Transfer of Stock");
                                prequery.setInt(6, item.getQty());
                                if (prequery.executeUpdate() == 1) {
                                    status = true;
                                }
                            }
                        }
                    
                    }
                }else{
                    if (deducteBranchStock(item.getPname(), item.getQty(), subID1, brID, "Head Office")  && populateBranchStock(item.getPname(), item.getQty(), brID, getBranchName(subID1, brID))  ) {
                        String save = "insert into Transfer(BRId, FromSBRId, ToSBRId, STId, Status, Qty) values (?,?,?,?,?,?)";
                        PreparedStatement prequery = DbSet.multiQuery(save);
                        prequery.setInt(1, brID);
                        prequery.setInt(2, subID1);
                        prequery.setInt(3, subID2);
                        prequery.setInt(4, productDAO.getStockID(item.getPname(), brID));
                        prequery.setString(5, "Transfer of Stock");
                        prequery.setInt(6, item.getQty());
                        if (prequery.executeUpdate() == 1) {
                            status = true;
                        }
                    }
                }
                
                
                
                

            } catch (Exception e) {
            }
        }
        return status;
    }
    
    public Boolean createBranchStock(Integer brID, Integer sbrID, Integer stID, String account){
        Boolean status = false;
        try {
            
            String querycreate =  "insert into BranchStock(BRId,SBRId,STId,account) values(?,?,?,?)";
            PreparedStatement prequery = DbSet.multiQuery(querycreate);
            prequery.setInt(1, brID);
            prequery.setInt(2, sbrID);
            prequery.setInt(3, stID);
            prequery.setString(4, account);
            if (prequery.executeUpdate() == 1) {
                status = true;
            }
           
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean populateBranchStock(String account, Integer stID, Integer qty) {
        Boolean status = false;
            try {
                String saveQuery = "insert into " + account + "(STId,status,invoicenumber,instock, outstock, remstock) values (?,?,?,?,?,?)";
                System.out.println(saveQuery);
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, stID);
                prequery.setString(2, "New Stock");
                prequery.setInt(3, 0);
                prequery.setInt(4, qty);
                prequery.setInt(5, 0);
                prequery.setInt(6, Balance(BranchStockRemain(account), qty, 0));
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }         
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        
        return status;
    }
    
    public Boolean populatenewBranchStock(String account) {
        Boolean status = false;
            try {
                String saveQuery = "insert into " + account + "(STId,status,invoicenumber,instock, outstock, remstock) values (?,?,?,?,?,?)";
                System.out.println(saveQuery);
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, 0);
                prequery.setString(2, "Account Created");
                prequery.setInt(3, 0);
                prequery.setInt(4, 0);
                prequery.setInt(5, 0);
                prequery.setInt(6, 0);
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }         
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        
        return status;
    }
    
    public Boolean populateStockSale(ObservableList<Branch> stock,Integer subID, Integer brID) {
        Boolean status = false;
        for (Branch item : stock) {
            try {
                    String saveQuery = "insert into " + getBranchStockAccount(subID, brID, productDAO.getStockID(item.getPname(), brID) ) + "(STId,status,invoicenumber,instock, outstock, remstock) values (?,?,?,?,?,?)";
                    PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                    prequery.setInt(1, productDAO.getStockID(item.getPname(), brID));
                    prequery.setString(2, "Sales");
                    prequery.setInt(3, 0);
                    prequery.setInt(4, 0);
                    prequery.setInt(5, item.getQty());
                    prequery.setInt(6, productRemain(BranchStockRemain(getBranchStockAccount(subID, brID, productDAO.getStockID(item.getPname(), brID) )), item.getQty()));
                    if (prequery.executeUpdate() == 1) {
                        status = true;
                    }    
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
        return status;
    }
    
    public Boolean deducteBranchStock(String pname, Integer qty ,Integer subID, Integer brID, String branchname) {
        Boolean status = false;
            try {
                    String saveQuery = "insert into " + getBranchStockAccount(subID, brID, productDAO.getStockID(pname, brID) ) + "(STId,status,invoicenumber,instock, outstock, remstock) values (?,?,?,?,?,?)";
                    PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                    prequery.setInt(1, productDAO.getStockID(pname, brID));
                    prequery.setString(2, "Sent to Branch " + branchname );
                    prequery.setInt(3, 0);
                    prequery.setInt(4, 0);
                    prequery.setInt(5, qty);
                    prequery.setInt(6, productRemain(BranchStockRemain(getBranchStockAccount(subID, brID, productDAO.getStockID(pname, brID) )), qty));
                    if (prequery.executeUpdate() == 1) {
                        status = true;
                    }    
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        return status;
    }

    public Boolean populateBranchStock(String pname, Integer qty ,Integer subID, Integer brID, String branchname) {
        Boolean status = false;
            try {
                    String saveQuery = "insert into " + getBranchStockAccount(subID, brID, productDAO.getStockID(pname, brID) ) + "(STId,status,invoicenumber,instock, outstock, remstock) values (?,?,?,?,?,?)";
                    PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                    prequery.setInt(1, productDAO.getStockID(pname, brID));
                    prequery.setString(2, "Received from Branch " + branchname );
                    prequery.setInt(3, 0);
                    prequery.setInt(4, qty);
                    prequery.setInt(5, 0);
                    prequery.setInt(6, Balance(BranchStockRemain(getBranchStockAccount(subID, brID, productDAO.getStockID(pname, brID) )), qty, 0));
                    if (prequery.executeUpdate() == 1) {
                        status = true;
                    }    
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        return status;
    }

    public Boolean populateBranchStock(String pname, Integer qty , Integer brID, String branchname) {
        Boolean status = false;
            try {
                    String saveQuery = "insert into " + productDAO.getStockTablename(productDAO.getStockID(pname, brID), brID) + "(STId,status,invoicenumber,instock, outstock, remstock) values (?,?,?,?,?,?)";
                    PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                    prequery.setInt(1, productDAO.getStockID(pname, brID));
                    prequery.setString(2, "Received from Branch " + branchname );
                    prequery.setInt(3, 0);
                    prequery.setInt(4, qty);
                    prequery.setInt(5, 0);
                    prequery.setInt(6, Balance(productDAO.ProductRemaining(productDAO.getStockTablename(productDAO.getStockID(pname, brID), brID)), qty, 0));
                    if (prequery.executeUpdate() == 1) {
                        status = true;
                    }    
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        return status;
    }
    
    public Boolean BranchInvoice(ObservableList<Branch> stock, Integer invoicenumber,Integer sbrID, Integer custID, Integer brID){
        Boolean status = false;
        String queryinvoice =  "insert into BranchInvoice(BRId,SBRId,InvoiceNumber,STId,CustId,Qty,UnitPrice,TotalPrice,ReturnStatus,ReturnQty,RemainQty,ReturnTotalPrice) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        for (Branch item : stock) {
            try {
                PreparedStatement preinvoice = DbSet.multiQuery(queryinvoice);
                preinvoice.setInt(1, brID);
                preinvoice.setInt(2, sbrID);
                preinvoice.setInt(3, invoicenumber);
                preinvoice.setInt(4, productDAO.getStockID(item.getPname(), brID));
                preinvoice.setInt(5, custID);
                preinvoice.setInt(6, item.getQty());
                preinvoice.setInt(7, item.getUprice());
                preinvoice.setInt(8, item.getTprice());
                preinvoice.setString(9, "");
                preinvoice.setInt(10, 0);
                preinvoice.setInt(11, item.getQty());
                preinvoice.setInt(12, item.getTprice());
                System.out.println(preinvoice);
                Integer success = preinvoice.executeUpdate();
                if (success == 1) {
                    System.out.println("this is " + success);
                    status = true;
                }
            }catch(SQLException e){
                status = false;
                e.printStackTrace();
            }
        } 
        return status;
    }
    
    public Boolean populatenewBranch(String account, Integer subID, Integer brID) {
        Boolean status = false;
            try {
                String saveQuery = "insert into " + account + "(PRId,Qty,UnitPrice,TotalPrice) values (?,?,?,?)";
                System.out.println(saveQuery);
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, 0);
                prequery.setInt(2, 0);
                prequery.setInt(3, 0);
                prequery.setInt(4, 0);
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }         
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        
        return status;
    }
    
    public Boolean populatenewBalanceSheet(String account, Integer subID, Integer brID) {
        Boolean status = false;
            try {
                String saveQuery = "insert into " + account + "(Payment,Charges,Balance) values (?,?,?)";
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, 0);
                prequery.setInt(2, 0);
                prequery.setInt(3, 0);
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }         
            }catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        
        return status;
    }
    
    public Boolean UpdateBalanceSheet(Integer amount, String account){
        Boolean status = false;
        String querycreate =  "insert into " + account +"(Payment,Charges,Balance) values(?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
            try {
                prequery.setInt(1, 0);
                prequery.setInt(2, amount); 
                prequery.setInt(3, Balance(BalanceRemaining(account), amount, 0 ));
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    System.out.println("Balance Sheet Updated!");
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();              
            }
        return status;
    }
    
    public Boolean SaveUpdateBalanceSheet(Integer amount, String account){
        Boolean status = false;
        String querycreate =  "insert into " + account +"(Payment,Charges,Balance) values(?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
            try {
                prequery.setInt(1, amount);
                prequery.setInt(2, 0); 
                prequery.setInt(3, Balance(BalanceRemaining(account), 0, amount ));
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    System.out.println("Balance Sheet Updated!");
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();              
            }
        return status;
    }
    
    public String getBranchStockAccount(Integer subID, Integer brID, Integer stID) {
        String ID = "";
        try {
            String proquery = "select account from BranchStock where SBRId = '" + subID + "' and BRId = '" + brID + "' and STId = '" + stID +"'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getString("account");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    public String getBranchAccount(Integer subID, Integer brID) {
        String ID = "";
        try {
            String proquery = "select account from SubBranch where SBRId = '" + subID + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getString("account");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    public String getBranchBalanceSheet(Integer subID, Integer brID) {
        String ID = "";
        try {
            String proquery = "select BalanceSheet from SubBranch where SBRId = '" + subID + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getString("BalanceSheet");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    public String getBranchName(Integer subID, Integer brID){
        String ID = "";
        try {
            String proquery = "select name from SubBranch where SBRId = '" + subID + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getString("name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    public String getStockAccount(Integer  brID, Integer subID, Integer stID){
        String ID = "";
        try {
            String proquery = "select account from BranchStock where SBRId = '" + subID + "' and BRId = '" + brID + "' and STId = '" + stID + "'" ;
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getString("account");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    private String generateBranchStockAccount(){
        String value = "509";
        for (int i = 0; i < 7; i++) {
            int number = 1 + (int)(Math.random() * 10);
            value += String.valueOf(number); 
        }
        return  value + "_StockBranch" ;  
    }
    
    public Integer productRemain(Integer goodRem, Integer goodQty) {
        Integer ID = goodRem - goodQty;
        return ID;
    }
    
    public Integer subBranchID(String branch, Integer brID) {
        Integer ID = 0;
        try {
            String proquery = "select SBRId from SubBranch where name = '" + branch + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getInt("SBRId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    public Integer BalanceRemaining(String account) {
        Integer ID = 0;
        try {
            String proquery = "select Balance from " + account + " where id = (select MAX(id) from " + account + ")";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = prostat.getInt("Balance");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
    
    public Integer BranchStockRemain(String account) {
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
    
    public Integer Balance(Integer goodRem, Integer goodQty, Integer good) {
        Integer ID = goodRem + goodQty - good;
        return ID;
    }
    
    public ObservableList<String> getBranch(Integer brID) {
        ObservableList<String> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select name from SubBranch where BRId = '" + brID + "' order by name";
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String warename = prostat.getString("name");
                prolist.add(warename);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return prolist;
    }
    
    public ObservableList<String> getProduct(Integer brID, Integer sbrID) {
        ObservableList<String> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select STId from BranchStock where BRId = '" + brID + "'and SBRId = '" + sbrID + "' order by STId";
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String warename = productDAO.getStockName(prostat.getInt("STId"), brID);
                prolist.add(warename);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return prolist;
    }
    
    public ObservableList<Branch> getBranchAccountDetails(String account, Integer brID, String DateA, String DateB){
        ObservableList<Branch> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + " where DATE(issueTime) >= '" + DateA +"' and DATE(issueTime) <= '" + DateB +  "' order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                String stock = productDAO.getStockName(statsto.getInt("PRId"), brID);
                Integer qty = statsto.getInt("Qty");
                Integer uprice = statsto.getInt("UnitPrice");
                Integer totprice = statsto.getInt("TotalPrice");
                prolist.add(new Branch(issuedate.toString(), stock, qty, uprice, totprice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Branch> getBranchAccountDetails(String account, Integer brID, String DateA){
        ObservableList<Branch> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + " where DATE(issueTime) = '" + DateA +"' order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                String stock = productDAO.getStockName(statsto.getInt("PRId"), brID);
                Integer qty = statsto.getInt("Qty");
                Integer uprice = statsto.getInt("UnitPrice");
                Integer totprice = statsto.getInt("TotalPrice");
                prolist.add(new Branch(issuedate.toString(), stock, qty, uprice, totprice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Branch> getBranchAccountDetails(String account, Integer brID){
        ObservableList<Branch> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + "  order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                String stock = productDAO.getStockName(statsto.getInt("PRId"), brID);
                Integer qty = statsto.getInt("Qty");
                Integer uprice = statsto.getInt("UnitPrice");
                Integer totprice = statsto.getInt("TotalPrice");
                prolist.add(new Branch(issuedate.toString(), stock, qty, uprice, totprice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Branch> getPartnerBalanceSheet(String account){
        ObservableList<Branch> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + " order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                Integer qty = statsto.getInt("Payment");
                Integer uprice = statsto.getInt("Charges");
                Integer totprice = statsto.getInt("Balance");
                prolist.add(new Branch(issuedate.toString(), "", qty, uprice, totprice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Branch> getPartnerBalanceSheet(String account, String Date, String Date2){
        ObservableList<Branch> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + " where DATE(issueTime) >= '" + Date +"' and DATE(issueTime) <= '" + Date2 +  "' order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                Integer qty = statsto.getInt("Payment");
                Integer uprice = statsto.getInt("Charges");
                Integer totprice = statsto.getInt("Balance");
                prolist.add(new Branch(issuedate.toString(), "", qty, uprice, totprice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Branch> getPartnerBalanceSheet(String account, String Date){
        ObservableList<Branch> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + " where DATE(issueTime) = '" + Date +"' order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                Integer qty = statsto.getInt("Payment");
                Integer uprice = statsto.getInt("Charges");
                Integer totprice = statsto.getInt("Balance");
                prolist.add(new Branch(issuedate.toString(), "", qty, uprice, totprice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Branch> getsubbranchdetails(String account, Integer brID){
        ObservableList<Branch> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + "  order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                String status = statsto.getString("status");
                Integer qty = statsto.getInt("instock");
                Integer uprice = statsto.getInt("outstock");
                Integer totprice = statsto.getInt("remstock");
                prolist.add(new Branch(issuedate.toString(), status, qty, uprice, totprice));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Branch> getAllBranchAccount( Integer brID, Integer sbrID){
        ObservableList<Branch> prolist = FXCollections.observableArrayList(); Integer count = 1;
        try {
            String queryretrieve = "select * from branchstock where SBRId = '" + sbrID + "' and BRId = '" + brID + "'" ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                String pname = productDAO.getStockName(statsto.getInt("STId"), brID) ;
                Integer qty = BranchStockRemain(statsto.getString("account")) ;
                prolist.add(new Branch(" ", pname, qty, count, 0));
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    

}
