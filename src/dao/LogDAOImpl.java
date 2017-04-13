/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import DTO.LogLogTypeDTO;
import entity.Log;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.eclipse.persistence.config.PersistenceUnitProperties;

/**
 *
 * @author C1602L3818
 */
public class LogDAOImpl implements ILogDAO{

    @Override
    public List<LogLogTypeDTO> getLogByCardNo(String dateTime, String cardNo) {
        EntityManagerFactory emf= utility.connectDB.connectionDB();
        EntityManager em= emf.createEntityManager();
        StringBuilder sb= new StringBuilder();
        sb.append("SELECT L.LogID, L.LogDate, L.Amount, LT.Description, A.Address");
        sb.append(" FROM Log L, LogType LT, ATM A");
        sb.append(" WHERE  L.LogTypeID= LT.LogTypeID AND L.ATMID= A.ATMID AND L.CardNo= "+ cardNo+" AND LogDate >= "+ dateTime);
        sb.append(" ORDER BY LogDate ASC");
        System.out.println(sb);
        Query query= em.createNativeQuery(sb.toString(), LogLogTypeDTO.class);
        return query.getResultList();
    }
    
}
