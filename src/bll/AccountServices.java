/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bll;

import dao.AccountDAOImpl;
import entity.Account;

/**
 *
 * @author HUY-PC
 */
public class AccountServices {
    private static AccountDAOImpl accObj= new AccountDAOImpl();
    
    public static Account getByAccountID(String accID) {
        return accObj.getByAccountID(accID);
    }
    
    public static boolean updateAccount(Account acc){
        return accObj.updateAccount(acc);
    }
}
