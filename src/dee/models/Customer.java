
package dee.models;

import dee.database.deeinventory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Customer {
    private Integer id;
    private String fname;
    private String origin;
    private SimpleStringProperty custfname;
    private SimpleIntegerProperty custid;
    private SimpleStringProperty custorigin;
    private SimpleStringProperty custstatus;
    private deeinventory DbSet;

    public Integer getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getOrigin() {
        return origin;
    }

    public String getCustfname() {
        return custfname.get();
    }

    public int getCustid() {
        return custid.get();
    }

    public String getCustorigin() {
        return custorigin.get();
    }

    public String getCuststatus() {
        return custstatus.get();
    }
    
   
    public Customer(){
    }
    
    public Customer(Integer ID, String FNAME, String ORIGIN){
        this.id = ID;
        this.fname = FNAME;
        this.origin = ORIGIN;
    }
    
    public Customer(String cusOrigin, Integer ID, String FNAME, String status){
        this.custid = new SimpleIntegerProperty(ID);
        this.custfname = new SimpleStringProperty(FNAME);
        this.custorigin = new SimpleStringProperty(cusOrigin);
        this.custstatus = new SimpleStringProperty(status);
    }

   
    
    
    
    
}
