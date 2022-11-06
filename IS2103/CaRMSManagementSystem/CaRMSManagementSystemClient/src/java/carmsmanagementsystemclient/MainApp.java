/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementsystemclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchSessionBeanRemote;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.RentalRate;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelDeletionException;
import util.exception.CarModelNotFoundException;
import util.exception.EndDateBeforeStartDateException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginException;
import util.exception.RentalDateDeletionException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
public class MainApp {

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote;
    private EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;
    private Employee currEmployee;
    
    private SalesManagementModule salesManagementModule;

    public MainApp() {

    }
/* MainApp mainApp = new MainApp(employeeSessionBeanRemote,outletSessionBeanRemote,carCategorySessionBeanRemote,carModelSessionBeanRemote,rentalRateSessionBeanRemote, carSessionBean);
       */

    public MainApp(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, TransitDriverDispatchSessionBeanRemote transitDriverDispatchSessionBeanRemote, EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchSessionBeanRemote = transitDriverDispatchSessionBeanRemote;
        this.ejbTimerSessionBeanRemote = ejbTimerSessionBeanRemote;
    }
    
    

    public void runApp() throws InvalidLoginException, InputDataValidationException, InvalidAccessRightException {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("***Welcome! CaRMS Management Client! ***");
            System.out.println("[1] Login");
            System.out.println("[2] Manually Allocate Cars");
            System.out.println("[3] Exit");

            input = 0;//set input/default input to 0
            while (input == 0) {//everytime want to do a new command
                System.out.print("Enter your input: ");
                input = sc.nextInt();

                if (input == 1) {
                    try {
                        login();
                        System.out.println("***Login Successfully***");
                        operationsMenu();
                    } catch (InvalidLoginException ex) {
                        System.out.println("Invalid Login: " + ex.getMessage());
                    } /*catch (Exception ex) {
                        System.out.println("The User Does Not Exist. Please Enter the Correct Username!");
                    }*/
                } else if (input == 2) {
                    //Need to add in NoAllocatableCarException and RentalReservationNotFoundException
                    manuallyAllocateCars();
                    break;//to be written
                } else if (input == 3) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }

            }
            if (input == 3) {
                break;//break the main true loop
            }
        }

    }

    private void login() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        String employeeUsername = "";
        String password = "";

        System.out.println("*** Enter Login Details ***");
        System.out.println("Enter Your Employee Username: ");
        employeeUsername = sc.nextLine().trim();
        System.out.println("Enter Your Password: ");
        password = sc.nextLine().trim();

        if (employeeUsername.length() > 0 && password.length() > 0) {
            currEmployee = employeeSessionBeanRemote.login(employeeUsername, password);
        } else {
            throw new InvalidLoginException("Wrong Password!");
        }

    }

    private void operationsMenu() {//throws InvalidAccessRightException {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("*** Welcome to the Operations Menu ***");
            System.out.println("Hello! " + currEmployee.getEmployeeFirstName() + " " + currEmployee.getEmployeeLastName());
            System.out.println("Your role is: " + currEmployee.getEmployeeRole().toString());
            System.out.println("[1] Sales Management Menu");
            System.out.println("[2] Customer Service Menu");
            System.out.println("[3] Logout");

            input = 0;

            while (input < 1 || input > 3) {
                System.out.print("Your input: ");
                input = sc.nextInt();

                //try {
                if (input == 1) {
                salesManagementModule = new SalesManagementModule(rentalRateSessionBeanRemote, employeeSessionBeanRemote, outletSessionBeanRemote, carModelSessionBeanRemote, carCategorySessionBeanRemote, carSessionBeanRemote, transitDriverDispatchSessionBeanRemote, currEmployee); 
                try {
                salesManagementModule.menuSalesManagement();
                } catch (InvalidAccessRightException ex) {
                      System.out.println(ex.getMessage()); 
                }
                    break;//to be written
                } else if (input == 2) {
                    break;//to be writtem
                } else if (input == 3) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }
                //to be uncomment
                //} catch (InvalidAccessRightException ex) {
                //    System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                //} catch (UnpaidRentalReservationException ex) {
                //    System.out.println("Customer has not paid for the car rental reservation!");
                //}
            }
            if (input == 3) {
                System.out.println("***Logout Successfully***");
                break;
            }
        }
    }
    
    //Need to continue fixing this method
    private void manuallyAllocateCars() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Allocating Cars to Reservation of a certain date ***");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.print("Enter Date(DD/MM/YYYY)> ");
        String inputDate = sc.nextLine().trim();
        try {
            Date date = sdf.parse(inputDate);
            //Import the ejbTimerSessionBeanRemote
            System.out.println(ejbTimerSessionBeanRemote);
            ejbTimerSessionBeanRemote.allocateCarsToCurrentDayReservations(date);
            System.out.println("*** Completed Allocation of Cars for reservations on " + inputDate + " ***\n");
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }

}
