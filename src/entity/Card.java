/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author HUY-PC
 */
@Entity
@Table(name = "Card")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Card.findAll", query = "SELECT c FROM Card c")
    , @NamedQuery(name = "Card.findByCardNo", query = "SELECT c FROM Card c WHERE c.cardNo = :cardNo")
    , @NamedQuery(name = "Card.findByStatus", query = "SELECT c FROM Card c WHERE c.status = :status")
    , @NamedQuery(name = "Card.findByPin", query = "SELECT c FROM Card c WHERE c.pin = :pin")
    , @NamedQuery(name = "Card.findByStartDate", query = "SELECT c FROM Card c WHERE c.startDate = :startDate")
    , @NamedQuery(name = "Card.findByExpiredDate", query = "SELECT c FROM Card c WHERE c.expiredDate = :expiredDate")
    , @NamedQuery(name = "Card.findByAttempt", query = "SELECT c FROM Card c WHERE c.attempt = :attempt")})
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CardNo")
    private String cardNo;
    @Column(name = "Status")
    private String status;
    @Column(name = "PIN")
    private String pin;
    @Column(name = "StartDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "ExpiredDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;
    @Column(name = "Attempt")
    private Integer attempt;
    @OneToMany(mappedBy = "cardNo")
    private Collection<Log> logCollection;
    @JoinColumn(name = "AccountID", referencedColumnName = "AccountID")
    @ManyToOne
    private Account accountID;

    public Card() {
    }

    public Card(String cardNo) {
        this.cardNo = cardNo;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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

    @XmlTransient
    public Collection<Log> getLogCollection() {
        return logCollection;
    }

    public void setLogCollection(Collection<Log> logCollection) {
        this.logCollection = logCollection;
    }

    public Account getAccountID() {
        return accountID;
    }

    public void setAccountID(Account accountID) {
        this.accountID = accountID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardNo != null ? cardNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Card)) {
            return false;
        }
        Card other = (Card) object;
        if ((this.cardNo == null && other.cardNo != null) || (this.cardNo != null && !this.cardNo.equals(other.cardNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Card[ cardNo=" + cardNo + " ]";
    }
    
}
