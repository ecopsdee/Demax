
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;



public class Warehouse {
    private Integer wareid;
    private String wname;
    private String wstate;
    private String city;
    private String describe;
    private SimpleIntegerProperty idcol;
    private SimpleStringProperty wnamecol;
    private SimpleStringProperty wstatecol;
    private SimpleStringProperty wcitycol;
    private SimpleStringProperty wdescribecol;

    public Integer getWareid() {
        return wareid;
    }

    public void setWareid(Integer wareid) {
        this.wareid = wareid;
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public String getWstate() {
        return wstate;
    }

    public void setWstate(String wstate) {
        this.wstate = wstate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getIdcol() {
        return idcol.get();
    }

    public String getWnamecol() {
        return wnamecol.get();
    }


    public String getWstatecol() {
        return wstatecol.get();
    }

    public String getWcitycol() {
        return wcitycol.get();
    }

    public String getWdescribecol() {
        return wdescribecol.get();
    }
    
    public Warehouse(){
    }
    
    public Warehouse(String name, String state, String city, String describe){
        wname = name;
        wstate = state;
        this.city = city;
        this.describe = describe;  
    }
    
    public Warehouse(Integer id, String name,String state, String city, String describe){
        idcol = new SimpleIntegerProperty(id);
        wnamecol = new SimpleStringProperty(name);
        wstatecol = new SimpleStringProperty(state);
        wcitycol = new SimpleStringProperty(city);
        wdescribecol = new SimpleStringProperty(describe);
    }
    
    
}
