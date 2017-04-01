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
import entity.Account;
import entity.Card;
import entity.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class CardJpaController implements Serializable {

    public CardJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Card card) throws PreexistingEntityException, Exception {
        if (card.getLogCollection() == null) {
            card.setLogCollection(new ArrayList<Log>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Account accountID = card.getAccountID();
            if (accountID != null) {
                accountID = em.getReference(accountID.getClass(), accountID.getAccountID());
                card.setAccountID(accountID);
            }
            Collection<Log> attachedLogCollection = new ArrayList<Log>();
            for (Log logCollectionLogToAttach : card.getLogCollection()) {
                logCollectionLogToAttach = em.getReference(logCollectionLogToAttach.getClass(), logCollectionLogToAttach.getLogID());
                attachedLogCollection.add(logCollectionLogToAttach);
            }
            card.setLogCollection(attachedLogCollection);
            em.persist(card);
            if (accountID != null) {
                accountID.getCardCollection().add(card);
                accountID = em.merge(accountID);
            }
            for (Log logCollectionLog : card.getLogCollection()) {
                Card oldCardNoOfLogCollectionLog = logCollectionLog.getCardNo();
                logCollectionLog.setCardNo(card);
                logCollectionLog = em.merge(logCollectionLog);
                if (oldCardNoOfLogCollectionLog != null) {
                    oldCardNoOfLogCollectionLog.getLogCollection().remove(logCollectionLog);
                    oldCardNoOfLogCollectionLog = em.merge(oldCardNoOfLogCollectionLog);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCard(card.getCardNo()) != null) {
                throw new PreexistingEntityException("Card " + card + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Card card) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Card persistentCard = em.find(Card.class, card.getCardNo());
            Account accountIDOld = persistentCard.getAccountID();
            Account accountIDNew = card.getAccountID();
            Collection<Log> logCollectionOld = persistentCard.getLogCollection();
            Collection<Log> logCollectionNew = card.getLogCollection();
            if (accountIDNew != null) {
                accountIDNew = em.getReference(accountIDNew.getClass(), accountIDNew.getAccountID());
                card.setAccountID(accountIDNew);
            }
            Collection<Log> attachedLogCollectionNew = new ArrayList<Log>();
            for (Log logCollectionNewLogToAttach : logCollectionNew) {
                logCollectionNewLogToAttach = em.getReference(logCollectionNewLogToAttach.getClass(), logCollectionNewLogToAttach.getLogID());
                attachedLogCollectionNew.add(logCollectionNewLogToAttach);
            }
            logCollectionNew = attachedLogCollectionNew;
            card.setLogCollection(logCollectionNew);
            card = em.merge(card);
            if (accountIDOld != null && !accountIDOld.equals(accountIDNew)) {
                accountIDOld.getCardCollection().remove(card);
                accountIDOld = em.merge(accountIDOld);
            }
            if (accountIDNew != null && !accountIDNew.equals(accountIDOld)) {
                accountIDNew.getCardCollection().add(card);
                accountIDNew = em.merge(accountIDNew);
            }
            for (Log logCollectionOldLog : logCollectionOld) {
                if (!logCollectionNew.contains(logCollectionOldLog)) {
                    logCollectionOldLog.setCardNo(null);
                    logCollectionOldLog = em.merge(logCollectionOldLog);
                }
            }
            for (Log logCollectionNewLog : logCollectionNew) {
                if (!logCollectionOld.contains(logCollectionNewLog)) {
                    Card oldCardNoOfLogCollectionNewLog = logCollectionNewLog.getCardNo();
                    logCollectionNewLog.setCardNo(card);
                    logCollectionNewLog = em.merge(logCollectionNewLog);
                    if (oldCardNoOfLogCollectionNewLog != null && !oldCardNoOfLogCollectionNewLog.equals(card)) {
                        oldCardNoOfLogCollectionNewLog.getLogCollection().remove(logCollectionNewLog);
                        oldCardNoOfLogCollectionNewLog = em.merge(oldCardNoOfLogCollectionNewLog);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = card.getCardNo();
                if (findCard(id) == null) {
                    throw new NonexistentEntityException("The card with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Card card;
            try {
                card = em.getReference(Card.class, id);
                card.getCardNo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The card with id " + id + " no longer exists.", enfe);
            }
            Account accountID = card.getAccountID();
            if (accountID != null) {
                accountID.getCardCollection().remove(card);
                accountID = em.merge(accountID);
            }
            Collection<Log> logCollection = card.getLogCollection();
            for (Log logCollectionLog : logCollection) {
                logCollectionLog.setCardNo(null);
                logCollectionLog = em.merge(logCollectionLog);
            }
            em.remove(card);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Card> findCardEntities() {
        return findCardEntities(true, -1, -1);
    }

    public List<Card> findCardEntities(int maxResults, int firstResult) {
        return findCardEntities(false, maxResults, firstResult);
    }

    private List<Card> findCardEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Card.class));
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

    public Card findCard(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Card.class, id);
        } finally {
            em.close();
        }
    }

    public int getCardCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Card> rt = cq.from(Card.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
