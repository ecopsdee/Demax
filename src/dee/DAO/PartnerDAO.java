
package dee.DAO;

import dee.database.deeinventory;
import dee.models.Partner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class PartnerDAO {
    
    deeinventory DbSet = deeinventory.getinstance();   
    CustomerDAO customerDAO = new CustomerDAO();        
    ProductDAO productDAO = new ProductDAO();
    
    public Boolean SendOrder(Partner partner, Integer invoice, Integer brID){
        Boolean status = false;
          try {
            String saveQuery = "insert into OrderDetailNP(BRId,InvoiceNumber,CustId,OrderStatus,StatusComplete) values (?,?,?,?,?)";
            PreparedStatement saveStat = DbSet.multiQuery(saveQuery);
            saveStat.setInt(1, brID);
            saveStat.setInt(2, invoice);
            saveStat.setInt(3, partner.getPartnerid());
            saveStat.setString(4, partner.getOrderstatus());
            saveStat.setString(5, partner.getStatuscomplete());
            System.out.println(saveStat);
            int save = saveStat.executeUpdate();
            if (save == 1) {
                System.out.println("Order sent!");
                status = true;
            }
        } catch (SQLException ex) {
            status = false;
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean DeleteSentOrder(Partner partner, Integer invoice, Integer brID){
        Boolean status = false;
        try {
            String saveQuery = "insert into OrderDetailNPDelete(BRId,InvoiceNumber,CustId,OrderStatus,StatusComplete) values (?,?,?,?,?)";
            PreparedStatement saveStat = DbSet.multiQuery(saveQuery);
            saveStat.setInt(1, brID);
            saveStat.setInt(2, invoice);
            saveStat.setInt(3, partner.getPartnerid());
            saveStat.setString(4, partner.getOrderstatus());
            saveStat.setString(5, partner.getStatuscomplete());
            System.out.println(saveStat);
            int save = saveStat.executeUpdate();
            if (save == 1) {
                System.out.println("Order sent!");
                status = true;
            }
        } catch (SQLException ex) {
            status = false;
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean DeletePermSentOrder(Integer invoice, Integer brID){
        Boolean status = false;
          try {
            String deletequery = "delete from OrderDetailNP where InvoiceNumber = '" + invoice +"' and BRId = '" + brID + "'";
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            System.out.println(predelete);
            if (predelete.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
     
    public Boolean SaveOrder(Partner partner, Integer invoice, Integer brID){
        Boolean status = false;
          try {
            String saveQuery = "insert into OrderDetail(BRId,InvoiceNumber,CustId,OrderStatus,StatusComplete) values (?,?,?,?,?)";
            PreparedStatement saveStat = DbSet.multiQuery(saveQuery);
            saveStat.setInt(1, brID);
            saveStat.setInt(2, invoice);
            saveStat.setInt(3, partner.getPartnerid());
            saveStat.setString(4, partner.getOrderstatus());
            saveStat.setString(5, partner.getStatuscomplete());
            System.out.println(saveStat);
            int save = saveStat.executeUpdate();
            if (save == 1) {
                System.out.println("Order sent!");
                status = true;
            }
        } catch (SQLException ex) {
            status = false;
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean SendCreditGoods(ObservableList<Partner> partner, Integer brID, Integer invoice,Integer custID){
        Boolean status = false;
        String querycreate =  "insert into CreditGoodNP(BRId,InvoiceNumber,CustId,STId,Qty,UnitPrice,TotalPrice) values(?,?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
        for (Partner item : partner) {
            try {
                prequery.setInt(1, brID);
                prequery.setInt(2, invoice);
                prequery.setInt(3, custID);
                prequery.setInt(4, productDAO.getStockID(item.getStockname(), brID));
                prequery.setInt(5, item.getNumberofgoods());
                prequery.setInt(6, item.getUnitprice());
                prequery.setInt(7, item.getTotalprice());
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
       return status;
    }
    
    public Boolean DeleteSentCreditGoods(ObservableList<Partner> partner, Integer brID, Integer invoice, Integer custID){
        Boolean status = false;
        String querycreate =  "insert into CreditGoodNPDelete(BRId,InvoiceNumber,CustId,STId,Qty,UnitPrice,TotalPrice) values(?,?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
        for (Partner item : partner) {
            try {
                prequery.setInt(1, brID);
                prequery.setInt(2, invoice);
                prequery.setInt(3, custID);
                prequery.setInt(4, productDAO.getStockID(item.getStockname(), brID));
                prequery.setInt(5, item.getNumberofgoods());
                prequery.setInt(6, item.getUnitprice());
                prequery.setInt(7, item.getTotalprice());
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
       return status;
    }
    
    public Boolean DeletePermSentCreditGoods(Integer invoice, Integer brID  ){
        Boolean status = false;
          try {
            String deletequery = "delete from CreditGoodNP where InvoiceNumber = '" + invoice +"' and BRId = '" + brID + "'";
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            System.out.println(predelete);
            System.out.println("the update here is " + predelete.executeUpdate() );
            predelete.executeUpdate(); 
            status = true;
            System.out.println("the update here is " );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean SaveCreditGoods(ObservableList<Partner> partner, Integer brID, Integer invoice, Integer custID){
        Boolean status = false;
        String querycreate =  "insert into CreditGood(BRId,InvoiceNumber,CustId,STId,Qty,UnitPrice,TotalPrice) values(?,?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
        for (Partner item : partner) {
            try {
                prequery.setInt(1, brID);
                prequery.setInt(2, invoice);
                prequery.setInt(3, custID);
                prequery.setInt(4, productDAO.getStockID(item.getStockname(), brID));
                prequery.setInt(5, item.getNumberofgoods());
                prequery.setInt(6, item.getUnitprice());
                prequery.setInt(7, item.getTotalprice());
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();
            }
        }
       return status;
    }
    
    public Boolean SendPartnerTransact(Partner partner, Integer invoice, Integer custID, Integer brID){
        Boolean status = false;
        String querycreate =  "insert into PartnerTransactNP(BRId,InvoiceNumber,CustId,Qty,TotalPrice,AmountPaid,Balance,UpdateCount) values(?,?,?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
            try {
                prequery.setInt(1, brID);
                prequery.setInt(2, invoice);
                prequery.setInt(3, custID);
                prequery.setInt(4, partner.getTotalNumberofGood());
                prequery.setInt(5, partner.getTotalPrice());
                prequery.setInt(6, partner.getAmountpaid());
                prequery.setInt(7, partner.getBalance());
                prequery.setInt(8, partner.getOrderid());
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    System.out.println("PartnerTransact sent!");
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();              
            }
        return status;
    }
    
    public Boolean DeleteSentPartnerTransact(Partner partner, Integer invoice, Integer custID, Integer brID){
        Boolean status = false;
        String querycreate =  "insert into PartnerTransactNPDelete(BRId,InvoiceNumber,CustId,Qty,TotalPrice,AmountPaid,Balance,UpdateCount) values(?,?,?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
            try {
                prequery.setInt(1, brID);
                prequery.setInt(2, invoice);
                prequery.setInt(3, custID);
                prequery.setInt(4, partner.getTotalNumberofGood());
                prequery.setInt(5, partner.getTotalPrice());
                prequery.setInt(6, partner.getAmountpaid());
                prequery.setInt(7, partner.getBalance());
                prequery.setInt(8, partner.getOrderid());
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    System.out.println("PartnerTransact sent!");
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();              
            }
        return status;
    }
    
    public Boolean DeletePermSentPartnerTransact(Integer invoice, Integer brID){
        Boolean status = false;
          try {
            String deletequery = "delete from PartnerTransactNP where InvoiceNumber = '" + invoice +"' and BRId = '" + brID + "'";
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            if (predelete.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean SavePartnerTransact(Partner partner, Integer invoice, Integer custID, Integer brId){
        Boolean status = false;
        String querycreate =  "insert into PartnerTransact(BRId,InvoiceNumber,CustId,Qty,TotalPrice,AmountPaid,Balance,UpdateCount) values(?,?,?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
            try {
                prequery.setInt(1, brId);
                prequery.setInt(2, invoice);
                prequery.setInt(3, custID);
                prequery.setInt(4, partner.getTotalNumberofGood());
                prequery.setInt(5, partner.getTotalPrice());
                prequery.setInt(6, partner.getAmountpaid());
                prequery.setInt(7, partner.getBalance());
                prequery.setInt(8, partner.getOrderid());
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    System.out.println("PartnerTransact sent!");
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();              
            }
        return status;
    }
    
    public Boolean SavePartnerBalanceSheet(Partner partner, String account){
        Boolean status = false;
        String querycreate =  "insert into " + account +"(Payment,Charges,Balance) values(?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
            try {
                prequery.setInt(1, partner.getAmountpaid());
                prequery.setInt(2, partner.getTotalPrice()); 
                prequery.setInt(3, Balance(BalanceRemaining(account), partner.getTotalPrice(),partner.getAmountpaid() ));
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
    
    public Boolean SaveUpdateBalanceSheetOnline(Integer amount, String account){
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
    
    public Boolean PopulatePartnerBalanceSheet(String account){
        Boolean status = false;
        String querycreate =  "insert into " + account +"(Payment,Charges,Balance) values(?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
            try {
                prequery.setInt(1, 0);
                prequery.setInt(2, 0);
                prequery.setInt(3, 0);
                System.out.println(prequery);
                int success = prequery.executeUpdate();
                if (success == 1) {
                    System.out.println("Balance Sheet populated!");
                    status = true;
                }
            } catch (SQLException e) {
                status = false;
                e.printStackTrace();              
            }
        return status;
    }
    
    //remember to also save in the customer table first
    public Boolean SaveDistributor(Partner partner){
        Boolean status = false;
        try {
            if (checkPartnerID(partner.getPartnerid(), partner.getOrderid())) {
                
            }else{
                String saveQuery = "insert into PartnerAccount(BRId,CustId,AccountNumber,BalanceSheet) values (?,?,?,?)";
                PreparedStatement saveStat = DbSet.multiQuery(saveQuery);
                saveStat.setInt(1, partner.getOrderid());
                saveStat.setInt(2, partner.getPartnerid());
                saveStat.setString(3, partner.getOrderstatus());
                saveStat.setString(4, partner.getStatuscomplete());
                System.out.println(saveStat);
                int save = saveStat.executeUpdate();
                if (save == 1) {
                    System.out.println("Partner created");
                    status = true;
                }
            }
        } catch (SQLException ex) {
            status = false;
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean checkPartnerID(Integer partnerid, Integer brID){
        Boolean status = false;
        try {
            String checkpartnerid = "SELECT CustId FROM PartnerAccount WHERE CustId = '" + partnerid + "'and BRId = '" + brID + "'";      
            System.out.println(checkpartnerid);
            PreparedStatement checkuname = DbSet.multiQuery(checkpartnerid);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if( partnerid == (retrieve.getInt("CustId"))){
                   status = true;
               }
            }  
        } catch (Exception ex) {
            status = false;
           ex.printStackTrace();
         
        }
        return status;
    }
    
    public Boolean updatePartnerAccount(ObservableList<Partner> partner, Integer brID, Integer invoice, Integer custID, String detail, String summary){
        Boolean status = false;
        for (Partner item : partner) {
            try {
                String saveQuery = "insert into " + getPartnerTablename(custID, brID) + "(status,TransactionSummary,invoicenumber,STId,Qty,UnitPrice,TotalPrice) values (?,?,?,?,?,?,?)";
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setString(1, detail);
                prequery.setString(2, summary);
                prequery.setInt(3, invoice);
                prequery.setInt(4, productDAO.getStockID(item.getStockname(), brID));
                prequery.setInt(5, item.getNumberofgoods());
                prequery.setInt(6, item.getUnitprice());
                prequery.setInt(7, item.getTotalprice());
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
    
    public Boolean checkAccount(String account, Integer brID){
        Boolean status = false;
        try {
            String checkusername = "SELECT AccountNumber FROM PartnerAccount WHERE AccountNumber = '" + account + "' and BRId = '" + brID + "'";
            System.out.println(checkusername);
            PreparedStatement checkuname = DbSet.multiQuery(checkusername);
            ResultSet retrieve = checkuname.executeQuery();
            while(retrieve.next()){
               if(retrieve.getString("AccountNumber")!= null){
                   status = true;
               }
            }  
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        System.out.println(status);
        
        return status; 
    }
    
    public String getPartnerTablename(Integer custID, Integer brID) {
        String ID = "";
        try {
            String proquery = "select AccountNumber from PartnerAccount where CustId = '" + custID + "' and BRId = '" + brID + "'";
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
      
    public String getPartnerBalanceSheet(Integer custID, Integer brID) {
        String ID = "";
        try {
            String proquery = "select BalanceSheet from PartnerAccount where CustId = '" + custID + "' and BRId = '" + brID + "'";
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
    
    public String getBalanceSheet(String account, Integer brID) {
        String ID = "";
        try {
            String proquery = "select BalanceSheet from PartnerAccount where AccountNumber = '" + account + "' and BRId = '" + brID + "'";
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
    
    public String getPartnerName(String account, Integer brID) {
        String ID = "";
        try {
            String proquery = "select CustId from PartnerAccount where BalanceSheet = '" + account + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
               ID = customerDAO.getcustomername(prostat.getInt("CustId"), brID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ID;
    }
     
    public Partner getBalanceDetails(Integer invoice, Integer brID){
        Integer TNOG = 0; Integer TP = 0; Integer AP = 0; Integer Balance = 0; String date = ""; Integer qty = 0;
        try {
            String detquery = "select * from PartnerTransactNP where InvoiceNumber = '" + invoice +"' and BRId = '" + brID + "'";
            System.out.println(detquery);
            PreparedStatement predet = DbSet.multiQuery(detquery);
            ResultSet details = predet.executeQuery();
            while (details.next()) {  
                date = details.getString("issueTime");
                TNOG = details.getInt("CustId");
                qty = details.getInt("Qty");
                TP = details.getInt("TotalPrice");
                AP  = details.getInt("AmountPaid");
                Balance = details.getInt("Balance");
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Partner(TNOG, date, String.valueOf(AP), TP, Balance,qty);
    }
    
    public Partner getOrderDetails(Integer invoice, Integer brID){
        String orderstatus = ""; String ordercomplete = "";
        try {
            String detquery = "select OrderStatus, StatusComplete from OrderDetailNP where InvoiceNumber = '" + invoice +"' and BRId = '" + brID + "'";
            System.out.println(detquery);
            PreparedStatement predet = DbSet.multiQuery(detquery);
            ResultSet details = predet.executeQuery();
            while (details.next()) {  
                orderstatus = details.getString("OrderStatus");
                ordercomplete = details.getString("StatusComplete");
                
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Partner(0, orderstatus, ordercomplete);
    }
    
    public ObservableList<Partner> getPartnerList(String status, Integer brID){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
        Integer count = 1;
        try {
            String proquery = "select CustId,Name,Location from Customer where Status = '" + status + "' and BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                Integer proname = prostat.getInt("CustId");
                String profirst = prostat.getString("Name");
                String prolast = prostat.getString("Location");
                prolist.add(new Partner(count, profirst, proname, prolast));
                count++;
                System.out.println(prolist);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Partner> getPartnerInvoiceDetails(Integer invoice, Integer brID){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
        try {
            Integer count = 1;              
                String proquery = "select * from CreditGoodNP where InvoiceNumber = '" + invoice + "' and BRId = '" + brID + "'";
                System.out.println(proquery);
                PreparedStatement propre = DbSet.multiQuery(proquery);
                ResultSet prostat = propre.executeQuery();  
                while (prostat.next()) {
                    String stockNAME = productDAO.getStockName(prostat.getInt("STId"), brID);
                    Integer QTY = prostat.getInt("Qty");
                    Integer UNITPRICE = prostat.getInt("UnitPrice");
                    Integer TOTALPRICE = prostat.getInt("TotalPrice");
                    prolist.add(new Partner(0,count, stockNAME, QTY, UNITPRICE, TOTALPRICE));
                    count++;
                    System.out.println(prolist);
                }
                
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return prolist;
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
    
    public Integer Balance(Integer goodRem, Integer goodQty, Integer good) {
        Integer ID = goodRem + goodQty - good;
        return ID;
    }
    
    public ObservableList<Partner> getPartnerAccountList(Integer brID){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select CustId, AccountNumber from PartnerAccount where  BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String name = customerDAO.getcustomername(prostat.getInt("CustId"), brID);
                String ID = prostat.getString("AccountNumber");
                prolist.add(new Partner(0, name, 0, ID));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Partner> getPartnerInvoice(String account, Integer brID){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + " order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                String status = statsto.getString("status");
                Integer invoice = statsto.getInt("invoicenumber");
                String stock = productDAO.getStockName(statsto.getInt("STId"), brID);
                Integer qty = statsto.getInt("Qty");
                Integer uprice = statsto.getInt("UnitPrice");
                Integer totprice = statsto.getInt("TotalPrice");
                prolist.add(new Partner(invoice, stock, qty, uprice, totprice, status, issuedate.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
     
    public ObservableList<Partner> getPartnerInvoice(String account, Integer brID, String Date, String Date2){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + " where DATE(issueTime) >= '" + Date +"' and DATE(issueTime) <= '" + Date2 +  "' order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                String status = statsto.getString("status");
                Integer invoice = statsto.getInt("invoicenumber");
                String stock = productDAO.getStockName(statsto.getInt("STId"), brID);
                Integer qty = statsto.getInt("Qty");
                Integer uprice = statsto.getInt("UnitPrice");
                Integer totprice = statsto.getInt("TotalPrice");
                prolist.add(new Partner(invoice, stock, qty, uprice, totprice, status, issuedate.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Partner> getPartnerInvoice(String account, Integer brID, String Date){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
        try {
            String queryretrieve = "select * from " + account + " where DATE(issueTime) = '" + Date +"' order by id desc " ;
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                Timestamp issuedate = statsto.getTimestamp("issueTime");
                String status = statsto.getString("status");
                Integer invoice = statsto.getInt("invoicenumber");
                String stock = productDAO.getStockName(statsto.getInt("STId"), brID);
                Integer qty = statsto.getInt("Qty");
                Integer uprice = statsto.getInt("UnitPrice");
                Integer totprice = statsto.getInt("TotalPrice");
                prolist.add(new Partner(invoice, stock, qty, uprice, totprice, status, issuedate.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Partner> getPartnerBalanceSheet(String account){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
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
                prolist.add(new Partner(0, "", qty, uprice, totprice, "", issuedate.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Partner> getPartnerBalanceSheet(String account, String Date, String Date2){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
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
                prolist.add(new Partner(0, "", qty, uprice, totprice, "", issuedate.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Partner> getPartnerBalanceSheet(String account, String Date){
        ObservableList<Partner> prolist = FXCollections.observableArrayList();
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
                prolist.add(new Partner(0, "", qty, uprice, totprice, "", issuedate.toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prolist;
    }
    
    public ObservableList<Partner> getPartnerSummary(Integer brID){
        ObservableList<Partner> prolist = FXCollections.observableArrayList(); Integer count = 1;
        try {
            String proquery = "select CustId, BalanceSheet from PartnerAccount where  BRId = '" + brID + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                Integer custID = prostat.getInt("CustId");
                String name = customerDAO.getcustomername(prostat.getInt("CustId"), brID);
                Integer balance = BalanceRemaining(prostat.getString("BalanceSheet"));
                prolist.add(new Partner(custID, balance, name, count));
                count++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return prolist;
    }
    
    
}
