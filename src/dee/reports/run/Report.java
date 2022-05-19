
package dee.reports.run;

import dee.models.Product;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;


public class Report extends JFrame{
    
    public void printProduct(ObservableList<Product> product){
        try {
            String File = "/dee/reports/product.jrxml";
            String destfile ="/dee/reports";
            InputStream template=this.getClass().getResourceAsStream(File);
            JasperDesign jasper = JRXmlLoader.load(template);
            JasperReport jasperreport = JasperCompileManager.compileReport(jasper);
            HashMap parameter = new HashMap();
            JRBeanCollectionDataSource collectdata = new JRBeanCollectionDataSource(product);
            JasperPrint printproduct = JasperFillManager.fillReport(jasperreport, parameter, collectdata);
            JRViewer viewreport = new JRViewer(printproduct);
            viewreport.setVisible(true);
            this.add(viewreport);
            this.setVisible(true);
          
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
