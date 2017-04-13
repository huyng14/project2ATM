/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import entity.Account;
import entity.Atm;
import entity.Card;
import entity.LogType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private Integer logID;
    @Column(name = "LogDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;
    @Column(name = "Amount")
    private Long amount;
    @JoinColumn(name = "ATMID", referencedColumnName = "ATMID")
    @ManyToOne
    private Atm atmid;
    @JoinColumn(name = "CardNo", referencedColumnName = "CardNo")
    @ManyToOne
    private Card cardNo;
    @JoinColumn(name = "LogTypeID", referencedColumnName = "LogTypeID")
    @ManyToOne
    private LogType logTypeID;
    
    
    
    public CardDTO(){
        
    }

    public CardDTO(Integer logID, Date logDate, Long amount, Atm atmid, Card cardNo, LogType logTypeID) {
        this.logID = logID;
        this.logDate = logDate;
        this.amount = amount;
        this.atmid = atmid;
        this.cardNo = cardNo;
        this.logTypeID = logTypeID;
    }

    public Integer getLogID() {
        return logID;
    }

    public void setLogID(Integer logID) {
        this.logID = logID;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Atm getAtmid() {
        return atmid;
    }

    public void setAtmid(Atm atmid) {
        this.atmid = atmid;
    }

    public Card getCardNo() {
        return cardNo;
    }

    public void setCardNo(Card cardNo) {
        this.cardNo = cardNo;
    }

    public LogType getLogTypeID() {
        return logTypeID;
    }

    public void setLogTypeID(LogType logTypeID) {
        this.logTypeID = logTypeID;
    }
}
