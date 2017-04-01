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
@Table(name = "LogType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogType.findAll", query = "SELECT l FROM LogType l")
    , @NamedQuery(name = "LogType.findByLogTypeID", query = "SELECT l FROM LogType l WHERE l.logTypeID = :logTypeID")
    , @NamedQuery(name = "LogType.findByDescription", query = "SELECT l FROM LogType l WHERE l.description = :description")})
public class LogType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogTypeID")
    private Integer logTypeID;
    @Column(name = "Description")
    private String description;
    @OneToMany(mappedBy = "logTypeID")
    private Collection<Log> logCollection;

    public LogType() {
    }

    public LogType(Integer logTypeID) {
        this.logTypeID = logTypeID;
    }

    public Integer getLogTypeID() {
        return logTypeID;
    }

    public void setLogTypeID(Integer logTypeID) {
        this.logTypeID = logTypeID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Log> getLogCollection() {
        return logCollection;
    }

    public void setLogCollection(Collection<Log> logCollection) {
        this.logCollection = logCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logTypeID != null ? logTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogType)) {
            return false;
        }
        LogType other = (LogType) object;
        if ((this.logTypeID == null && other.logTypeID != null) || (this.logTypeID != null && !this.logTypeID.equals(other.logTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LogType[ logTypeID=" + logTypeID + " ]";
    }
    
}
