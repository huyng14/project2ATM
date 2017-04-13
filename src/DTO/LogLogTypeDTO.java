/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

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
 * @author C1602L3818
 */
@Entity
@XmlRootElement
public class LogLogTypeDTO implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "LogID")
    private Integer logID;
    
    @Column(name = "LogDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;
    
    @Column(name = "Amount")
    private Long amount;
  
    private String description;
    
    private String address;

    public LogLogTypeDTO() {
    }

    public LogLogTypeDTO(Integer logID, Date logDate, Long amount, String description, String addressATM) {
        this.logID = logID;
        this.logDate = logDate;
        this.amount = amount;
        this.description = description;
        this.address = addressATM;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "LogLogTypeDTO{" + "logID=" + logID + ", logDate=" + logDate + ", amount=" + amount + ", description=" + description + ", addressATM=" + address + '}';
    }


}
