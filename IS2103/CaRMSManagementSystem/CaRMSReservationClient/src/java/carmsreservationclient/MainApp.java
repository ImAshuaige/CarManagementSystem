/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import util.exception.CustomerEmailExistsException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;

/**
 *
 * @author 60540
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CarModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private ReservationSessionBeanRemote rentalReservationSessionBeanRemote;

    private Customer currCustomer;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote,
            ReservationSessionBeanRemote rentalReservationSessionBeanRemote,
            CarModelSessionBeanRemote modelSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote) {
        this();

        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.rentalReservationSessionBeanRemote = rentalReservationSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
    }

    public void runApp() throws InvalidLoginException, InputDataValidationException {
        Scanner sc = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("***Welcome! CaRMS Reservation Client! ***");
            System.out.println("[1] Login");
            System.out.println("[2] Register As Customer");
            System.out.println("[3] Search Car");
            System.out.println("[4] Exit");

            input = 0;
            while (input == 0) {
                System.out.print("Enter your input: ");
                input = sc.nextInt();

                if (input == 1) {
                    try {
                        login();
                        System.out.println("***Login Successfully***");
                        loggedInMenu();
                    } catch (InvalidLoginException ex) {
                        System.out.println("Invalid Login: " + ex.getMessage());
                    }
                    /*catch (Exception ex) {
                        System.out.println("The User Does Not Exist. Please Enter the Correct Username!");
                    }*/
                } else if (input == 2) {
                    registerAsCustomer();
                    break;
                } else if (input == 3) {
                    searchCar();
                    break;
                } else if (input == 4) {
                    break;
                } else {
                    System.out.println("Invalid input, please try again!");
                }

            }
            if (input == 4) {
                break;
            }
        }

    }

    private void login() throws InvalidLoginException {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";
        System.out.println("*** Enter Login Details ***");
        System.out.print("Enter Your Email: ");
        email = scanner.nextLine().trim();
        System.out.print("Enter Your Password: ");
        password = scanner.nextLine().trim();

        if (email.length() > 0 && password.length() > 0) {
            currCustomer = customerSessionBeanRemote.login(email, password);
        } else {
            throw new InvalidLoginException("Missing Login Credential!");
        }
    }

    private void loggedInMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println();
            System.out.println("*** Welcome! CaRMS Reservation Client ***\n");
            System.out.println("You are logged in as " + currCustomer.getCustomerFirstName() + " " + currCustomer.getCustomerLastName() + "\n");
            System.out.println("[1] Search Car");//Reserve Car is included here
            System.out.println("[2] View Reservation Details");
            System.out.println("[3] View All My Reservations");
            System.out.println("[4] Logout\n");
            input = 0;

            while (input < 1 || input > 4) {
                System.out.print("Your input: ");

                input = scanner.nextInt();
                scanner.nextLine();

                if (input == 1) {
                    searchCar();
                } else if (input == 2) {
                    viewReservationDetails();
                } else if (input == 3) {
                    viewAllMyReservations();
                } else if (input == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }

            }
            if (input == 4) {
                System.out.println("***Logout Successfully***");
                break;
            }
        }
    }

    private void searchCar() {

    }

    private void registerAsCustomer() {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";
        String firstName = "";
        String lastName = "";

        System.out.println("*** Register As Customer ***\n");
        System.out.print("Enter Your Email: ");
        email = scanner.nextLine().trim();
        System.out.print("Enter Your Password: ");
        password = scanner.nextLine().trim();
        System.out.print("Enter Your First Name: ");
        firstName = scanner.nextLine().trim();
        System.out.print("Enter Your Last Name: ");
        lastName = scanner.nextLine().trim();

        /*if (firstName.length() > 0 && lastName.length() > 0 && email.length() > 0 && password.length() > 0) {*/
        Customer newCust = new Customer(firstName, lastName, email, password);
        try {
            Long newCustId = customerSessionBeanRemote.createNewCustomer(newCust);
            if (newCustId == -1) {
                System.out.println("An Error Occurred While Registering.");
                return;
            }
            System.out.println("You have successfully registered! Your Customer ID is " + newCustId + "\n");
        } catch (CustomerEmailExistsException ex) {
            System.out.println("An error occured while creating member: " + ex.getMessage() + "\n");
        }
        /*} else {
            throw new IncompleteRegistrationDetailsException("An error occured while creating member: Incomplete registration details!\n");
        }*/

    }

    private void viewReservationDetails() {

    }

    private void viewAllMyReservations() {

    }

}
