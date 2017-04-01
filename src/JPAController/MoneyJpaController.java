/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPAController;

import JPAController.exceptions.IllegalOrphanException;
import JPAController.exceptions.NonexistentEntityException;
import JPAController.exceptions.PreexistingEntityException;
import entity.Money;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Stock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class MoneyJpaController implements Serializable {

    public MoneyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Money money) throws PreexistingEntityException, Exception {
        if (money.getStockCollection() == null) {
            money.setStockCollection(new ArrayList<Stock>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Stock> attachedStockCollection = new ArrayList<Stock>();
            for (Stock stockCollectionStockToAttach : money.getStockCollection()) {
                stockCollectionStockToAttach = em.getReference(stockCollectionStockToAttach.getClass(), stockCollectionStockToAttach.getStockPK());
                attachedStockCollection.add(stockCollectionStockToAttach);
            }
            money.setStockCollection(attachedStockCollection);
            em.persist(money);
            for (Stock stockCollectionStock : money.getStockCollection()) {
                Money oldMoneyOfStockCollectionStock = stockCollectionStock.getMoney();
                stockCollectionStock.setMoney(money);
                stockCollectionStock = em.merge(stockCollectionStock);
                if (oldMoneyOfStockCollectionStock != null) {
                    oldMoneyOfStockCollectionStock.getStockCollection().remove(stockCollectionStock);
                    oldMoneyOfStockCollectionStock = em.merge(oldMoneyOfStockCollectionStock);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMoney(money.getMoneyID()) != null) {
                throw new PreexistingEntityException("Money " + money + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Money money) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Money persistentMoney = em.find(Money.class, money.getMoneyID());
            Collection<Stock> stockCollectionOld = persistentMoney.getStockCollection();
            Collection<Stock> stockCollectionNew = money.getStockCollection();
            List<String> illegalOrphanMessages = null;
            for (Stock stockCollectionOldStock : stockCollectionOld) {
                if (!stockCollectionNew.contains(stockCollectionOldStock)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Stock " + stockCollectionOldStock + " since its money field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Stock> attachedStockCollectionNew = new ArrayList<Stock>();
            for (Stock stockCollectionNewStockToAttach : stockCollectionNew) {
                stockCollectionNewStockToAttach = em.getReference(stockCollectionNewStockToAttach.getClass(), stockCollectionNewStockToAttach.getStockPK());
                attachedStockCollectionNew.add(stockCollectionNewStockToAttach);
            }
            stockCollectionNew = attachedStockCollectionNew;
            money.setStockCollection(stockCollectionNew);
            money = em.merge(money);
            for (Stock stockCollectionNewStock : stockCollectionNew) {
                if (!stockCollectionOld.contains(stockCollectionNewStock)) {
                    Money oldMoneyOfStockCollectionNewStock = stockCollectionNewStock.getMoney();
                    stockCollectionNewStock.setMoney(money);
                    stockCollectionNewStock = em.merge(stockCollectionNewStock);
                    if (oldMoneyOfStockCollectionNewStock != null && !oldMoneyOfStockCollectionNewStock.equals(money)) {
                        oldMoneyOfStockCollectionNewStock.getStockCollection().remove(stockCollectionNewStock);
                        oldMoneyOfStockCollectionNewStock = em.merge(oldMoneyOfStockCollectionNewStock);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = money.getMoneyID();
                if (findMoney(id) == null) {
                    throw new NonexistentEntityException("The money with id " + id + " no longer exists.");
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
            Money money;
            try {
                money = em.getReference(Money.class, id);
                money.getMoneyID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The money with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Stock> stockCollectionOrphanCheck = money.getStockCollection();
            for (Stock stockCollectionOrphanCheckStock : stockCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Money (" + money + ") cannot be destroyed since the Stock " + stockCollectionOrphanCheckStock + " in its stockCollection field has a non-nullable money field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(money);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Money> findMoneyEntities() {
        return findMoneyEntities(true, -1, -1);
    }

    public List<Money> findMoneyEntities(int maxResults, int firstResult) {
        return findMoneyEntities(false, maxResults, firstResult);
    }

    private List<Money> findMoneyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Money.class));
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

    public Money findMoney(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Money.class, id);
        } finally {
            em.close();
        }
    }

    public int getMoneyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Money> rt = cq.from(Money.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
