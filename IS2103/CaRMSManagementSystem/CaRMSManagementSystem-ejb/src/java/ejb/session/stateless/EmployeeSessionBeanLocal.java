/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginException;

/**
 *
 * @author 60540
 */
@Local
public interface EmployeeSessionBeanLocal {


    public List<Employee> retrieveAllEmployees();

    public Long createNewEmployee(Employee employee, long outletId);
    
    public Employee login(String username, String password) throws InvalidLoginException;
}
