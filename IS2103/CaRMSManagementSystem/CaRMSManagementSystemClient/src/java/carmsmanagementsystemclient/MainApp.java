/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementsystemclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.CarModel;
import entity.Employee;
import java.util.Scanner;
import javax.persistence.PersistenceException;
import util.exception.CarCategoryNotFoundException;
import util.exception.InvalidLoginException;


/**
 *
 * @author 60540
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private Employee currEmployee;

    public MainApp() {

    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote,CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote ) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carCategorySessionBeanRemote =carCategorySessionBeanRemote;
    }

    public void runApp() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("***Welcome! CaRMS Management Client! ***");
            System.out.println("Input 1 to Login");
            System.out.println("Input 2 to Manually Allocate Cars");
            System.out.println("Input 3 to Exit");

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
                    }
                } else if (input == 2) {
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

    private void operationsMenu() {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("*** Welcome to the Operations Menu ***");
            System.out.println("Hello! " + currEmployee.getEmployeeFirstName() + " " + currEmployee.getEmployeeLastName());
            System.out.println("Your role is: " + currEmployee.getEmployeeRole().toString());
            System.out.println("Input 1 to the Sales Management Menu");
            System.out.println("Input 2 to the Customer Service Menu");
            System.out.println("Input 3 to log out");

            input = 0;

            while (input < 1 || input > 3) {
                System.out.print("Your input: ");
                input = sc.nextInt();

                //try {
                if (input == 1) {
                    salesManagementMenu();
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

    private void salesManagementMenu() {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("*** Welcome to the Sales Management Menu ***");
            System.out.println("[1] Create New Model");
            System.out.println("[2] View All Models");
            System.out.println("[3] Update Model");
            System.out.println("[4] Delete Model");
            System.out.println("[5] Exit");

            input = 0;

            while (input < 1 || input > 5) {
                System.out.print("Your input: ");
                input = sc.nextInt();

                if (input == 1) {
                    createNewModel();
                    break;
                } else if (input == 2) {
                    //viewAllModels();
                    break;
                } else if (input == 3) {
                    //updateModel();
                    break;
                } else if (input == 4) {
                    //deleteModel();
                } else if (input == 5) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }
            }

            if (input == 5) {
                break;
            }
        }

    }

    private void createNewModel() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Create New Model ***");

        CarModel m = new CarModel();
        System.out.print("Enter the name of the model: ");
        String model = sc.nextLine().trim();
        m.setModel(model);
        System.out.print("Enter the make of the model: ");
        String make = sc.nextLine().trim();
        m.setMake(make);
        
        System.out.print("Enter the belonged car category Id: ");
        long categoryId = sc.nextLong();
        long modelId = 0;
        try {
            modelId = carModelSessionBeanRemote.createNewCarModel(categoryId,m);
            
            if (modelId == -1) {
                System.out.print("An Persistence Error Occured While Creating a New Car Model.");
                return;
            }
              
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category Not Found! Please Enter an Valid Car Category Id.");
            return;
        } catch (PersistenceException ex) {
            System.out.print("An Error Occured While Creating a New Car Model: ");
            System.out.println(ex.getMessage());
            return;
        }
        
        System.out.println("*** The new car model is successfully created ***");
        System.out.println("The new car model id is:[ " + modelId + " ]");
    }

}
