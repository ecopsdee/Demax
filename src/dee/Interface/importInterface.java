
package dee.Interface;

import dee.models.Import;
import javafx.collections.ObservableList;


public interface importInterface {
    
    public boolean CreateImport(ObservableList<Import> imports, Integer brID);
    
    public Integer RemainProduct(String account);
    
    public Integer Total(Integer qty, Integer remain);
}
