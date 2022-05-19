
package dee.Interface;

import dee.models.Employee;
import javafx.collections.ObservableList;


public interface EmployeeInterface {
    
    public Boolean createEmployee(Employee employee, Integer brID);
    
    public void updateEmployee(Employee employee);
    
    public boolean checkUser(String username, Integer brID);
    
    public boolean checkPassword(String username, String password, Integer brID);
    
    public Boolean updateEmployee(Integer EmID);
    
    public String getemployeeType(String username, Integer brID);
    
    public ObservableList<Employee> getEmployees(Integer brId);
    
    public Boolean deleteEmployee(String username, Integer brID);
    
    public Boolean RecordLoggedIn(String username, String Type, Integer brID);
    
    public Boolean RecordLoggedOut(String username, String dat, Integer brIDe);
    
    public ObservableList<Employee> displayLoggedUser(String date, Integer brID);
    
    public ObservableList<Employee> displayLoggedUser(Integer brID);
    
    public Integer getTime();
    
    public Integer getBranchID(String branch);
    
    public Boolean deductTime(Integer time);
    
}
