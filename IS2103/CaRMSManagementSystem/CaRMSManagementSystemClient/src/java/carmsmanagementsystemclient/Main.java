/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementsystemclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Employee;
import entity.Outlet;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Mehak
 */
public class Main {

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;

    @EJB
    private static OutletSessionBeanRemote outletSessionBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<Outlet> outlets = outletSessionBean.retrieveAllOutlets();
        for (Outlet outlet:outlets) {
            //trying to see merge conflict
            System.out.println("outletId = " + outlet.getOutletId() + "; outletName = " + outlet.getOutletName() 
                               + "outlet Opening Time = " + outlet.getOutletOpeningTime()
                               + "outlet Closing Time = " + outlet.getOutletClosingTime());
            
        }
        /*  this.transitDriverDispatchRecord = new ArrayList<> ();
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeeUserName = employeeUserName;
        this.employeePassword = employeePassword;
        this.employeeRole = employeeRole;
        this.outlet = outlet;*/
        
        List<Employee> employees = employeeSessionBean.retrieveAllEmployees();
        for (Employee employee:employees) {
            
            System.out.println("employeeId = " + employee.getEmployeeId() + "; employeeFirstName = " + employee.getEmployeeFirstName() 
                               + "employeeLastName = " + employee.getEmployeeLastName()
                               + "employeeUserName = " + employee.getEmployeeUserName()
                               + "employeePassword = " + employee.getEmployeePassword()
                               + "employeeRole = " + employee.getEmployeeRole()
                               + "employeeOutlet = " + employee.getOutlet());
            
        }
        
    }
    }
    

