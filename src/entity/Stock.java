/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HUY-PC
 */
@Entity
@Table(name = "Stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stock.findAll", query = "SELECT s FROM Stock s")
    , @NamedQuery(name = "Stock.findByMoneyID", query = "SELECT s FROM Stock s WHERE s.stockPK.moneyID = :moneyID")
    , @NamedQuery(name = "Stock.findByAtmid", query = "SELECT s FROM Stock s WHERE s.stockPK.atmid = :atmid")
    , @NamedQuery(name = "Stock.findByQuanlity", query = "SELECT s FROM Stock s WHERE s.quanlity = :quanlity")
    , @NamedQuery(name = "Stock.findByTotal", query = "SELECT s FROM Stock s WHERE s.total = :total")})
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StockPK stockPK;
    @Column(name = "Quanlity")
    private Integer quanlity;
    @Column(name = "Total")
    private Long total;
    @JoinColumn(name = "ATMID", referencedColumnName = "ATMID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Atm atm;
    @JoinColumn(name = "MoneyID", referencedColumnName = "MoneyID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Money money;

    public Stock() {
    }

    public Stock(StockPK stockPK) {
        this.stockPK = stockPK;
    }

    public Stock(int moneyID, int atmid) {
        this.stockPK = new StockPK(moneyID, atmid);
    }

    public StockPK getStockPK() {
        return stockPK;
    }

    public void setStockPK(StockPK stockPK) {
        this.stockPK = stockPK;
    }

    public Integer getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(Integer quanlity) {
        this.quanlity = quanlity;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Atm getAtm() {
        return atm;
    }

    public void setAtm(Atm atm) {
        this.atm = atm;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockPK != null ? stockPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.stockPK == null && other.stockPK != null) || (this.stockPK != null && !this.stockPK.equals(other.stockPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Stock[ stockPK=" + stockPK + " ]";
    }
    
}
