/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
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
@Local
public interface CustomerSessionBeanLocal {
    public Customer login(String email, String password) throws InvalidLoginException;

    public Long createNewPartnerCustomer(Long partnerId, Customer customer) throws PartnerNotFoundException, UnknownPersistenceException, InputDataValidationException;
    
    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    public Long createNewCustomer(Customer newCustomer) throws CustomerEmailExistsException, UnknownPersistenceException, InputDataValidationException;
    
}
