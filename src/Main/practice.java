
package Main;

import dee.models.Employee;
import dee.models.Stock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Random;


public class practice {
    public static void main(String[] args) {
        System.out.println(displaydateandtime());
        
    }
    
    private static String displaydateandtime(){
        DateTimeFormatter datetime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        LocalDateTime now = LocalDateTime.now();
        return datetime.format(now);
    }
           
}
    
    


