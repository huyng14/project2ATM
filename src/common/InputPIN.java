/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import javax.swing.JPasswordField;
import sun.security.util.Password;

/**
 *
 * @author HUY-PC
 */
public class InputPIN {
 
    public static void nhapMaPIN(String str, JPasswordField pass){
        if(str.length()<=6){
            System.out.println(str);
            pass.setText(str);
        }
        else{
            return;
        }
    }
}
