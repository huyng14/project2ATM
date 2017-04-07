/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author HUY-PC
 */
public class connectDB {
    public static EntityManagerFactory connectionDB(){
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("databasePU");
        return emf;
    }
}
