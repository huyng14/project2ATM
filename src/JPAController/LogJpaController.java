/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPAController;

import JPAController.exceptions.NonexistentEntityException;
import JPAController.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Atm;
import entity.Card;
import entity.Log;
import entity.LogType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class LogJpaController implements Serializable {

    public LogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Log log) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atm atmid = log.getAtmid();
            if (atmid != null) {
                atmid = em.getReference(atmid.getClass(), atmid.getAtmid());
                log.setAtmid(atmid);
            }
            Card cardNo = log.getCardNo();
            if (cardNo != null) {
                cardNo = em.getReference(cardNo.getClass(), cardNo.getCardNo());
                log.setCardNo(cardNo);
            }
            LogType logTypeID = log.getLogTypeID();
            if (logTypeID != null) {
                logTypeID = em.getReference(logTypeID.getClass(), logTypeID.getLogTypeID());
                log.setLogTypeID(logTypeID);
            }
            em.persist(log);
            if (atmid != null) {
                atmid.getLogCollection().add(log);
                atmid = em.merge(atmid);
            }
            if (cardNo != null) {
                cardNo.getLogCollection().add(log);
                cardNo = em.merge(cardNo);
            }
            if (logTypeID != null) {
                logTypeID.getLogCollection().add(log);
                logTypeID = em.merge(logTypeID);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLog(log.getLogID()) != null) {
                throw new PreexistingEntityException("Log " + log + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Log log) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Log persistentLog = em.find(Log.class, log.getLogID());
            Atm atmidOld = persistentLog.getAtmid();
            Atm atmidNew = log.getAtmid();
            Card cardNoOld = persistentLog.getCardNo();
            Card cardNoNew = log.getCardNo();
            LogType logTypeIDOld = persistentLog.getLogTypeID();
            LogType logTypeIDNew = log.getLogTypeID();
            if (atmidNew != null) {
                atmidNew = em.getReference(atmidNew.getClass(), atmidNew.getAtmid());
                log.setAtmid(atmidNew);
            }
            if (cardNoNew != null) {
                cardNoNew = em.getReference(cardNoNew.getClass(), cardNoNew.getCardNo());
                log.setCardNo(cardNoNew);
            }
            if (logTypeIDNew != null) {
                logTypeIDNew = em.getReference(logTypeIDNew.getClass(), logTypeIDNew.getLogTypeID());
                log.setLogTypeID(logTypeIDNew);
            }
            log = em.merge(log);
            if (atmidOld != null && !atmidOld.equals(atmidNew)) {
                atmidOld.getLogCollection().remove(log);
                atmidOld = em.merge(atmidOld);
            }
            if (atmidNew != null && !atmidNew.equals(atmidOld)) {
                atmidNew.getLogCollection().add(log);
                atmidNew = em.merge(atmidNew);
            }
            if (cardNoOld != null && !cardNoOld.equals(cardNoNew)) {
                cardNoOld.getLogCollection().remove(log);
                cardNoOld = em.merge(cardNoOld);
            }
            if (cardNoNew != null && !cardNoNew.equals(cardNoOld)) {
                cardNoNew.getLogCollection().add(log);
                cardNoNew = em.merge(cardNoNew);
            }
            if (logTypeIDOld != null && !logTypeIDOld.equals(logTypeIDNew)) {
                logTypeIDOld.getLogCollection().remove(log);
                logTypeIDOld = em.merge(logTypeIDOld);
            }
            if (logTypeIDNew != null && !logTypeIDNew.equals(logTypeIDOld)) {
                logTypeIDNew.getLogCollection().add(log);
                logTypeIDNew = em.merge(logTypeIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = log.getLogID();
                if (findLog(id) == null) {
                    throw new NonexistentEntityException("The log with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Log log;
            try {
                log = em.getReference(Log.class, id);
                log.getLogID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The log with id " + id + " no longer exists.", enfe);
            }
            Atm atmid = log.getAtmid();
            if (atmid != null) {
                atmid.getLogCollection().remove(log);
                atmid = em.merge(atmid);
            }
            Card cardNo = log.getCardNo();
            if (cardNo != null) {
                cardNo.getLogCollection().remove(log);
                cardNo = em.merge(cardNo);
            }
            LogType logTypeID = log.getLogTypeID();
            if (logTypeID != null) {
                logTypeID.getLogCollection().remove(log);
                logTypeID = em.merge(logTypeID);
            }
            em.remove(log);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Log> findLogEntities() {
        return findLogEntities(true, -1, -1);
    }

    public List<Log> findLogEntities(int maxResults, int firstResult) {
        return findLogEntities(false, maxResults, firstResult);
    }

    private List<Log> findLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Log.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Log findLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Log.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Log> rt = cq.from(Log.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
