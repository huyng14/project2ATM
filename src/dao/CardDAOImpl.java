/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import DTO.CardDTO;
import JPAController.CardJpaController;
import entity.Card;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import utility.connectDB;

/**
 *
 * @author HUY-PC
 */
public class CardDAOImpl implements ICardDAO{

    @Override
    public List<Card> getAll() {
        EntityManagerFactory emf = connectDB.connectionDB();
        CardJpaController controller= new CardJpaController(emf);
        return controller.findCardEntities();
    }

    @Override
    public Card getByCardNo(String cardNo) {
        try{
        EntityManagerFactory emf= connectDB.connectionDB();
        EntityManager em= emf.createEntityManager();
        StringBuilder sb1= new StringBuilder();
        sb1.append("SELECT CardNo, Status, PIN, StartDate, ExpiredDate, Attempt, AccountID");
        sb1.append(" FROM Card");
        sb1.append(" WHERE CardNo = "+ cardNo);
        
        System.out.println(sb1);
        
        Query query= em.createNativeQuery(sb1.toString(), Card.class);
        return (Card) query.getSingleResult();
        
        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @Override
    public boolean updateCard(Card card) {
        EntityManagerFactory emf= connectDB.connectionDB();
        CardJpaController cardControl= new CardJpaController(emf);
        Card findCard= cardControl.findCard(card.getCardNo());
        try {
            if(findCard==null)
                return false;
            cardControl.edit(card);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(CardDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
