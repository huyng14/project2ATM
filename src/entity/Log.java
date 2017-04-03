/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HUY-PC
 */
@Entity
@Table(name = "Log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l")
    , @NamedQuery(name = "Log.findByLogID", query = "SELECT l FROM Log l WHERE l.logID = :logID")
    , @NamedQuery(name = "Log.findByLogDate", query = "SELECT l FROM Log l WHERE l.logDate = :logDate")
    , @NamedQuery(name = "Log.findByAmount", query = "SELECT l FROM Log l WHERE l.amount = :amount")})
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;
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

    public Log() {
    }

    public Log(Integer logID) {
        this.logID = logID;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logID != null ? logID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.logID == null && other.logID != null) || (this.logID != null && !this.logID.equals(other.logID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Log[ logID=" + logID + " ]";
    }
    
}
