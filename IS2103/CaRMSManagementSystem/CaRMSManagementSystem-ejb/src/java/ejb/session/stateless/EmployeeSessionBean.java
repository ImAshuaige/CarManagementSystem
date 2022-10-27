/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginException;

/**
 *
 * @author 60540
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    //Added create new employee method
    @Override
    public Long createNewEmployee(Employee employee, long outletId) {
        em.persist(employee);
        Outlet o = em.find(Outlet.class, outletId);
        o.getEmployeesList().add(employee);
        employee.setOutlet(o);
        em.flush();
        return employee.getEmployeeId();
    }

    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");
        return query.getResultList();
    }
    //assume that the username is always valid
    @Override
    public Employee login(String username, String password) throws InvalidLoginException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        Employee currEmployee = (Employee) query.getSingleResult();

        if (currEmployee.getEmployeePassword().equals(password)) {
            return currEmployee;
        } else {
            throw new InvalidLoginException("Wrong password!");
        }

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
