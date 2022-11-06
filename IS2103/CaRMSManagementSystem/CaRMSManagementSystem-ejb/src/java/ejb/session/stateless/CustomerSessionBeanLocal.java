/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.CustomerEmailExistsException;
import util.exception.InvalidLoginException;

/**
 *
 * @author 60540
 */
@Local
public interface CustomerSessionBeanLocal {
    public Customer login(String email, String password) throws InvalidLoginException;

    public Long createNewCustomer(Customer customer) throws CustomerEmailExistsException;
}
