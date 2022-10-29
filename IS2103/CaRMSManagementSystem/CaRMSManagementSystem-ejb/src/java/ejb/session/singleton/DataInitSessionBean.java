/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Partner;
import entity.Employee;
import entity.Outlet;
import entity.RentalRate;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeRoleEnum;

/**
 *
 * @author Mehak
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;
    //try
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(Outlet.class, 1l) == null && em.find(Employee.class, 1l) == null && em.find(RentalRate.class, 1l) == null) {
            initializeData();
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private void initializeData() {
        //Outlet Backend data initialisation
        Outlet o1 = new Outlet("Outlet A", "Ang Mo Kio", new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 0));
        outletSessionBean.createNewOutlet(o1);
        Outlet o2 = new Outlet("Outlet B", "Orchard", new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 0));
        outletSessionBean.createNewOutlet(o2);
        Outlet o3 = new Outlet("Outlet C", "Bugis", new Date(2000, 1, 1, 10, 0, 0), new Date(2000, 1, 1, 22, 0, 0));
        outletSessionBean.createNewOutlet(o3);

        //Employee Backend data initialisation
        Employee a1 = new Employee("Employee", "A1", "socsales", "123", EmployeeRoleEnum.SALES_MANAGER);//Always use object id. In the employee session bean, we inser tthe outletsession bean
        employeeSessionBean.createNewEmployee(a1, o1.getOutletId());
        //o1.getEmployeesList().add(a1);
        Employee a2 = new Employee("Employee", "A2", "socops", "123", EmployeeRoleEnum.OPERATIONS_MANAGER);
        employeeSessionBean.createNewEmployee(a2, o2.getOutletId());
        //o2.getEmployeesList().add(a2);
        Employee a3 = new Employee("Employee", "A3", "soccust", "123", EmployeeRoleEnum.CUSTOMER_SERVICE_EXCUTIVE);
        employeeSessionBean.createNewEmployee(a3, o3.getOutletId());
        //o3.getEmployeesList().add(a3);
        Employee a4 = new Employee("Employee", "A4", "socemployee4", "123", EmployeeRoleEnum.EMPLOYEE);
        employeeSessionBean.createNewEmployee(a4, o1.getOutletId());
        //o1.getEmployeesList().add(a4);
        Employee a5 = new Employee("Employee", "A4", "socemployee5", "123", EmployeeRoleEnum.ADMINISTRATOR);
        employeeSessionBean.createNewEmployee(a5, o2.getOutletId());

        //Partner Backend data initialisation
        Partner holidayReservation = new Partner("HolidayReservation", "123");
        partnerSessionBean.createNewPartner(holidayReservation);




       //o2.getEmployeesList().add(a5);
        /*this.isApplied = false; 
        this.rentalName = rentalName;
        this.dailyRate = dailyRate;
        this.rateStartDate = rateStartDate;
        this.rateEndDate = rateEndDate;
        this.carCategory = carCategory;*/
 /*
        RentalRate rentalRate = new RentalRateEntity("Weekend Promo", 80, new Date(119, 11, 6, 12, 0), new Date(119, 11, 8, 0, 0));
        rentalRateSessionBean.createRentalRateEntity(rentalRateEntity);
         */
        //standardSedan.getRentalRates().add(rentalRateEntity); Add this later when we create a car to establish the relationship
    }

}
