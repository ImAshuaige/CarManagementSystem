/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author 60540
 */
@Remote
public interface EmployeeSessionBeanRemote {
    
    public Long createNewEmployee(Employee employee, long outletId);
    
    public List<Employee> retrieveAllEmployees();

    public Employee login(String username, String password) throws InvalidLoginException;

    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;
    
}
