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
import util.enumeration.RentalRateType;
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

            Outlet o1 = new Outlet("Outlet A", "Bedok", null, null);
            outletSessionBean.createNewOutlet(o1);
            Outlet o2 = new Outlet("Outlet B", "Changi", null, null);
            outletSessionBean.createNewOutlet(o2);
            Outlet o3 = new Outlet("Outlet C", "Simei", new Date(2000, 1, 1, 10, 0, 0), new Date(2000, 1, 1, 22, 0, 0));
            outletSessionBean.createNewOutlet(o3);
            
            
            Employee a1 = new Employee("Employee", "A1", "salesA1", "123", EmployeeRoleEnum.SALES_MANAGER);//Always use object id. In the employee session bean, we inser tthe outletsession bean
            employeeSessionBean.createNewEmployee(a1, o1.getOutletId());
            
            Employee a2 = new Employee("Employee", "A2", "opsA2", "123", EmployeeRoleEnum.OPERATIONS_MANAGER);
            employeeSessionBean.createNewEmployee(a2, o1.getOutletId());
            
            Employee a3 = new Employee("Employee", "A3", "custA3", "123", EmployeeRoleEnum.CUSTOMER_SERVICE_EXCUTIVE);
            employeeSessionBean.createNewEmployee(a3, o1.getOutletId());
            Employee a4 = new Employee("Employee", "A4", "employeeA4", "123", EmployeeRoleEnum.EMPLOYEE);
            employeeSessionBean.createNewEmployee(a4, o1.getOutletId());
            Employee a5 = new Employee("Employee", "A5", "employeeA5", "123", EmployeeRoleEnum.EMPLOYEE);
            employeeSessionBean.createNewEmployee(a5, o1.getOutletId());
            
            Employee b1 = new Employee("Employee", "B1", "salesB1", "123", EmployeeRoleEnum.SALES_MANAGER);//Always use object id. In the employee session bean, we inser tthe outletsession bean
            employeeSessionBean.createNewEmployee(b1, o2.getOutletId());
            
            Employee b2 = new Employee("Employee", "B2", "opsB2", "123", EmployeeRoleEnum.OPERATIONS_MANAGER);
            employeeSessionBean.createNewEmployee(b2, o2.getOutletId());
            
            Employee b3 = new Employee("Employee", "B3", "custB3", "123", EmployeeRoleEnum.CUSTOMER_SERVICE_EXCUTIVE);
            employeeSessionBean.createNewEmployee(b3, o2.getOutletId());
           
            
            Employee c1 = new Employee("Employee", "C1", "salesC1", "123", EmployeeRoleEnum.SALES_MANAGER);//Always use object id. In the employee session bean, we inser tthe outletsession bean
            employeeSessionBean.createNewEmployee(c1, o3.getOutletId());
            
            Employee c2 = new Employee("Employee", "C2", "opsC2", "123", EmployeeRoleEnum.OPERATIONS_MANAGER);
            employeeSessionBean.createNewEmployee(c2, o3.getOutletId());
            
            Employee c3 = new Employee("Employee", "C3", "custC3", "123", EmployeeRoleEnum.CUSTOMER_SERVICE_EXCUTIVE);
            employeeSessionBean.createNewEmployee(c3, o3.getOutletId());
            
         

            
            CarCategory standardSedan = new CarCategory("Standard Sedan");
            carCategorySessionBean.createNewCarCategory(standardSedan);
            CarCategory familySedan = new CarCategory("Family Sedan");
            carCategorySessionBean.createNewCarCategory(familySedan);
            CarCategory luxurySedan = new CarCategory("Luxury Sedan");
            carCategorySessionBean.createNewCarCategory(luxurySedan);
            CarCategory suvAndMini = new CarCategory("SUV and Minivan");
            carCategorySessionBean.createNewCarCategory(suvAndMini);

            CarModel toyota = new CarModel("Toyota", "Corolla",standardSedan);
            carModelSessionBean.createNewCarModel(standardSedan.getCategoryId(),toyota);
            CarModel honda = new CarModel("Honda","Civic",standardSedan);
            carModelSessionBean.createNewCarModel(standardSedan.getCategoryId(),honda);
            CarModel nissan = new CarModel("Nissan","Sunny",standardSedan);
            carModelSessionBean.createNewCarModel(standardSedan.getCategoryId(),nissan);
            CarModel mercedes = new CarModel("Mercedes","E Class",luxurySedan);
            carModelSessionBean.createNewCarModel(luxurySedan.getCategoryId(),mercedes);
            
            Car carA1 = new Car("SS00A1TC","DEFAULT");
            carSessionBean.createNewCar(toyota.getModelId(), o1.getOutletId(), carA1);
            Car carA2 = new Car("SS00A2TC","DEFAULT");
            carSessionBean.createNewCar(toyota.getModelId(), o1.getOutletId(), carA2);
            Car carA3 = new Car("SS00A3TC","DEFAULT");
            carSessionBean.createNewCar(toyota.getModelId(), o1.getOutletId(), carA3);
            Car carA4 = new Car("LS00A4ME","DEFAULT");
            carSessionBean.createNewCar(mercedes.getModelId(), o1.getOutletId(), carA4);
            
            Car carB1 = new Car("SS00B1HC","DEFAULT");
            carSessionBean.createNewCar(honda.getModelId(), o2.getOutletId(), carB1);
            Car carB2 = new Car("SS00B2HC","DEFAULT");
            carSessionBean.createNewCar(honda.getModelId(), o2.getOutletId(), carB2);
            Car carB3 = new Car("SS00B3HC","DEFAULT");
            carSessionBean.createNewCar(honda.getModelId(), o2.getOutletId(), carB3);
            
            Car carC1 = new Car("SS00C1NS","DEFAULT");
            carSessionBean.createNewCar(nissan.getModelId(), o3.getOutletId(), carC1);
            Car carC2 = new Car("SS00C2NS","DEFAULT");
            carSessionBean.createNewCar(nissan.getModelId(), o3.getOutletId(), carC2);
            Car carC3 = new Car("SS00C3NS","DEFAULT");
            carSessionBean.createNewCar(nissan.getModelId(), o3.getOutletId(), carC3);
            
            RentalRate standardSedanDefault = new RentalRate("Standard Sedan - Default", BigDecimal.valueOf(100.0), null, null, standardSedan, RentalRateType.DEFAULT);
            rentalRateSessionBean.createNewRentalRate(standardSedan.getCategoryId(), standardSedanDefault);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date startDateTime = dateFormat.parse("09/12/2022 12:00");
            Date endDateTime = dateFormat.parse("11/12/2022 00:00");
            RentalRate standardSedanWeekendPromo = new RentalRate("Standard Sedan - Weekend Promo", BigDecimal.valueOf(80.0), startDateTime, endDateTime, standardSedan, RentalRateType.PROMOTION);
            rentalRateSessionBean.createNewRentalRate(standardSedan.getCategoryId(), standardSedanWeekendPromo);
            
            RentalRate familySedanDefault = new RentalRate("Family Sedan - Default", BigDecimal.valueOf(200.0), null, null, familySedan, RentalRateType.DEFAULT);
            rentalRateSessionBean.createNewRentalRate(familySedan.getCategoryId(), familySedanDefault);
            
            RentalRate luxurySedanDefault = new RentalRate("Luxury Sedan - Default", BigDecimal.valueOf(300.0), null, null, luxurySedan, RentalRateType.DEFAULT);
            rentalRateSessionBean.createNewRentalRate(luxurySedan.getCategoryId(), luxurySedanDefault);

            startDateTime = dateFormat.parse("05/12/2022 00:00");
            endDateTime = dateFormat.parse("05/12/2022 23:59");
            RentalRate luxurySedanMonday = new RentalRate("Luxury Sedan - Monday", BigDecimal.valueOf(310.0), startDateTime, endDateTime, luxurySedan, RentalRateType.PEAK);
            rentalRateSessionBean.createNewRentalRate(luxurySedan.getCategoryId(), luxurySedanMonday);

            startDateTime = dateFormat.parse("06/12/2022 00:00");
            endDateTime = dateFormat.parse("06/12/2022 23:59");
            RentalRate luxurySedanTuesday = new RentalRate("Luxury Sedan - Tuesday", BigDecimal.valueOf(320.0), startDateTime, endDateTime, luxurySedan, RentalRateType.PEAK);
            rentalRateSessionBean.createNewRentalRate(luxurySedan.getCategoryId(), luxurySedanTuesday);
              
            startDateTime = dateFormat.parse("07/12/2022 00:00");
            endDateTime = dateFormat.parse("07/12/2022 23:59");
            RentalRate luxurySedanWednesday = new RentalRate("Luxury Sedan - Wednesday", BigDecimal.valueOf(330.0), startDateTime, endDateTime, luxurySedan, RentalRateType.PEAK);
            rentalRateSessionBean.createNewRentalRate(luxurySedan.getCategoryId(), luxurySedanWednesday);
            
            startDateTime = dateFormat.parse("07/12/2022 12:00");
            endDateTime = dateFormat.parse("08/12/2022 12:00");
            RentalRate luxurySedanWeekdayPromo = new RentalRate("Luxury Sedan - Weekday Promo", BigDecimal.valueOf(250.0), startDateTime, endDateTime, luxurySedan, RentalRateType.PROMOTION);
            rentalRateSessionBean.createNewRentalRate(luxurySedan.getCategoryId(), luxurySedanWeekdayPromo);
            
            startDateTime = null;//dateFormat.parse(null);
            endDateTime = null;//dateFormat.parse(null);
            RentalRate suvAndMiniDefault = new RentalRate("SUV and Minivan - Default", BigDecimal.valueOf(400.0), startDateTime, endDateTime, suvAndMini, RentalRateType.DEFAULT);
            rentalRateSessionBean.createNewRentalRate(luxurySedan.getCategoryId(), luxurySedanWeekdayPromo);
            
            //Partner Backend data initialisation
            Partner holidayReservation = new Partner("Holiday.com", "123");
            partnerSessionBean.createNewPartner(holidayReservation);

            
        } catch (ParseException | CarCategoryNotFoundException | UnknownPersistenceException | 
                InputDataValidationException | CarModelNotFoundException
                | LicensePlateExistException | ModelDisabledException | OutletNotFoundException ex) {
            ex.printStackTrace();

        }  

    }

}
