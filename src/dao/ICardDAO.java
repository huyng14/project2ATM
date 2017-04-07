/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import DTO.CardDTO;
import entity.Card;
import java.util.List;

/**
 *
 * @author HUY-PC
 */
public interface ICardDAO {
    public List<Card> getAll();
    
    public CardDTO getByCardNo(String cardNo);
}
