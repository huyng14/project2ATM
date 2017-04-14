/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import DTO.AccCustDTO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import utility.connectDB;

/**
 *
 * @author EightDdy
 */
public class CustomerDAO implements ICustomer{

    @Override
    public AccCustDTO getNameByAccID(int id) {
        EntityManagerFactory emf = connectDB.connectionDB();
        EntityManager em = emf.createEntityManager();
        
        try{
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.AccountID, c.Name");
        sb.append(" FROM Account a INNER JOIN Customer c ON ");
        sb.append(" c.CustID = a.CustID WHERE a.AccountID = "+id);
//        System.out.println(sb);

        Query q = em.createNativeQuery(sb.toString(),AccCustDTO.class);
        return (AccCustDTO) q.getSingleResult();
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
        
        
    }
    
}
