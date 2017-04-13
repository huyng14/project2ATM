/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bill;

import DTO.LogLogTypeDTO;
import dao.LogDAOImpl;
import java.util.List;

/**
 *
 * @author C1602L3818
 */
public class LogServices {
    private static LogDAOImpl  logObj= new LogDAOImpl();

    public static List<LogLogTypeDTO> getLogByCardNo(String dateTime,String cardNo){
        return logObj.getLogByCardNo(dateTime,cardNo);
    }
    
    
}
