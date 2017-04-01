/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPAController;

import JPAController.exceptions.NonexistentEntityException;
import JPAController.exceptions.PreexistingEntityException;
import entity.Account;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Customer;
import entity.Card;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class AccountJpaController implements Serializable {

    public AccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Account account) throws PreexistingEntityException, Exception {
        if (account.getCardCollection() == null) {
            account.setCardCollection(new ArrayList<Card>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer custID = account.getCustID();
            if (custID != null) {
                custID = em.getReference(custID.getClass(), custID.getCustID());
                account.setCustID(custID);
            }
            Collection<Card> attachedCardCollection = new ArrayList<Card>();
            for (Card cardCollectionCardToAttach : account.getCardCollection()) {
                cardCollectionCardToAttach = em.getReference(cardCollectionCardToAttach.getClass(), cardCollectionCardToAttach.getCardNo());
                attachedCardCollection.add(cardCollectionCardToAttach);
            }
            account.setCardCollection(attachedCardCollection);
            em.persist(account);
            if (custID != null) {
                custID.getAccountCollection().add(account);
                custID = em.merge(custID);
            }
            for (Card cardCollectionCard : account.getCardCollection()) {
                Account oldAccountIDOfCardCollectionCard = cardCollectionCard.getAccountID();
                cardCollectionCard.setAccountID(account);
                cardCollectionCard = em.merge(cardCollectionCard);
                if (oldAccountIDOfCardCollectionCard != null) {
                    oldAccountIDOfCardCollectionCard.getCardCollection().remove(cardCollectionCard);
                    oldAccountIDOfCardCollectionCard = em.merge(oldAccountIDOfCardCollectionCard);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAccount(account.getAccountID()) != null) {
                throw new PreexistingEntityException("Account " + account + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Account account) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Account persistentAccount = em.find(Account.class, account.getAccountID());
            Customer custIDOld = persistentAccount.getCustID();
            Customer custIDNew = account.getCustID();
            Collection<Card> cardCollectionOld = persistentAccount.getCardCollection();
            Collection<Card> cardCollectionNew = account.getCardCollection();
            if (custIDNew != null) {
                custIDNew = em.getReference(custIDNew.getClass(), custIDNew.getCustID());
                account.setCustID(custIDNew);
            }
            Collection<Card> attachedCardCollectionNew = new ArrayList<Card>();
            for (Card cardCollectionNewCardToAttach : cardCollectionNew) {
                cardCollectionNewCardToAttach = em.getReference(cardCollectionNewCardToAttach.getClass(), cardCollectionNewCardToAttach.getCardNo());
                attachedCardCollectionNew.add(cardCollectionNewCardToAttach);
            }
            cardCollectionNew = attachedCardCollectionNew;
            account.setCardCollection(cardCollectionNew);
            account = em.merge(account);
            if (custIDOld != null && !custIDOld.equals(custIDNew)) {
                custIDOld.getAccountCollection().remove(account);
                custIDOld = em.merge(custIDOld);
            }
            if (custIDNew != null && !custIDNew.equals(custIDOld)) {
                custIDNew.getAccountCollection().add(account);
                custIDNew = em.merge(custIDNew);
            }
            for (Card cardCollectionOldCard : cardCollectionOld) {
                if (!cardCollectionNew.contains(cardCollectionOldCard)) {
                    cardCollectionOldCard.setAccountID(null);
                    cardCollectionOldCard = em.merge(cardCollectionOldCard);
                }
            }
            for (Card cardCollectionNewCard : cardCollectionNew) {
                if (!cardCollectionOld.contains(cardCollectionNewCard)) {
                    Account oldAccountIDOfCardCollectionNewCard = cardCollectionNewCard.getAccountID();
                    cardCollectionNewCard.setAccountID(account);
                    cardCollectionNewCard = em.merge(cardCollectionNewCard);
                    if (oldAccountIDOfCardCollectionNewCard != null && !oldAccountIDOfCardCollectionNewCard.equals(account)) {
                        oldAccountIDOfCardCollectionNewCard.getCardCollection().remove(cardCollectionNewCard);
                        oldAccountIDOfCardCollectionNewCard = em.merge(oldAccountIDOfCardCollectionNewCard);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = account.getAccountID();
                if (findAccount(id) == null) {
                    throw new NonexistentEntityException("The account with id " + id + " no longer exists.");
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
            Account account;
            try {
                account = em.getReference(Account.class, id);
                account.getAccountID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The account with id " + id + " no longer exists.", enfe);
            }
            Customer custID = account.getCustID();
            if (custID != null) {
                custID.getAccountCollection().remove(account);
                custID = em.merge(custID);
            }
            Collection<Card> cardCollection = account.getCardCollection();
            for (Card cardCollectionCard : cardCollection) {
                cardCollectionCard.setAccountID(null);
                cardCollectionCard = em.merge(cardCollectionCard);
            }
            em.remove(account);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Account> findAccountEntities() {
        return findAccountEntities(true, -1, -1);
    }

    public List<Account> findAccountEntities(int maxResults, int firstResult) {
        return findAccountEntities(false, maxResults, firstResult);
    }

    private List<Account> findAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Account.class));
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

    public Account findAccount(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Account.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Account> rt = cq.from(Account.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
