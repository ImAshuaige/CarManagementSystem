/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Partner;
import entity.Employee;
import entity.Outlet;
import entity.RentalRate;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.LicensePlateExistException;
import util.exception.ModelDisabledException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Mehak
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBean;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBean;

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
        try {
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

            CarCategory hondaCivic = new CarCategory("Honda Civic");
            carCategorySessionBean.createNewCarCategory(hondaCivic);
            CarCategory bmw = new CarCategory("BMW");
            carCategorySessionBean.createNewCarCategory(bmw);
            CarCategory mercedes = new CarCategory("Mercedes Benz");
            carCategorySessionBean.createNewCarCategory(mercedes);
            CarCategory mitsubishi = new CarCategory("Mitsubishi");
            carCategorySessionBean.createNewCarCategory(mitsubishi);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date startDateTime = dateFormat.parse("06/12/2019 12:00");
            Date endDateTime = dateFormat.parse("08/12/2019 00:00");
            RentalRate hondaCivicDefault = new RentalRate("Honda Civic: Default", BigDecimal.valueOf(70.0), startDateTime, endDateTime, hondaCivic);
            rentalRateSessionBean.createNewRentalRate(hondaCivic.getCategoryId(), hondaCivicDefault);

            startDateTime = dateFormat.parse("02/12/2019 00:00");
            endDateTime = dateFormat.parse("02/12/2019 23:59");
            RentalRate bmwDefault = new RentalRate("BMW: Default", BigDecimal.valueOf(200.0), startDateTime, endDateTime, hondaCivic);
            rentalRateSessionBean.createNewRentalRate(bmw.getCategoryId(), bmwDefault);

            startDateTime = dateFormat.parse("03/12/2019 00:00");
            endDateTime = dateFormat.parse("03/12/2019 23:59");
            RentalRate mercedesDefault = new RentalRate("Mercedes: Default", BigDecimal.valueOf(100.0), startDateTime, endDateTime, hondaCivic);
            rentalRateSessionBean.createNewRentalRate(mercedes.getCategoryId(), mercedesDefault);

            startDateTime = dateFormat.parse("04/12/2019 00:00");
            endDateTime = dateFormat.parse("04/12/2019 23:59");
            RentalRate mitsubishiDefault = new RentalRate("Mitsubishi: Default", BigDecimal.valueOf(80.0), startDateTime, endDateTime, hondaCivic);
            rentalRateSessionBean.createNewRentalRate(mitsubishi.getCategoryId(), mitsubishiDefault);
            //standardSedan.getRentalRates().add(rentalRateEntity); Add this later when we create a car to establish the relationship

            
            
            //Initialise Car and Model to make testing easier, not part of requriement!

            CarModel modelA = new CarModel("makeA", "modelA",bmw);
            CarModel modelB = new CarModel("makeB","modelB",mercedes);
            
            carModelSessionBean.createNewCarModel(bmw.getCategoryId(),modelA);
            carModelSessionBean.createNewCarModel(mercedes.getCategoryId(),modelB);
            
            //public long createNewCar(long modelId, long outletId, Car newCar)
            //public Car(String carLicensePlateNum, String colour) 
            
            Car car1 = new Car("888A","DEFAULT");
            Car car2 = new Car("999A","DEFAULT");
   
            carSessionBean.createNewCar(modelA.getModelId(), o1.getOutletId(), car1);
            carSessionBean.createNewCar(modelB.getModelId(), o2.getOutletId(), car2);
            
        } catch (ParseException | CarCategoryNotFoundException | UnknownPersistenceException | 
                InputDataValidationException | CarModelNotFoundException
                | LicensePlateExistException | ModelDisabledException | OutletNotFoundException ex) {
            ex.printStackTrace();

        }  

    }

}
