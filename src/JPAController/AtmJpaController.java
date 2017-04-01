/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPAController;

import JPAController.exceptions.IllegalOrphanException;
import JPAController.exceptions.NonexistentEntityException;
import JPAController.exceptions.PreexistingEntityException;
import entity.Atm;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Log;
import java.util.ArrayList;
import java.util.Collection;
import entity.Stock;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class AtmJpaController implements Serializable {

    public AtmJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Atm atm) throws PreexistingEntityException, Exception {
        if (atm.getLogCollection() == null) {
            atm.setLogCollection(new ArrayList<Log>());
        }
        if (atm.getStockCollection() == null) {
            atm.setStockCollection(new ArrayList<Stock>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Log> attachedLogCollection = new ArrayList<Log>();
            for (Log logCollectionLogToAttach : atm.getLogCollection()) {
                logCollectionLogToAttach = em.getReference(logCollectionLogToAttach.getClass(), logCollectionLogToAttach.getLogID());
                attachedLogCollection.add(logCollectionLogToAttach);
            }
            atm.setLogCollection(attachedLogCollection);
            Collection<Stock> attachedStockCollection = new ArrayList<Stock>();
            for (Stock stockCollectionStockToAttach : atm.getStockCollection()) {
                stockCollectionStockToAttach = em.getReference(stockCollectionStockToAttach.getClass(), stockCollectionStockToAttach.getStockPK());
                attachedStockCollection.add(stockCollectionStockToAttach);
            }
            atm.setStockCollection(attachedStockCollection);
            em.persist(atm);
            for (Log logCollectionLog : atm.getLogCollection()) {
                Atm oldAtmidOfLogCollectionLog = logCollectionLog.getAtmid();
                logCollectionLog.setAtmid(atm);
                logCollectionLog = em.merge(logCollectionLog);
                if (oldAtmidOfLogCollectionLog != null) {
                    oldAtmidOfLogCollectionLog.getLogCollection().remove(logCollectionLog);
                    oldAtmidOfLogCollectionLog = em.merge(oldAtmidOfLogCollectionLog);
                }
            }
            for (Stock stockCollectionStock : atm.getStockCollection()) {
                Atm oldAtmOfStockCollectionStock = stockCollectionStock.getAtm();
                stockCollectionStock.setAtm(atm);
                stockCollectionStock = em.merge(stockCollectionStock);
                if (oldAtmOfStockCollectionStock != null) {
                    oldAtmOfStockCollectionStock.getStockCollection().remove(stockCollectionStock);
                    oldAtmOfStockCollectionStock = em.merge(oldAtmOfStockCollectionStock);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAtm(atm.getAtmid()) != null) {
                throw new PreexistingEntityException("Atm " + atm + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Atm atm) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atm persistentAtm = em.find(Atm.class, atm.getAtmid());
            Collection<Log> logCollectionOld = persistentAtm.getLogCollection();
            Collection<Log> logCollectionNew = atm.getLogCollection();
            Collection<Stock> stockCollectionOld = persistentAtm.getStockCollection();
            Collection<Stock> stockCollectionNew = atm.getStockCollection();
            List<String> illegalOrphanMessages = null;
            for (Stock stockCollectionOldStock : stockCollectionOld) {
                if (!stockCollectionNew.contains(stockCollectionOldStock)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Stock " + stockCollectionOldStock + " since its atm field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Log> attachedLogCollectionNew = new ArrayList<Log>();
            for (Log logCollectionNewLogToAttach : logCollectionNew) {
                logCollectionNewLogToAttach = em.getReference(logCollectionNewLogToAttach.getClass(), logCollectionNewLogToAttach.getLogID());
                attachedLogCollectionNew.add(logCollectionNewLogToAttach);
            }
            logCollectionNew = attachedLogCollectionNew;
            atm.setLogCollection(logCollectionNew);
            Collection<Stock> attachedStockCollectionNew = new ArrayList<Stock>();
            for (Stock stockCollectionNewStockToAttach : stockCollectionNew) {
                stockCollectionNewStockToAttach = em.getReference(stockCollectionNewStockToAttach.getClass(), stockCollectionNewStockToAttach.getStockPK());
                attachedStockCollectionNew.add(stockCollectionNewStockToAttach);
            }
            stockCollectionNew = attachedStockCollectionNew;
            atm.setStockCollection(stockCollectionNew);
            atm = em.merge(atm);
            for (Log logCollectionOldLog : logCollectionOld) {
                if (!logCollectionNew.contains(logCollectionOldLog)) {
                    logCollectionOldLog.setAtmid(null);
                    logCollectionOldLog = em.merge(logCollectionOldLog);
                }
            }
            for (Log logCollectionNewLog : logCollectionNew) {
                if (!logCollectionOld.contains(logCollectionNewLog)) {
                    Atm oldAtmidOfLogCollectionNewLog = logCollectionNewLog.getAtmid();
                    logCollectionNewLog.setAtmid(atm);
                    logCollectionNewLog = em.merge(logCollectionNewLog);
                    if (oldAtmidOfLogCollectionNewLog != null && !oldAtmidOfLogCollectionNewLog.equals(atm)) {
                        oldAtmidOfLogCollectionNewLog.getLogCollection().remove(logCollectionNewLog);
                        oldAtmidOfLogCollectionNewLog = em.merge(oldAtmidOfLogCollectionNewLog);
                    }
                }
            }
            for (Stock stockCollectionNewStock : stockCollectionNew) {
                if (!stockCollectionOld.contains(stockCollectionNewStock)) {
                    Atm oldAtmOfStockCollectionNewStock = stockCollectionNewStock.getAtm();
                    stockCollectionNewStock.setAtm(atm);
                    stockCollectionNewStock = em.merge(stockCollectionNewStock);
                    if (oldAtmOfStockCollectionNewStock != null && !oldAtmOfStockCollectionNewStock.equals(atm)) {
                        oldAtmOfStockCollectionNewStock.getStockCollection().remove(stockCollectionNewStock);
                        oldAtmOfStockCollectionNewStock = em.merge(oldAtmOfStockCollectionNewStock);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = atm.getAtmid();
                if (findAtm(id) == null) {
                    throw new NonexistentEntityException("The atm with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atm atm;
            try {
                atm = em.getReference(Atm.class, id);
                atm.getAtmid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atm with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Stock> stockCollectionOrphanCheck = atm.getStockCollection();
            for (Stock stockCollectionOrphanCheckStock : stockCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Atm (" + atm + ") cannot be destroyed since the Stock " + stockCollectionOrphanCheckStock + " in its stockCollection field has a non-nullable atm field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Log> logCollection = atm.getLogCollection();
            for (Log logCollectionLog : logCollection) {
                logCollectionLog.setAtmid(null);
                logCollectionLog = em.merge(logCollectionLog);
            }
            em.remove(atm);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Atm> findAtmEntities() {
        return findAtmEntities(true, -1, -1);
    }

    public List<Atm> findAtmEntities(int maxResults, int firstResult) {
        return findAtmEntities(false, maxResults, firstResult);
    }

    private List<Atm> findAtmEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Atm.class));
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

    public Atm findAtm(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Atm.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtmCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Atm> rt = cq.from(Atm.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
