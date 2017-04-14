/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import javax.swing.JTextField;

/**
 *
 * @author HUY-PC
 */
public class InputMoneyTranfer {
    public static void nhapIDAccReceiver(String str, JTextField ID){
        if(str.length()<=12){
            ID.setText(str);
        }
        else{
            return;
        }
    }
    
    public static void nhapAmount(String str, JTextField amount){
        amount.setText(str);
    }
}
