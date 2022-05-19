
package dee.reports.run;

import dee.models.Employee;
import dee.models.Invoice;
import dee.models.Partner;
import dee.models.Product;
import dee.models.Stock;
import dee.models.Warehouse;
import dee.models.WarehouseOrder;
import java.io.InputStream;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;


public class warehousereport extends JFrame{
    
    private String distname;
    private Integer distID;
    private Integer distorder;
    private String diststatus;
    private String distdate;
    private Integer distinvoice;
    private Integer distqty;
    private Integer distprice;
    private Integer distbalance;

    public String getDistname() {
        return distname;
    }

    public Integer getDistID() {
        return distID;
    }

    public Integer getDistorder() {
        return distorder;
    }

    public String getDiststatus() {
        return diststatus;
    }

    public String getDistdate() {
        return distdate;
    }

    public Integer getDistinvoice() {
        return distinvoice;
    }

    public Integer getDistqty() {
        return distqty;
    }

    public Integer getDistprice() {
        return distprice;
    }

    public Integer getDistbalance() {
        return distbalance;
    }

    public warehousereport(String distname, Integer distID, Integer distorder, String diststatus, String distdate, Integer distinvoice, Integer distqty, Integer distprice, Integer distbalance) {
        this.distname = distname;
        this.distID = distID;
        this.distorder = distorder;
        this.diststatus = diststatus;
        this.distdate = distdate;
        this.distinvoice = distinvoice;
        this.distqty = distqty;
        this.distprice = distprice;
        this.distbalance = distbalance;
    }


}
