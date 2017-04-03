<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author HUY-PC
 */
@Embeddable
public class StockPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MoneyID")
    private int moneyID;
    @Basic(optional = false)
    @Column(name = "ATMID")
    private int atmid;

    public StockPK() {
    }

    public StockPK(int moneyID, int atmid) {
        this.moneyID = moneyID;
        this.atmid = atmid;
    }

    public int getMoneyID() {
        return moneyID;
    }

    public void setMoneyID(int moneyID) {
        this.moneyID = moneyID;
    }

    public int getAtmid() {
        return atmid;
    }

    public void setAtmid(int atmid) {
        this.atmid = atmid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) moneyID;
        hash += (int) atmid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockPK)) {
            return false;
        }
        StockPK other = (StockPK) object;
        if (this.moneyID != other.moneyID) {
            return false;
        }
        if (this.atmid != other.atmid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StockPK[ moneyID=" + moneyID + ", atmid=" + atmid + " ]";
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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author HUY-PC
 */
@Embeddable
public class StockPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MoneyID")
    private int moneyID;
    @Basic(optional = false)
    @Column(name = "ATMID")
    private int atmid;

    public StockPK() {
    }

    public StockPK(int moneyID, int atmid) {
        this.moneyID = moneyID;
        this.atmid = atmid;
    }

    public int getMoneyID() {
        return moneyID;
    }

    public void setMoneyID(int moneyID) {
        this.moneyID = moneyID;
    }

    public int getAtmid() {
        return atmid;
    }

    public void setAtmid(int atmid) {
        this.atmid = atmid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) moneyID;
        hash += (int) atmid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockPK)) {
            return false;
        }
        StockPK other = (StockPK) object;
        if (this.moneyID != other.moneyID) {
            return false;
        }
        if (this.atmid != other.atmid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StockPK[ moneyID=" + moneyID + ", atmid=" + atmid + " ]";
    }
    
}
>>>>>>> acf0e3267d851f3e4de6d24aebaa5bce97662785
