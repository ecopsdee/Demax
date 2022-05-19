
package dee.DAO;

import dee.database.deeinventory;
import dee.models.Invoice;
import dee.models.Partner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class InvoiceDAO {
    
    deeinventory DbSet = deeinventory.getinstance();  ProductDAO productDAO = new ProductDAO(); CustomerDAO customerDAO = new CustomerDAO();  
    
    
    public Boolean sendInvoiceNuumber(Integer invoice, String invoiceowner, Integer customerID, Integer totprice, Integer balance, Integer brID){
        Boolean status = false;
        String querycreate =  "insert into InvoiceDetailNP(BRId,InvoiceNumber,Ownership,CustId,TotalPrice,Balance) values(?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
        try {
            prequery.setInt(1, brID);
            prequery.setInt(2, invoice);
            prequery.setString(3, invoiceowner);
            prequery.setInt(4, customerID);
            prequery.setInt(5, totprice);
            prequery.setInt(6, balance);
            System.out.println(prequery);
            int success = prequery.executeUpdate();
            if (success == 1) {
                System.out.println("invoice number saved!");
                status = true;
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
    
    public Boolean DeletesentInvoiceNuumber(Integer invoice, String invoiceowner, Integer customerID, Integer totprice, Integer balance, Integer brID){
        Boolean status = false;
        String querycreate =  "insert into InvoiceDetailNPDelete(BRId,InvoiceNumber,Ownership,CustId,TotalPrice,Balance) values(?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
        try {
            prequery.setInt(1, brID);
            prequery.setInt(2, invoice);
            prequery.setString(3, invoiceowner);
            prequery.setInt(4, customerID);
            prequery.setInt(5, totprice);
            prequery.setInt(6, balance);
            System.out.println(prequery);
            int success = prequery.executeUpdate();
            if (success == 1) {
                System.out.println("invoice number saved!");
                status = true;
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
    
    public Boolean DeletePermSentInvoiceNumber(Integer invoice, Integer brID){
        Boolean status = false;
        try {
            String deletequery = "delete from InvoiceDetailNP where InvoiceNumber = '" + invoice +"' and BRId = '" + brID + "'";
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            if (predelete.executeUpdate() == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean saveInvoiceNuumber(Integer invoice, String invoiceowner, Integer customerID, Integer totprice, Integer balance, Integer brID){
        Boolean status = false;
        String querycreate =  "insert into InvoiceDetail(BRId,InvoiceNumber,Ownership,CustId,TotalPrice,Balance) values(?,?,?,?,?,?)";
        PreparedStatement prequery = DbSet.multiQuery(querycreate);
        try {
            prequery.setInt(1, brID);
            prequery.setInt(2, invoice);
            prequery.setString(3, invoiceowner);
            prequery.setInt(4, customerID);
            prequery.setInt(5, totprice);
            prequery.setInt(6, balance);
            System.out.println(prequery);
            int success = prequery.executeUpdate();
            if (success == 1) {
                System.out.println("invoice number saved!");
                status = true;
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
    
    public Boolean SendInvoice(ObservableList<Invoice> invoice, Integer invoicenumber, Boolean stat,Integer customerID, Integer brID){
        Boolean status = false;
        String queryinvoice =  "insert into InvoiceNP(BRId,InvoiceNumber,STId,CustId,Qty,UnitPrice,TotalPrice) values(?,?,?,?,?,?,?)";
        for (Invoice item : invoice) {
            try {
                PreparedStatement preinvoice = DbSet.multiQuery(queryinvoice);
                preinvoice.setInt(1, brID);
                preinvoice.setInt(2, invoicenumber);
                preinvoice.setInt(3, productDAO.getStockID(item.getProductname(), brID));
                preinvoice.setInt(4, customerID);
                preinvoice.setInt(5, item.getQuantity());
                preinvoice.setInt(6, item.getUnitprice());
                preinvoice.setInt(7, item.getTotalprice());
                System.out.println(preinvoice);
                Integer success = preinvoice.executeUpdate();
                if (success == 1) {
                    status = true;
                    System.out.println("this is " + success);
                }
            }catch(SQLException e){
                status = false;
                e.printStackTrace();
            }
        } 
        return status;
    }
    
    public Boolean DeleteSentInvoice(ObservableList<Invoice> invoice, Integer invoicenumber, Boolean pick,Integer customerID, Integer brID){
        Boolean status = false;
        String queryinvoice =  "insert into InvoiceNPDelete(BRId,InvoiceNumber,STId,CustId,Qty,UnitPrice,TotalPrice) values(?,?,?,?,?,?,?)";
        for (Invoice item : invoice) {
            try {
                PreparedStatement preinvoice = DbSet.multiQuery(queryinvoice);
                preinvoice.setInt(1, brID);
                preinvoice.setInt(2, invoicenumber);
                preinvoice.setInt(3, productDAO.getStockID(item.getProductname(), brID));
                preinvoice.setInt(4, customerID);
                preinvoice.setInt(5, item.getQuantity());
                preinvoice.setInt(6, item.getUnitprice());
                preinvoice.setInt(7, item.getTotalprice());
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
    
    public Boolean DeletePermSentInvoice(Integer invoice, Integer brID){
        Boolean status = false;
          try {
            String deletequery = "delete from InvoiceNP where InvoiceNumber = '" + invoice +"' and BRId = '" + brID + "'";
            System.out.println(deletequery);
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            predelete.executeUpdate();
            status = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean SaveInvoice(ObservableList<Invoice> invoice, Integer invoicenumber, Boolean pick,Integer customerID, Integer brID){
        Boolean status = false;
        String queryinvoice =  "insert into Invoice(BRId,InvoiceNumber,STId,CustId,Qty,UnitPrice,TotalPrice,ReturnStatus,ReturnQty,RemainQty,ReturnTotalPrice) values(?,?,?,?,?,?,?,?,?,?,?)";
        for (Invoice item : invoice) {
            try {
                PreparedStatement preinvoice = DbSet.multiQuery(queryinvoice);
                preinvoice.setInt(1, brID);
                preinvoice.setInt(2, invoicenumber);
                preinvoice.setInt(3, productDAO.getStockID(item.getProductname(), brID));
                preinvoice.setInt(4, customerID);
                preinvoice.setInt(5, item.getQuantity());
                preinvoice.setInt(6, item.getUnitprice());
                preinvoice.setInt(7, item.getTotalprice());
                preinvoice.setString(8, "");
                preinvoice.setInt(9, 0);
                preinvoice.setInt(10, item.getQuantity());
                preinvoice.setInt(11, item.getTotalprice());
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
    
    public Boolean UpdateInvoice(Integer remainqty, Integer returnqty, Integer invoice, Integer total){
       Boolean status = false;
        try {
            String queryupdate = "update Invoice set ReturnQty = '" + returnqty + "', ReturnStatus = 'Return', RemainQty = '" + remainqty + "', ReturnTotalPrice = '" + total+ "' where InvoiceNumber = '" + invoice + "'";
            PreparedStatement predelete = DbSet.multiQuery(queryupdate);
            if (predelete.executeUpdate() == 1) {
                status = true;
            }         
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean checkInvoice(Integer invoice, Integer brID){
        Boolean check = true;
            try {
                String checkinvoice = "SELECT InvoiceNumber FROM InvoiceDetail WHERE InvoiceNumber = '" + invoice + "' and BRId = '" + brID + "'";      
                System.out.println(checkinvoice);
                PreparedStatement checkuname = DbSet.multiQuery(checkinvoice);
                ResultSet retrieve = checkuname.executeQuery();
                while(retrieve.next()){
                   if( invoice != (retrieve.getInt("InvoiceNumber"))){
                       check = false;
                   }
                }  
            } catch (Exception ex) {
            }
        return check;
    }
    
    public Integer RemainQty(Integer brID, Integer invoice, Integer stID, Integer custID){
        Integer qty = 0;
        try {
           String getQty = "select RemainQty from Invoice where InvoiceNumber = '" + invoice +"' and CustId = '" + custID + "' and STId = '" + stID + "' and BRId = '" + brID + "'";
            System.out.println(getQty);
           PreparedStatement preproduct = DbSet.multiQuery(getQty);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
               qty = statproduct.getInt("RemainQty");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return qty;
    }
    
    public Integer ReturnQty(Integer brID, Integer invoice, Integer stID, Integer custID){
        Integer qty = 0;
        try {
           String getQty = "select ReturnQty from Invoice where InvoiceNumber = '" + invoice +"' and CustId = '" + custID + "' and STId = '" + stID + "' and BRId = '" + brID + "'";
           PreparedStatement preproduct = DbSet.multiQuery(getQty);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
               qty = statproduct.getInt("ReturnQty");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return qty;
    }
    
    public Integer Unitprice(Integer brID, Integer invoice, Integer stID, Integer custID){
        Integer qty = 0;
        try {
           String getQty = "select UnitPrice from Invoice where InvoiceNumber = '" + invoice +"' and CustId = '" + custID + "' and STId = '" + stID + "' and BRId = '" + brID + "'";
           PreparedStatement preproduct = DbSet.multiQuery(getQty);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
               qty = statproduct.getInt("UnitPrice");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return qty;
    }
   
    public ObservableList<String> getProduct(Integer invoice, Integer brID) {
        ObservableList<String> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select distinct STId from Invoice where InvoiceNumber = '" + invoice + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String proname = productDAO.getStockName(prostat.getInt("STId"), brID) ;
                prolist.add(proname);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return prolist;
    }
    
    public ObservableList<Integer> InvoiceDetails(String date, Integer brID){
        ObservableList<Integer> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select InvoiceNumber from InvoiceDetail where DATE(issueTime) = '" + date +"' and BRId = '" + brID + "'";
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Integer invoiceNO = statproduct.getInt("InvoiceNumber");
                invoicelist.add(invoiceNO);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return invoicelist;
   
    }
    
    public ObservableList<Integer> getInvoiceTempList(String customer, Integer brID){
        ObservableList<Integer> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select distinct InvoiceNumber from InvoiceDetailNP where Ownership = '" + customer +"' and BRId = '" + brID + "' order by InvoiceNumber ";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Integer invoiceNO = statproduct.getInt("InvoiceNumber");
                invoicelist.add(invoiceNO);
            }
            System.out.println(invoicelist);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return invoicelist;
    }
    
    public ObservableList<Integer> getInvoicePartnerTempList(String partner, Integer brID){
        ObservableList<Integer> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select distinct InvoiceNumber from InvoiceDetailNP where Ownership = '" + partner +"'and BRId = '" + brID + "' order by InvoiceNumber ";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Integer invoiceNO = statproduct.getInt("InvoiceNumber");
                invoicelist.add(invoiceNO);
            }
            System.out.println(invoicelist);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return invoicelist;
    }
    
    public ObservableList<Invoice> ConvertToInvoice(ObservableList<Partner> partner){
        ObservableList<Invoice> prolist = FXCollections.observableArrayList();
        partner.forEach(_item ->{
            Invoice invoice = new Invoice(0, _item.getStockname(), _item.getNumberofgoods(), _item.getUnitprice(), _item.getTotalprice());
            prolist.add(invoice);
        });
        return prolist;
    }
    
    public ObservableList<Invoice> displayInvoiceTempList(Integer invoicenumber, Integer brID){
        ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select * from InvoiceNP where InvoiceNumber = '" + invoicenumber +"' and BRId = '" + brID + "'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Timestamp Date = statproduct.getTimestamp("issueTime");
                //The CustomerID will replace the invoicenumber in this context
                Integer custID = statproduct.getInt("CustId");
                String stockname = productDAO.getStockName(statproduct.getInt("STId"), brID) ;
                Integer SNOC = statproduct.getInt("Qty");
                Integer uprice = statproduct.getInt("UnitPrice");
                Integer tprice = statproduct.getInt("TotalPrice");
                invoicelist.add(new Invoice(Date.toString(), custID, stockname, SNOC, uprice, tprice));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoicelist;
    }
    
    public ObservableList<Invoice> getInvoice(Integer invoiceno, Integer brID){
        ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select issueTime,InvoiceNumber,STId,Qty,UnitPrice,TotalPrice,ReturnStatus,ReturnQty,RemainQty,ReturnTotalPrice from Invoice where InvoiceNumber = '" + invoiceno +"' and BRId = '" + brID +"'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                Integer invoice = statproduct.getInt("InvoiceNumber");
                String stockname = productDAO.getStockName(statproduct.getInt("STId"), brID);
                Integer qty = statproduct.getInt("Qty");
                Integer uprice = statproduct.getInt("UnitPrice");
                Integer tprice = statproduct.getInt("TotalPrice");
                String status = statproduct.getString("ReturnStatus");
                Integer returnqty = statproduct.getInt("ReturnQty");
                Integer remainqty = statproduct.getInt("RemainQty");               
                Integer rtprice = statproduct.getInt("ReturnTotalPrice");
                invoicelist.add(new Invoice(issuedate.toString(), invoice, stockname, qty, uprice, tprice, returnqty, remainqty, rtprice, status));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoicelist;
   
    }
    
    public ObservableList<Invoice> getInvoice(String dateA, String dateB, Integer brID){
        ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select issueTime,InvoiceNumber,STId,Qty,UnitPrice,TotalPrice,ReturnStatus,ReturnQty,RemainQty,ReturnTotalPrice from Invoice where DATE(issueTime) >= '" + dateA +"' and DATE(issueTime) <= '" + dateB + "' and BRId = '" + brID +"'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                Integer invoice = statproduct.getInt("InvoiceNumber");
                String stockname = productDAO.getStockName(statproduct.getInt("STId"), brID);
                Integer qty = statproduct.getInt("Qty");
                Integer uprice = statproduct.getInt("UnitPrice");
                Integer tprice = statproduct.getInt("TotalPrice");
                String status = statproduct.getString("ReturnStatus");
                Integer returnqty = statproduct.getInt("ReturnQty");
                Integer remainqty = statproduct.getInt("RemainQty");               
                Integer rtprice = statproduct.getInt("ReturnTotalPrice");
                invoicelist.add(new Invoice(issuedate.toString(), invoice, stockname, qty, uprice, tprice, returnqty, remainqty, rtprice, status));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoicelist;
   
    }
    
    public ObservableList<Invoice> getInvoice(String dateA, Integer brID){
        ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select issueTime,InvoiceNumber,STId,Qty,UnitPrice,TotalPrice,ReturnStatus,ReturnQty,RemainQty,ReturnTotalPrice from Invoice where DATE(issueTime) = '" + dateA +"' and BRId = '" + brID +"'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                Integer invoice = statproduct.getInt("InvoiceNumber");
                String stockname = productDAO.getStockName(statproduct.getInt("STId"), brID);
                Integer qty = statproduct.getInt("Qty");
                Integer uprice = statproduct.getInt("UnitPrice");
                Integer tprice = statproduct.getInt("TotalPrice");
                String status = statproduct.getString("ReturnStatus");
                Integer returnqty = statproduct.getInt("ReturnQty");
                Integer remainqty = statproduct.getInt("RemainQty");               
                Integer rtprice = statproduct.getInt("ReturnTotalPrice");
                invoicelist.add(new Invoice(issuedate.toString(), invoice, stockname, qty, uprice, tprice, returnqty, remainqty, rtprice, status));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoicelist;
   
    }
    
    public ObservableList<Invoice> getInvoiceDetails(String dateA, Integer brID){
        ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select issueTime,InvoiceNumber,Ownership,CustId,TotalPrice,Balance from InvoiceDetail where DATE(issueTime) = '" + dateA +"' and BRId = '" + brID +"'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                Integer invoice = statproduct.getInt("InvoiceNumber");
                String status = statproduct.getString("Ownership");
                String name = customerDAO.getcustomername(statproduct.getInt("CustId"), brID);
                Integer id = statproduct.getInt("CustId");
                Integer tprice = statproduct.getInt("TotalPrice");
                Integer balance = statproduct.getInt("Balance");
                invoicelist.add(new Invoice(issuedate.toString(), invoice, name, id, 0, tprice, balance, 0, 0, status));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoicelist;
   
    }
    
    public ObservableList<Invoice> getInvoiceDetails(String dateA, String dateB, Integer brID){
        ObservableList<Invoice> invoicelist = FXCollections.observableArrayList();
        try {
           String getproduct = "select issueTime,InvoiceNumber,Ownership,CustId,TotalPrice,Balance from InvoiceDetail where DATE(issueTime) >= '" + dateA +"' and DATE(issueTime) <= '" + dateB + "' and BRId = '" + brID +"'";
           System.out.println(getproduct);
           PreparedStatement preproduct = DbSet.multiQuery(getproduct);
           ResultSet statproduct = preproduct.executeQuery();
           while(statproduct.next()){
                Timestamp issuedate = statproduct.getTimestamp("issueTime");
                Integer invoice = statproduct.getInt("InvoiceNumber");
                String status = statproduct.getString("Ownership");
                String name = customerDAO.getcustomername(statproduct.getInt("CustId"), brID);
                Integer id = statproduct.getInt("CustId");
                Integer tprice = statproduct.getInt("TotalPrice");
                Integer balance = statproduct.getInt("Balance");
                invoicelist.add(new Invoice(issuedate.toString(), invoice, name, id, 0, tprice, balance, 0, 0, status));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoicelist;
   
    }
    
    public Invoice getInvoiceTemplist(Integer invoice, Integer brID){
        Integer totprice = 0; Integer bal = 0; Integer custID = 0; String date = "";
        try {
            String queryretrieve = "select issueTime, CustId, TotalPrice, Balance from InvoiceDetailNP where InvoiceNumber = '" + invoice + "' and BRId = '" + brID + "'";
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                date = statsto.getString("issueTime");
                custID = statsto.getInt("CustId");
                totprice = statsto.getInt("TotalPrice");
                bal = statsto.getInt("Balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Invoice(totprice, date, custID, bal, 0);
    }
   
    public Invoice getInvoicePersona (Integer invoice, Integer brID){
        Integer custID = 0; String owner = "";
        try {
            String queryretrieve = "select Ownership, CustId from InvoiceDetail where InvoiceNumber = '" + invoice + "' and BRId = '" + brID + "'";
            PreparedStatement prequery = DbSet.multiQuery(queryretrieve);
            System.out.println(prequery);
            ResultSet statsto = prequery.executeQuery();
            while(statsto.next()){
                owner = statsto.getString("Ownership");
                custID = statsto.getInt("CustId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Invoice(0, owner, custID, 0, 0);
    }
    
    
}
