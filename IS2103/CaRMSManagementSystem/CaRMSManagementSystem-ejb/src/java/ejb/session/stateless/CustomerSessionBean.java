/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Partner;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }
    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewPartnerCustomer(Long partnerId, Customer customer) throws PartnerNotFoundException, UnknownPersistenceException, InputDataValidationException {
        
        try {
            Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);

            if (constraintViolations.isEmpty()) {
                //Importing the partner session bean
                Partner partner = partnerSessionBeanLocal.retrievePartnerByPartnerId(partnerId);
                partner.addCustomer(customer);
                customer.setPartner(partner);
                em.persist(customer);
                em.flush();
                return customer.getCustomerId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Long createNewCustomer(Customer newCustomer) throws CustomerEmailExistsException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(newCustomer);

            if (constraintViolations.isEmpty()) {
                em.persist(newCustomer);
                em.flush();
                return newCustomer.getCustomerId();
            } else {
                throw new InputDataValidationException();
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CustomerEmailExistsException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
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
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException {
        Customer customer = em.find(Customer.class,
                 customerId);

        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

}
