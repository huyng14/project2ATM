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
@Table(name = "ATM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Atm.findAll", query = "SELECT a FROM Atm a")
    , @NamedQuery(name = "Atm.findByAtmid", query = "SELECT a FROM Atm a WHERE a.atmid = :atmid")
    , @NamedQuery(name = "Atm.findByBranch", query = "SELECT a FROM Atm a WHERE a.branch = :branch")
    , @NamedQuery(name = "Atm.findByAddress", query = "SELECT a FROM Atm a WHERE a.address = :address")})
public class Atm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATMID")
    private Integer atmid;
    @Column(name = "Branch")
    private String branch;
    @Column(name = "Address")
    private String address;
    @OneToMany(mappedBy = "atmid")
    private Collection<Log> logCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atm")
    private Collection<Stock> stockCollection;

    public Atm() {
    }

    public Atm(Integer atmid) {
        this.atmid = atmid;
    }

    public Integer getAtmid() {
        return atmid;
    }

    public void setAtmid(Integer atmid) {
        this.atmid = atmid;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlTransient
    public Collection<Log> getLogCollection() {
        return logCollection;
    }

    public void setLogCollection(Collection<Log> logCollection) {
        this.logCollection = logCollection;
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
        hash += (atmid != null ? atmid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Atm)) {
            return false;
        }
        Atm other = (Atm) object;
        if ((this.atmid == null && other.atmid != null) || (this.atmid != null && !this.atmid.equals(other.atmid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Atm[ atmid=" + atmid + " ]";
    }
    
}
