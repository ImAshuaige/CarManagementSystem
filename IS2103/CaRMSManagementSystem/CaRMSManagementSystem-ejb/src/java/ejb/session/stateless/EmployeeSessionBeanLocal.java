/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 60540
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Long createNewEmployee(Employee employee);

    public List<Employee> retrieveAllEmployees();
    
}
