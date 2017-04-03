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
import entity.Account;
import entity.Customer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class CustomerJpaController implements Serializable {

    public CustomerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customer customer) throws PreexistingEntityException, Exception {
        if (customer.getAccountCollection() == null) {
            customer.setAccountCollection(new ArrayList<Account>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Account> attachedAccountCollection = new ArrayList<Account>();
            for (Account accountCollectionAccountToAttach : customer.getAccountCollection()) {
                accountCollectionAccountToAttach = em.getReference(accountCollectionAccountToAttach.getClass(), accountCollectionAccountToAttach.getAccountID());
                attachedAccountCollection.add(accountCollectionAccountToAttach);
            }
            customer.setAccountCollection(attachedAccountCollection);
            em.persist(customer);
            for (Account accountCollectionAccount : customer.getAccountCollection()) {
                Customer oldCustIDOfAccountCollectionAccount = accountCollectionAccount.getCustID();
                accountCollectionAccount.setCustID(customer);
                accountCollectionAccount = em.merge(accountCollectionAccount);
                if (oldCustIDOfAccountCollectionAccount != null) {
                    oldCustIDOfAccountCollectionAccount.getAccountCollection().remove(accountCollectionAccount);
                    oldCustIDOfAccountCollectionAccount = em.merge(oldCustIDOfAccountCollectionAccount);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCustomer(customer.getCustID()) != null) {
                throw new PreexistingEntityException("Customer " + customer + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customer customer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer persistentCustomer = em.find(Customer.class, customer.getCustID());
            Collection<Account> accountCollectionOld = persistentCustomer.getAccountCollection();
            Collection<Account> accountCollectionNew = customer.getAccountCollection();
            Collection<Account> attachedAccountCollectionNew = new ArrayList<Account>();
            for (Account accountCollectionNewAccountToAttach : accountCollectionNew) {
                accountCollectionNewAccountToAttach = em.getReference(accountCollectionNewAccountToAttach.getClass(), accountCollectionNewAccountToAttach.getAccountID());
                attachedAccountCollectionNew.add(accountCollectionNewAccountToAttach);
            }
            accountCollectionNew = attachedAccountCollectionNew;
            customer.setAccountCollection(accountCollectionNew);
            customer = em.merge(customer);
            for (Account accountCollectionOldAccount : accountCollectionOld) {
                if (!accountCollectionNew.contains(accountCollectionOldAccount)) {
                    accountCollectionOldAccount.setCustID(null);
                    accountCollectionOldAccount = em.merge(accountCollectionOldAccount);
                }
            }
            for (Account accountCollectionNewAccount : accountCollectionNew) {
                if (!accountCollectionOld.contains(accountCollectionNewAccount)) {
                    Customer oldCustIDOfAccountCollectionNewAccount = accountCollectionNewAccount.getCustID();
                    accountCollectionNewAccount.setCustID(customer);
                    accountCollectionNewAccount = em.merge(accountCollectionNewAccount);
                    if (oldCustIDOfAccountCollectionNewAccount != null && !oldCustIDOfAccountCollectionNewAccount.equals(customer)) {
                        oldCustIDOfAccountCollectionNewAccount.getAccountCollection().remove(accountCollectionNewAccount);
                        oldCustIDOfAccountCollectionNewAccount = em.merge(oldCustIDOfAccountCollectionNewAccount);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = customer.getCustID();
                if (findCustomer(id) == null) {
                    throw new NonexistentEntityException("The customer with id " + id + " no longer exists.");
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
            Customer customer;
            try {
                customer = em.getReference(Customer.class, id);
                customer.getCustID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
            }
            Collection<Account> accountCollection = customer.getAccountCollection();
            for (Account accountCollectionAccount : accountCollection) {
                accountCollectionAccount.setCustID(null);
                accountCollectionAccount = em.merge(accountCollectionAccount);
            }
            em.remove(customer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customer> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Customer> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Customer> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customer.class));
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

    public Customer findCustomer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customer> rt = cq.from(Customer.class);
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
import entity.Account;
import entity.Customer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HUY-PC
 */
public class CustomerJpaController implements Serializable {

    public CustomerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customer customer) throws PreexistingEntityException, Exception {
        if (customer.getAccountCollection() == null) {
            customer.setAccountCollection(new ArrayList<Account>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Account> attachedAccountCollection = new ArrayList<Account>();
            for (Account accountCollectionAccountToAttach : customer.getAccountCollection()) {
                accountCollectionAccountToAttach = em.getReference(accountCollectionAccountToAttach.getClass(), accountCollectionAccountToAttach.getAccountID());
                attachedAccountCollection.add(accountCollectionAccountToAttach);
            }
            customer.setAccountCollection(attachedAccountCollection);
            em.persist(customer);
            for (Account accountCollectionAccount : customer.getAccountCollection()) {
                Customer oldCustIDOfAccountCollectionAccount = accountCollectionAccount.getCustID();
                accountCollectionAccount.setCustID(customer);
                accountCollectionAccount = em.merge(accountCollectionAccount);
                if (oldCustIDOfAccountCollectionAccount != null) {
                    oldCustIDOfAccountCollectionAccount.getAccountCollection().remove(accountCollectionAccount);
                    oldCustIDOfAccountCollectionAccount = em.merge(oldCustIDOfAccountCollectionAccount);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCustomer(customer.getCustID()) != null) {
                throw new PreexistingEntityException("Customer " + customer + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customer customer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer persistentCustomer = em.find(Customer.class, customer.getCustID());
            Collection<Account> accountCollectionOld = persistentCustomer.getAccountCollection();
            Collection<Account> accountCollectionNew = customer.getAccountCollection();
            Collection<Account> attachedAccountCollectionNew = new ArrayList<Account>();
            for (Account accountCollectionNewAccountToAttach : accountCollectionNew) {
                accountCollectionNewAccountToAttach = em.getReference(accountCollectionNewAccountToAttach.getClass(), accountCollectionNewAccountToAttach.getAccountID());
                attachedAccountCollectionNew.add(accountCollectionNewAccountToAttach);
            }
            accountCollectionNew = attachedAccountCollectionNew;
            customer.setAccountCollection(accountCollectionNew);
            customer = em.merge(customer);
            for (Account accountCollectionOldAccount : accountCollectionOld) {
                if (!accountCollectionNew.contains(accountCollectionOldAccount)) {
                    accountCollectionOldAccount.setCustID(null);
                    accountCollectionOldAccount = em.merge(accountCollectionOldAccount);
                }
            }
            for (Account accountCollectionNewAccount : accountCollectionNew) {
                if (!accountCollectionOld.contains(accountCollectionNewAccount)) {
                    Customer oldCustIDOfAccountCollectionNewAccount = accountCollectionNewAccount.getCustID();
                    accountCollectionNewAccount.setCustID(customer);
                    accountCollectionNewAccount = em.merge(accountCollectionNewAccount);
                    if (oldCustIDOfAccountCollectionNewAccount != null && !oldCustIDOfAccountCollectionNewAccount.equals(customer)) {
                        oldCustIDOfAccountCollectionNewAccount.getAccountCollection().remove(accountCollectionNewAccount);
                        oldCustIDOfAccountCollectionNewAccount = em.merge(oldCustIDOfAccountCollectionNewAccount);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = customer.getCustID();
                if (findCustomer(id) == null) {
                    throw new NonexistentEntityException("The customer with id " + id + " no longer exists.");
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
            Customer customer;
            try {
                customer = em.getReference(Customer.class, id);
                customer.getCustID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
            }
            Collection<Account> accountCollection = customer.getAccountCollection();
            for (Account accountCollectionAccount : accountCollection) {
                accountCollectionAccount.setCustID(null);
                accountCollectionAccount = em.merge(accountCollectionAccount);
            }
            em.remove(customer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customer> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Customer> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Customer> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customer.class));
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

    public Customer findCustomer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customer> rt = cq.from(Customer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
>>>>>>> acf0e3267d851f3e4de6d24aebaa5bce97662785
