<<<<<<< HEAD
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
import entity.Log;
import entity.LogType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class LogTypeJpaController implements Serializable {

    public LogTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LogType logType) throws PreexistingEntityException, Exception {
        if (logType.getLogCollection() == null) {
            logType.setLogCollection(new ArrayList<Log>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Log> attachedLogCollection = new ArrayList<Log>();
            for (Log logCollectionLogToAttach : logType.getLogCollection()) {
                logCollectionLogToAttach = em.getReference(logCollectionLogToAttach.getClass(), logCollectionLogToAttach.getLogID());
                attachedLogCollection.add(logCollectionLogToAttach);
            }
            logType.setLogCollection(attachedLogCollection);
            em.persist(logType);
            for (Log logCollectionLog : logType.getLogCollection()) {
                LogType oldLogTypeIDOfLogCollectionLog = logCollectionLog.getLogTypeID();
                logCollectionLog.setLogTypeID(logType);
                logCollectionLog = em.merge(logCollectionLog);
                if (oldLogTypeIDOfLogCollectionLog != null) {
                    oldLogTypeIDOfLogCollectionLog.getLogCollection().remove(logCollectionLog);
                    oldLogTypeIDOfLogCollectionLog = em.merge(oldLogTypeIDOfLogCollectionLog);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLogType(logType.getLogTypeID()) != null) {
                throw new PreexistingEntityException("LogType " + logType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LogType logType) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogType persistentLogType = em.find(LogType.class, logType.getLogTypeID());
            Collection<Log> logCollectionOld = persistentLogType.getLogCollection();
            Collection<Log> logCollectionNew = logType.getLogCollection();
            Collection<Log> attachedLogCollectionNew = new ArrayList<Log>();
            for (Log logCollectionNewLogToAttach : logCollectionNew) {
                logCollectionNewLogToAttach = em.getReference(logCollectionNewLogToAttach.getClass(), logCollectionNewLogToAttach.getLogID());
                attachedLogCollectionNew.add(logCollectionNewLogToAttach);
            }
            logCollectionNew = attachedLogCollectionNew;
            logType.setLogCollection(logCollectionNew);
            logType = em.merge(logType);
            for (Log logCollectionOldLog : logCollectionOld) {
                if (!logCollectionNew.contains(logCollectionOldLog)) {
                    logCollectionOldLog.setLogTypeID(null);
                    logCollectionOldLog = em.merge(logCollectionOldLog);
                }
            }
            for (Log logCollectionNewLog : logCollectionNew) {
                if (!logCollectionOld.contains(logCollectionNewLog)) {
                    LogType oldLogTypeIDOfLogCollectionNewLog = logCollectionNewLog.getLogTypeID();
                    logCollectionNewLog.setLogTypeID(logType);
                    logCollectionNewLog = em.merge(logCollectionNewLog);
                    if (oldLogTypeIDOfLogCollectionNewLog != null && !oldLogTypeIDOfLogCollectionNewLog.equals(logType)) {
                        oldLogTypeIDOfLogCollectionNewLog.getLogCollection().remove(logCollectionNewLog);
                        oldLogTypeIDOfLogCollectionNewLog = em.merge(oldLogTypeIDOfLogCollectionNewLog);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logType.getLogTypeID();
                if (findLogType(id) == null) {
                    throw new NonexistentEntityException("The logType with id " + id + " no longer exists.");
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
            LogType logType;
            try {
                logType = em.getReference(LogType.class, id);
                logType.getLogTypeID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logType with id " + id + " no longer exists.", enfe);
            }
            Collection<Log> logCollection = logType.getLogCollection();
            for (Log logCollectionLog : logCollection) {
                logCollectionLog.setLogTypeID(null);
                logCollectionLog = em.merge(logCollectionLog);
            }
            em.remove(logType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LogType> findLogTypeEntities() {
        return findLogTypeEntities(true, -1, -1);
    }

    public List<LogType> findLogTypeEntities(int maxResults, int firstResult) {
        return findLogTypeEntities(false, maxResults, firstResult);
    }

    private List<LogType> findLogTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LogType.class));
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

    public LogType findLogType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LogType.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LogType> rt = cq.from(LogType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
=======
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
import entity.Log;
import entity.LogType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class LogTypeJpaController implements Serializable {

    public LogTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LogType logType) throws PreexistingEntityException, Exception {
        if (logType.getLogCollection() == null) {
            logType.setLogCollection(new ArrayList<Log>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Log> attachedLogCollection = new ArrayList<Log>();
            for (Log logCollectionLogToAttach : logType.getLogCollection()) {
                logCollectionLogToAttach = em.getReference(logCollectionLogToAttach.getClass(), logCollectionLogToAttach.getLogID());
                attachedLogCollection.add(logCollectionLogToAttach);
            }
            logType.setLogCollection(attachedLogCollection);
            em.persist(logType);
            for (Log logCollectionLog : logType.getLogCollection()) {
                LogType oldLogTypeIDOfLogCollectionLog = logCollectionLog.getLogTypeID();
                logCollectionLog.setLogTypeID(logType);
                logCollectionLog = em.merge(logCollectionLog);
                if (oldLogTypeIDOfLogCollectionLog != null) {
                    oldLogTypeIDOfLogCollectionLog.getLogCollection().remove(logCollectionLog);
                    oldLogTypeIDOfLogCollectionLog = em.merge(oldLogTypeIDOfLogCollectionLog);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLogType(logType.getLogTypeID()) != null) {
                throw new PreexistingEntityException("LogType " + logType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LogType logType) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogType persistentLogType = em.find(LogType.class, logType.getLogTypeID());
            Collection<Log> logCollectionOld = persistentLogType.getLogCollection();
            Collection<Log> logCollectionNew = logType.getLogCollection();
            Collection<Log> attachedLogCollectionNew = new ArrayList<Log>();
            for (Log logCollectionNewLogToAttach : logCollectionNew) {
                logCollectionNewLogToAttach = em.getReference(logCollectionNewLogToAttach.getClass(), logCollectionNewLogToAttach.getLogID());
                attachedLogCollectionNew.add(logCollectionNewLogToAttach);
            }
            logCollectionNew = attachedLogCollectionNew;
            logType.setLogCollection(logCollectionNew);
            logType = em.merge(logType);
            for (Log logCollectionOldLog : logCollectionOld) {
                if (!logCollectionNew.contains(logCollectionOldLog)) {
                    logCollectionOldLog.setLogTypeID(null);
                    logCollectionOldLog = em.merge(logCollectionOldLog);
                }
            }
            for (Log logCollectionNewLog : logCollectionNew) {
                if (!logCollectionOld.contains(logCollectionNewLog)) {
                    LogType oldLogTypeIDOfLogCollectionNewLog = logCollectionNewLog.getLogTypeID();
                    logCollectionNewLog.setLogTypeID(logType);
                    logCollectionNewLog = em.merge(logCollectionNewLog);
                    if (oldLogTypeIDOfLogCollectionNewLog != null && !oldLogTypeIDOfLogCollectionNewLog.equals(logType)) {
                        oldLogTypeIDOfLogCollectionNewLog.getLogCollection().remove(logCollectionNewLog);
                        oldLogTypeIDOfLogCollectionNewLog = em.merge(oldLogTypeIDOfLogCollectionNewLog);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logType.getLogTypeID();
                if (findLogType(id) == null) {
                    throw new NonexistentEntityException("The logType with id " + id + " no longer exists.");
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
            LogType logType;
            try {
                logType = em.getReference(LogType.class, id);
                logType.getLogTypeID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logType with id " + id + " no longer exists.", enfe);
            }
            Collection<Log> logCollection = logType.getLogCollection();
            for (Log logCollectionLog : logCollection) {
                logCollectionLog.setLogTypeID(null);
                logCollectionLog = em.merge(logCollectionLog);
            }
            em.remove(logType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LogType> findLogTypeEntities() {
        return findLogTypeEntities(true, -1, -1);
    }

    public List<LogType> findLogTypeEntities(int maxResults, int firstResult) {
        return findLogTypeEntities(false, maxResults, firstResult);
    }

    private List<LogType> findLogTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LogType.class));
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

    public LogType findLogType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LogType.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LogType> rt = cq.from(LogType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
>>>>>>> acf0e3267d851f3e4de6d24aebaa5bce97662785
