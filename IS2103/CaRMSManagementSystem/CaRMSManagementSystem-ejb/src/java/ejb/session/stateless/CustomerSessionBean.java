/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import util.exception.CustomerEmailExistsException;
import util.exception.InvalidLoginException;

/**
 *
 * @author 60540
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCustomer(/*Long partnerId, */Customer customer) throws CustomerEmailExistsException {
        
        Query query = em.createQuery("SELECT m FROM Customer m WHERE m.customerEmail = :inEmail");
        query.setParameter("inEmail", customer.getCustomerEmail());
        
        Customer c;
        try {
            c = (Customer)query.getSingleResult();
            throw new CustomerEmailExistsException("This Email Has Already Being Registered.");
        }
        catch(NoResultException ex1) { //create a new customer
            try 
            {
                em.persist(customer);
                em.flush();
                return customer.getCustomerId();
            }
            catch(PersistenceException ex2) {
                return (long)-1;
            }
        }
    }

    @Override
    public Customer login(String email, String password) throws InvalidLoginException {
        Query query = em.createQuery("SELECT m FROM Customer m WHERE m.customerEmail = :inEmail");
        query.setParameter("inEmail", email);
        Customer m;

        try {
            m = (Customer) query.getSingleResult();
            if (m.getCustomerPassword().equals(password)) {
                return m;
            } else {
                throw new InvalidLoginException("You Enter The Wrong Password. Please Try Again.");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginException("Member Not Found!");
        }

    }

}
