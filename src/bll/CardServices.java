/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bll;

import DTO.CardDTO;
import dao.CardDAOImpl;
import entity.Card;
import java.util.List;

/**
 *
 * @author HUY-PC
 */
public class CardServices {
    private static CardDAOImpl objCard= new CardDAOImpl();
    
    public static List<Card> getAll(){
        return objCard.getAll();
    }
    
    public static Card getByCardNo(String cardNo){
        return objCard.getByCardNo(cardNo);
    }
    
    public static boolean updateCard(Card card){
        return objCard.updateCard(card);
    }
}
