
package dee.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

   

public class Employee {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String phone;
    private String address;
    private String type;
    private SimpleIntegerProperty id;
    private SimpleStringProperty fname;
    private SimpleStringProperty Lname;
    private SimpleStringProperty uname;
    private SimpleStringProperty Pass;
    private SimpleStringProperty Fone;
    private SimpleStringProperty Addres;
    private SimpleStringProperty emtype;
   

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id.get();
    }

    public String getFname() {
        return fname.get();
    }

    public String getUname() {
        return uname.get();
    }

    public String getEmtype() {
        return emtype.get();
    }

    public String getLname() {
        return Lname.get();
    }

    public String getPass() {
        return Pass.get();
    }

    public String getFone() {
        return Fone.get();
    }

    public String getAddres() {
        return Addres.get();
    }

    


    public Employee(){
      
    }
    
    public Employee(String fname, String lname, String username, String password, String phone, String address, String emtype){
        firstname = fname; 
        lastname = lname;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        type = emtype;
    }
    
    public Employee(Integer sn, String fname, String lname, String username, String password, String phone, String address, String emtype ){
        id = new SimpleIntegerProperty(sn);
        this.fname = new SimpleStringProperty(fname);
        Lname = new SimpleStringProperty(lname);
        uname = new SimpleStringProperty(username);
        Pass = new SimpleStringProperty(password);
        Fone = new SimpleStringProperty(phone);
        Addres = new SimpleStringProperty(address);
        this.emtype = new SimpleStringProperty(emtype);
        
        
    }
    
}
