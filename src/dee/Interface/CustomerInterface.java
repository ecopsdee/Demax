
package dee.Interface;

import dee.models.Customer;
import javafx.collections.ObservableList;


public interface CustomerInterface {
    
    public ObservableList<Customer> getCustomer();
    
    public Boolean CreateCustomer(Customer customers, Integer brID);
    
    public Boolean checkCustomer(Integer CustomerID, Integer brID);
    
    public Boolean SendCustomer(Customer customers, Integer brID);
    
    public Boolean DeleteCustomerReview(Customer customers, Integer brID);
    
    public ObservableList<Customer> getCustomerTempList(Integer customerID, Integer brID);
    
    public Boolean deleteCustomerTempList(Integer customerID, Integer brID);
    
    public String getcustomername(Integer customerID, Integer brID);
    
}
