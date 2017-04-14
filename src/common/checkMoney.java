/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author HUY-PC
 */
public class checkMoney {
    private static final int moneyMin= 50000;
    public static  boolean ktrTienGui(long money)
    {
        if(money % moneyMin !=0 || money == 0)
            return false;
        return true;
    }
}
