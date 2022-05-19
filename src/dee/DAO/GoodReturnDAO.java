
package dee.DAO;

import dee.database.deeinventory;
import dee.models.Invoice;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class GoodReturnDAO {
    
    deeinventory DbSet = deeinventory.getinstance(); ProductDAO productDAO = new ProductDAO();
    
    //
    public Boolean sendreturn(ObservableList<Invoice> goods, Integer invoice, Integer customerID, Integer brID){
        Boolean status = false;
        String returnquery = "insert into stockreturnnp(BRId,InvoiceNumber,CustId,STId,Qty) values(?,?,?,?,?)";
        for (Invoice _item : goods) {
            try {
                PreparedStatement prereturn = DbSet.multiQuery(returnquery);
                prereturn.setInt(1, brID);
                prereturn.setInt(2, invoice);
                prereturn.setInt(3, customerID);
                prereturn.setInt(4, productDAO.getStockID(_item.getProductname(), brID));
                prereturn.setInt(5, _item.getQuantity());
                Integer success = prereturn.executeUpdate();
                if (success == 1) {
                    status = true;
                }
            } catch (SQLException ex) {
               ex.printStackTrace();
            }
        }
        return status;
    }
    
    public Boolean savereturn(ObservableList<Invoice> goods, Integer invoice, Integer customerID, Integer brID){
        Boolean status = false;
        String returnquery = "insert into stockreturn(BRId,InvoiceNumber,CustId,STId,Qty) values(?,?,?,?,?)";
        for (Invoice _item : goods) {
            try {
                PreparedStatement prereturn = DbSet.multiQuery(returnquery);
                prereturn.setInt(1, brID);
                prereturn.setInt(2, invoice);
                prereturn.setInt(3, customerID);
                prereturn.setInt(4, productDAO.getStockID(_item.getProductname(), brID));
                prereturn.setInt(5, _item.getQuantity());
                Integer success = prereturn.executeUpdate();
                if (success == 1) {
                    status = true;
                }
            } catch (SQLException ex) {
               ex.printStackTrace();
            }
        }
        return status;
    }
    
    public Boolean Deletesentreturn(ObservableList<Invoice> goods, Integer invoice, Integer customerID, Integer brID){
        Boolean status = false;
        String returnquery = "insert into StockReturnNPDelete(BRId,InvoiceNumber,CustId,STId,Qty) values(?,?,?,?,?)";
        for (Invoice _item : goods) {
            try {
                PreparedStatement prereturn = DbSet.multiQuery(returnquery);
                prereturn.setInt(1, brID);
                prereturn.setInt(2, invoice);
                prereturn.setInt(3, customerID);
                prereturn.setInt(4, productDAO.getStockID(_item.getProductname(), brID));
                prereturn.setInt(5, _item.getQuantity());
                Integer success = prereturn.executeUpdate();
                if (success == 1) {
                    status = true;
                }
            } catch (SQLException ex) {
               ex.printStackTrace();
            }
        }
        System.out.println(returnquery);
        return status;
    }
    
    public Boolean DeletePermsentreturn(Integer invoice, Integer brID){
        Boolean status = false;
        try {
            String deletequery = "delete from stockreturnnp where InvoiceNumber = '" + invoice +"' and BRId = '" + brID + "'";
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            if (predelete.executeUpdate() == 1) {
                status = true;
            }
            System.out.println(deletequery );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }
    
    public Boolean UpdateStockAccount(ObservableList<Invoice> stock, Integer invoice, Integer brID){
        Boolean status = false;
        try {
            for (Invoice item : stock) {
               String saveQuery = "insert into " + productDAO.getStockTablename(productDAO.getStockID(item.getProductname(), brID), brID) + "(STId,status,invoicenumber,instock,outstock,remstock) values (?,?,?,?,?,?)";
                PreparedStatement prequery = DbSet.multiQuery(saveQuery);
                prequery.setInt(1, productDAO.getStockID(item.getProductname(), brID));
                prequery.setString(2, "Stock Returned");
                prequery.setInt(3, invoice);
                prequery.setInt(4, item.getQuantity());
                prequery.setInt(5, 0);
                prequery.setInt(6, productRemain(productDAO.ProductRemaining(productDAO.getStockTablename(productDAO.getStockID(item.getProductname(), brID), brID)), item.getQuantity()));
                if (prequery.executeUpdate() == 1) {
                    status = true;
                }         
            } 
        } catch (SQLException ex) {
            status = false;
            ex.printStackTrace();
        }
        return status;
    }
  
    public ObservableList<Integer> getInvoice(Integer brID){
        ObservableList<Integer> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select distinct InvoiceNumber from stockreturnnp where BRId = '" + brID + "'";
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                Integer proname = prostat.getInt("InvoiceNumber");
                prolist.add(proname);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return prolist;
    }
     
    //
    public ObservableList<Invoice> getReturnList(Integer invoiceno, Integer brID){
        ObservableList<Invoice> invoiceelist = FXCollections.observableArrayList();
        try {
            Integer count = 1;
            String proquery = "select STId, Qty from stockreturnnp where InvoiceNumber = '" + invoiceno + "'";
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                String proname = productDAO.getStockName(prostat.getInt("STId"), brID)  ;
                Integer qty = prostat.getInt("Qty");
                invoiceelist.add(new Invoice(count, proname, qty));
                count++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return invoiceelist;
    }
    
    public ObservableList<Invoice> displayGoodReturn(String date, Integer brID){
    ObservableList<Invoice> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select * from stockreturn where DATE(issueTime) = '" + date +"'";
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                Integer invoiceno = prostat.getInt("InvoiceNumber");
                String stockname = productDAO.getStockName(prostat.getInt("STId"), brID);
                Integer NOG = prostat.getInt("Qty");
                Timestamp Date = prostat.getTimestamp("issueTime");
                prolist.add(new Invoice(Date.toString(), invoiceno, stockname, NOG, 0, 0));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return prolist;
    
    }
    
    public ObservableList<Invoice> displayGoodReturn(String dateA, String dateB, Integer brID){
    ObservableList<Invoice> prolist = FXCollections.observableArrayList();
        try {
            String proquery = "select * from stockreturn where DATE(issueTime) >= '" + dateA +"' and DATE(issueTime) <= '" + dateB + "' and BRId = '" + brID +"'";
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                Integer invoiceno = prostat.getInt("InvoiceNumber");
                String stockname = productDAO.getStockName(prostat.getInt("STId"), brID);
                Integer NOG = prostat.getInt("Qty");
                Timestamp Date = prostat.getTimestamp("issueTime");
                prolist.add(new Invoice(Date.toString(), invoiceno, stockname, NOG, 0, 0));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         return prolist;
    
    }
    
    //
    public Integer getCustID(Integer invoiceno){
        Integer proid = 0;
        try {
            String proquery = "select distinct CustId from stockreturnnp where InvoiceNumber = '" + invoiceno + "'";
            System.out.println(proquery);
            PreparedStatement propre = DbSet.multiQuery(proquery);
            ResultSet prostat = propre.executeQuery();  
            while (prostat.next()) {
                proid = prostat.getInt("CustId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return proid;
    }
    
    public Integer productRemain(Integer goodRem, Integer goodQty) {
        Integer ID = goodRem + goodQty;
        return ID;
    }
    
    public void deleteInvoice(Integer invoiceno){
        try {
            String deletequery = "delete from stockreturnnp where InvoiceNumber = '" + invoiceno +"'";
            PreparedStatement predelete = DbSet.multiQuery(deletequery);
            int redelete = predelete.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
}
