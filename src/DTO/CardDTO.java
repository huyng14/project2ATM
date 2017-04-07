/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HUY-PC
 */

@Entity
@XmlRootElement
public class CardDTO implements Serializable{
    @Id
    @Basic(optional = false)
    @Column(name = "CardNo")
    private String cardNo;
    @Column(name = "Status")
    private String status;
    @Column(name = "PIN")
    private String pin;
    @Column(name = "ExpiredDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;
    @Column(name = "Attempt")
    private Integer attempt;
    
    public CardDTO(){
        
    }

    public CardDTO(String cardNo, String status, String pin, Date expiredDate, Integer attempt) {
        this.cardNo = cardNo;
        this.status = status;
        this.pin = pin;
        this.expiredDate = expiredDate;
        this.attempt = attempt;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }
    
    
}
