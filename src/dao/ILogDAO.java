/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import DTO.LogLogTypeDTO;
import java.util.List;

/**
 *
 * @author C1602L3818
 */
public interface ILogDAO {
    public List<LogLogTypeDTO> getLogByCardNo(String dateTime, String cardNo);
}
