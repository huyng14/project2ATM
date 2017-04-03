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
import entity.Atm;
import entity.Money;
import entity.Stock;
import entity.StockPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class StockJpaController implements Serializable {

    public StockJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stock stock) throws PreexistingEntityException, Exception {
        if (stock.getStockPK() == null) {
            stock.setStockPK(new StockPK());
        }
        stock.getStockPK().setAtmid(stock.getAtm().getAtmid());
        stock.getStockPK().setMoneyID(stock.getMoney().getMoneyID());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atm atm = stock.getAtm();
            if (atm != null) {
                atm = em.getReference(atm.getClass(), atm.getAtmid());
                stock.setAtm(atm);
            }
            Money money = stock.getMoney();
            if (money != null) {
                money = em.getReference(money.getClass(), money.getMoneyID());
                stock.setMoney(money);
            }
            em.persist(stock);
            if (atm != null) {
                atm.getStockCollection().add(stock);
                atm = em.merge(atm);
            }
            if (money != null) {
                money.getStockCollection().add(stock);
                money = em.merge(money);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStock(stock.getStockPK()) != null) {
                throw new PreexistingEntityException("Stock " + stock + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stock stock) throws NonexistentEntityException, Exception {
        stock.getStockPK().setAtmid(stock.getAtm().getAtmid());
        stock.getStockPK().setMoneyID(stock.getMoney().getMoneyID());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock persistentStock = em.find(Stock.class, stock.getStockPK());
            Atm atmOld = persistentStock.getAtm();
            Atm atmNew = stock.getAtm();
            Money moneyOld = persistentStock.getMoney();
            Money moneyNew = stock.getMoney();
            if (atmNew != null) {
                atmNew = em.getReference(atmNew.getClass(), atmNew.getAtmid());
                stock.setAtm(atmNew);
            }
            if (moneyNew != null) {
                moneyNew = em.getReference(moneyNew.getClass(), moneyNew.getMoneyID());
                stock.setMoney(moneyNew);
            }
            stock = em.merge(stock);
            if (atmOld != null && !atmOld.equals(atmNew)) {
                atmOld.getStockCollection().remove(stock);
                atmOld = em.merge(atmOld);
            }
            if (atmNew != null && !atmNew.equals(atmOld)) {
                atmNew.getStockCollection().add(stock);
                atmNew = em.merge(atmNew);
            }
            if (moneyOld != null && !moneyOld.equals(moneyNew)) {
                moneyOld.getStockCollection().remove(stock);
                moneyOld = em.merge(moneyOld);
            }
            if (moneyNew != null && !moneyNew.equals(moneyOld)) {
                moneyNew.getStockCollection().add(stock);
                moneyNew = em.merge(moneyNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                StockPK id = stock.getStockPK();
                if (findStock(id) == null) {
                    throw new NonexistentEntityException("The stock with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(StockPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock stock;
            try {
                stock = em.getReference(Stock.class, id);
                stock.getStockPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stock with id " + id + " no longer exists.", enfe);
            }
            Atm atm = stock.getAtm();
            if (atm != null) {
                atm.getStockCollection().remove(stock);
                atm = em.merge(atm);
            }
            Money money = stock.getMoney();
            if (money != null) {
                money.getStockCollection().remove(stock);
                money = em.merge(money);
            }
            em.remove(stock);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stock> findStockEntities() {
        return findStockEntities(true, -1, -1);
    }

    public List<Stock> findStockEntities(int maxResults, int firstResult) {
        return findStockEntities(false, maxResults, firstResult);
    }

    private List<Stock> findStockEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stock.class));
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

    public Stock findStock(StockPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stock.class, id);
        } finally {
            em.close();
        }
    }

    public int getStockCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stock> rt = cq.from(Stock.class);
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
import entity.Atm;
import entity.Money;
import entity.Stock;
import entity.StockPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class StockJpaController implements Serializable {

    public StockJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stock stock) throws PreexistingEntityException, Exception {
        if (stock.getStockPK() == null) {
            stock.setStockPK(new StockPK());
        }
        stock.getStockPK().setAtmid(stock.getAtm().getAtmid());
        stock.getStockPK().setMoneyID(stock.getMoney().getMoneyID());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atm atm = stock.getAtm();
            if (atm != null) {
                atm = em.getReference(atm.getClass(), atm.getAtmid());
                stock.setAtm(atm);
            }
            Money money = stock.getMoney();
            if (money != null) {
                money = em.getReference(money.getClass(), money.getMoneyID());
                stock.setMoney(money);
            }
            em.persist(stock);
            if (atm != null) {
                atm.getStockCollection().add(stock);
                atm = em.merge(atm);
            }
            if (money != null) {
                money.getStockCollection().add(stock);
                money = em.merge(money);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStock(stock.getStockPK()) != null) {
                throw new PreexistingEntityException("Stock " + stock + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stock stock) throws NonexistentEntityException, Exception {
        stock.getStockPK().setAtmid(stock.getAtm().getAtmid());
        stock.getStockPK().setMoneyID(stock.getMoney().getMoneyID());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock persistentStock = em.find(Stock.class, stock.getStockPK());
            Atm atmOld = persistentStock.getAtm();
            Atm atmNew = stock.getAtm();
            Money moneyOld = persistentStock.getMoney();
            Money moneyNew = stock.getMoney();
            if (atmNew != null) {
                atmNew = em.getReference(atmNew.getClass(), atmNew.getAtmid());
                stock.setAtm(atmNew);
            }
            if (moneyNew != null) {
                moneyNew = em.getReference(moneyNew.getClass(), moneyNew.getMoneyID());
                stock.setMoney(moneyNew);
            }
            stock = em.merge(stock);
            if (atmOld != null && !atmOld.equals(atmNew)) {
                atmOld.getStockCollection().remove(stock);
                atmOld = em.merge(atmOld);
            }
            if (atmNew != null && !atmNew.equals(atmOld)) {
                atmNew.getStockCollection().add(stock);
                atmNew = em.merge(atmNew);
            }
            if (moneyOld != null && !moneyOld.equals(moneyNew)) {
                moneyOld.getStockCollection().remove(stock);
                moneyOld = em.merge(moneyOld);
            }
            if (moneyNew != null && !moneyNew.equals(moneyOld)) {
                moneyNew.getStockCollection().add(stock);
                moneyNew = em.merge(moneyNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                StockPK id = stock.getStockPK();
                if (findStock(id) == null) {
                    throw new NonexistentEntityException("The stock with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(StockPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock stock;
            try {
                stock = em.getReference(Stock.class, id);
                stock.getStockPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stock with id " + id + " no longer exists.", enfe);
            }
            Atm atm = stock.getAtm();
            if (atm != null) {
                atm.getStockCollection().remove(stock);
                atm = em.merge(atm);
            }
            Money money = stock.getMoney();
            if (money != null) {
                money.getStockCollection().remove(stock);
                money = em.merge(money);
            }
            em.remove(stock);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stock> findStockEntities() {
        return findStockEntities(true, -1, -1);
    }

    public List<Stock> findStockEntities(int maxResults, int firstResult) {
        return findStockEntities(false, maxResults, firstResult);
    }

    private List<Stock> findStockEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stock.class));
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

    public Stock findStock(StockPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stock.class, id);
        } finally {
            em.close();
        }
    }

    public int getStockCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stock> rt = cq.from(Stock.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
>>>>>>> acf0e3267d851f3e4de6d24aebaa5bce97662785
