/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bll;

import DTO.AccCustDTO;
import dao.CustomerDAO;

/**
 *
 * @author HUY-PC
 */
public class CustomerServices {
    private static CustomerDAO customerObj= new CustomerDAO();
    
    public static AccCustDTO getNameByAccID(int id){
        return customerObj.getNameByAccID(id);
    }
}
