
package dee.DAO;

import dee.models.Activity;
import dee.models.Branch;
import dee.models.Invoice;
import dee.models.Partner;
import dee.models.Product;
import dee.models.Warehouse;
import dee.models.WarehouseOrder;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;


public class ReportDAO extends JFrame{
    
    public Boolean printWarehouse(ObservableList<Warehouse> warehouse, String username, String date, String picA, String name, String shop, String printdate){
        try {
            String File = "/dee/reports/warehousereport.jrxml";
            BufferedImage imageA = ImageIO.read(getClass().getResource(picA));
            InputStream template = this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageA);
            parameter.put("employee", username);
            parameter.put("date", date);
            parameter.put("name", name);
            parameter.put("shop", shop);
            parameter.put("printdate", printdate);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(warehouse,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch ( IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printWarehouseOrder(ObservableList<WarehouseOrder> warehouse,String username, String date, String date2, String picA, String printdate){
        try {
            String File = "/dee/reports/warehousetakeout.jrxml";
            BufferedImage imageB = ImageIO.read(getClass().getResource(picA));
            InputStream template=this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageB);
            parameter.put("employee", username);
            parameter.put("DATE", date);
            parameter.put("DATE2", date2);
            parameter.put("date", printdate);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(warehouse,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch (IOException | JRException ex ) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printWarehouseDetail(ObservableList<Product> warehouse,String username, String date, String ware, String picA){
        try {
            String File = "/dee/reports/productbasedwarehouse.jrxml";
            BufferedImage imageC = ImageIO.read(getClass().getResource(picA));
            InputStream template=this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageC);
            parameter.put("Warehouse", ware);
            parameter.put("date", date);
            parameter.put("username", username);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(warehouse,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch ( IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printDailySales(ObservableList<Invoice> invoice,String username, String DATE, String date2, String date, Integer invoicenum, Integer total, String picA){
        try {
            String File = "/dee/reports/dailysales.jrxml";
            BufferedImage imageC = ImageIO.read(getClass().getResource(picA));
            InputStream template = this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageC);
            parameter.put("invoicenumber", invoicenum);
            parameter.put("total", total);
            parameter.put("date", date);
            parameter.put("employee", username);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(invoice,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch (IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printInvoice(ObservableList<Invoice> invoice,String username, String fromdate, String todate, String date, Integer invoicenum, Integer total, String picA){
        try {
            String File = "/dee/reports/invoice.jrxml";
            BufferedImage imageC = ImageIO.read(getClass().getResource(picA));
            InputStream template = this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageC);
            parameter.put("invoice", invoicenum);
            parameter.put("total", total);
            parameter.put("date", date);
            parameter.put("fromdate", fromdate);
            parameter.put("todate", todate);
            parameter.put("employee", username);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(invoice,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch (IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printCredit(ObservableList<Partner> partner ,String username, String name, String dateA, String dateB, String date, Integer total, String picA){
        try {
            String File = "/dee/reports/creditaccount.jrxml";
            BufferedImage imageC = ImageIO.read(getClass().getResource(picA));
            InputStream template = this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageC);
            parameter.put("date", date);
            parameter.put("employee", username);
            parameter.put("account", name);
            parameter.put("fromdate", dateA);
            parameter.put("todate", dateB);
            parameter.put("total", total);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(partner,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch ( IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printBalanceSheet(ObservableList<Partner> partner ,String username, String name, String DATE, String date2, String date, String picA){
        try {
            String File = "/dee/reports/distributorinvoice.jrxml";
            BufferedImage imageC = ImageIO.read(getClass().getResource(picA));
            InputStream template = this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageC);
            parameter.put("date", date);
            parameter.put("employee", username);
            parameter.put("distname", name);
            parameter.put("dateA", DATE);
            parameter.put("dateB", date2);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(partner,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch ( IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printBranchOutlet(ObservableList<Branch> branch ,String username, String branchname, String DATE, String dateA, String dateB, String picA){
        try {
            String File = "/dee/reports/branchoutlet.jrxml";
            BufferedImage imageC = ImageIO.read(getClass().getResource(picA));
            InputStream template = this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageC);
            parameter.put("date", DATE);
            parameter.put("employee", username);
            parameter.put("branch", branchname);
            parameter.put("fromdate", dateA);
            parameter.put("todate", dateB);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(branch,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch ( IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printBranchSheet(ObservableList<Branch> partner ,String username, String name, String DATE, String date2, String date, String picA){
        try {
            String File = "/dee/reports/branchsheet.jrxml";
            BufferedImage imageC = ImageIO.read(getClass().getResource(picA));
            InputStream template = this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageC);
            parameter.put("date", date);
            parameter.put("employee", username);
            parameter.put("name", name);
            parameter.put("fromdate", DATE);
            parameter.put("todate", date2);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(partner,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch ( IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public Boolean printActivity(ObservableList<Activity> activity ,String username, String dateprint, String date, Integer revenue, Integer expense, Integer balance, String picA){
        try {
            String File = "/dee/reports/activity.jrxml";
            BufferedImage imageC = ImageIO.read(getClass().getResource(picA));
            InputStream template = this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            parameter.put("logoa", imageC);
            parameter.put("date", date);
            parameter.put("revenue", revenue);
            parameter.put("expense", expense);
            parameter.put("balance", balance);
            parameter.put("dateprint", dateprint);
            parameter.put("user", username);
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(activity,false);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct );
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch ( IOException | JRException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
}


