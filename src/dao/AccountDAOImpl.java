/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import JPAController.AccountJpaController;
import entity.Account;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import utility.connectDB;

/**
 *
 * @author HUY-PC
 */
public class AccountDAOImpl implements IAccountDAO{

    @Override
    public Account getByAccountID(String accID) {
        try{
            EntityManagerFactory emf= connectDB.connectionDB();
            EntityManager em= emf.createEntityManager();
            StringBuilder sb= new StringBuilder();
            sb.append("SELECT A.AccountID, A.CustID, A.Balance");
            sb.append(" FROM Account A");
            sb.append(" WHERE AccountID= "+ accID);
            
            System.out.println(sb);
            Query query= em.createNativeQuery(sb.toString(), Account.class);
            return (Account) query.getSingleResult();
        } catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    @Override
    public boolean updateAccount(Account acc) {
        EntityManagerFactory emf= connectDB.connectionDB();
        AccountJpaController con= new AccountJpaController(emf);

        try {
            Account findAcc= con.findAccount(acc.getAccountID());
            if(findAcc==null)
            {
                return false;
            }
            con.edit(acc);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(AccountDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
