/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author HUY-PC
 */
@Entity
@Table(name = "Account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
    , @NamedQuery(name = "Account.findByAccountID", query = "SELECT a FROM Account a WHERE a.accountID = :accountID")
    , @NamedQuery(name = "Account.findByBalance", query = "SELECT a FROM Account a WHERE a.balance = :balance")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    private Integer accountID;
    @Column(name = "Balance")
    private Long balance;
    @JoinColumn(name = "CustID", referencedColumnName = "CustID")
    @ManyToOne
    private Customer custID;
    @OneToMany(mappedBy = "accountID")
    private Collection<Card> cardCollection;

    public Account() {
    }

    public Account(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Customer getCustID() {
        return custID;
    }

    public void setCustID(Customer custID) {
        this.custID = custID;
    }

    @XmlTransient
    public Collection<Card> getCardCollection() {
        return cardCollection;
    }

    public void setCardCollection(Collection<Card> cardCollection) {
        this.cardCollection = cardCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountID != null ? accountID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.accountID == null && other.accountID != null) || (this.accountID != null && !this.accountID.equals(other.accountID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Account[ accountID=" + accountID + " ]";
    }
    
}
