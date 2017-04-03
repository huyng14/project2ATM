<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "Money")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Money.findAll", query = "SELECT m FROM Money m")
    , @NamedQuery(name = "Money.findByMoneyID", query = "SELECT m FROM Money m WHERE m.moneyID = :moneyID")
    , @NamedQuery(name = "Money.findByMoneyValue", query = "SELECT m FROM Money m WHERE m.moneyValue = :moneyValue")})
public class Money implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MoneyID")
    private Integer moneyID;
    @Column(name = "MoneyValue")
    private Long moneyValue;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "money")
    private Collection<Stock> stockCollection;

    public Money() {
    }

    public Money(Integer moneyID) {
        this.moneyID = moneyID;
    }

    public Integer getMoneyID() {
        return moneyID;
    }

    public void setMoneyID(Integer moneyID) {
        this.moneyID = moneyID;
    }

    public Long getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Long moneyValue) {
        this.moneyValue = moneyValue;
    }

    @XmlTransient
    public Collection<Stock> getStockCollection() {
        return stockCollection;
    }

    public void setStockCollection(Collection<Stock> stockCollection) {
        this.stockCollection = stockCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moneyID != null ? moneyID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Money)) {
            return false;
        }
        Money other = (Money) object;
        if ((this.moneyID == null && other.moneyID != null) || (this.moneyID != null && !this.moneyID.equals(other.moneyID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Money[ moneyID=" + moneyID + " ]";
    }
    
}
=======
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "Money")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Money.findAll", query = "SELECT m FROM Money m")
    , @NamedQuery(name = "Money.findByMoneyID", query = "SELECT m FROM Money m WHERE m.moneyID = :moneyID")
    , @NamedQuery(name = "Money.findByMoneyValue", query = "SELECT m FROM Money m WHERE m.moneyValue = :moneyValue")})
public class Money implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MoneyID")
    private Integer moneyID;
    @Column(name = "MoneyValue")
    private Long moneyValue;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "money")
    private Collection<Stock> stockCollection;

    public Money() {
    }

    public Money(Integer moneyID) {
        this.moneyID = moneyID;
    }

    public Integer getMoneyID() {
        return moneyID;
    }

    public void setMoneyID(Integer moneyID) {
        this.moneyID = moneyID;
    }

    public Long getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Long moneyValue) {
        this.moneyValue = moneyValue;
    }

    @XmlTransient
    public Collection<Stock> getStockCollection() {
        return stockCollection;
    }

    public void setStockCollection(Collection<Stock> stockCollection) {
        this.stockCollection = stockCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moneyID != null ? moneyID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Money)) {
            return false;
        }
        Money other = (Money) object;
        if ((this.moneyID == null && other.moneyID != null) || (this.moneyID != null && !this.moneyID.equals(other.moneyID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Money[ moneyID=" + moneyID + " ]";
    }
    
}
>>>>>>> acf0e3267d851f3e4de6d24aebaa5bce97662785
