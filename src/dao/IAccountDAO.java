/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Account;

/**
 *
 * @author HUY-PC
 */
public interface IAccountDAO {
    public Account getByAccountID(String accID);
    
    public boolean updateAccount(Account acc);
}
