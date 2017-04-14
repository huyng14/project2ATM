/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author EightDdy
 */
@Entity
@XmlRootElement
public class AccCustDTO {
    @Id
    @Basic(optional = false)
    @Column(name = "AccountID")
    private Integer accountID;
    
    @Column(name = "Name")
    private String name;

    public AccCustDTO() {
    }

    public AccCustDTO(Integer accountID, String name) {
        this.accountID = accountID;
        this.name = name;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AccCustDTO{" + "accountID=" + accountID + ", name=" + name + '}';
    }
    
    
}
