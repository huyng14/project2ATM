/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bill;

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
    
    public static CardDTO getByCardNo(String cardNo){
        return objCard.getByCardNo(cardNo);
    }
}
