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
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Outlet;
import entity.Reservation;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerEmailExistsException;
import util.exception.DatePeriodInvalidException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.NoAvailableRentalRateException;
import util.exception.OutletNotFoundException;
import util.exception.OutsideOutletOperatingHourException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author 60540
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CarModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;

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
        this.reservationSessionBeanRemote = rentalReservationSessionBeanRemote;
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
        Scanner scanner = new Scanner(System.in);
        Integer input = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Long carCategoryId = new Long(-1);
        Long modelId = new Long(-1);

        Date pickUpDateTime;
        Date returnDateTime;
        Long pickUpOutletId;
        Long returnOutletId;

        System.out.println("*** Search Car ***\n");
        Boolean canReserve = false;

        try {
            System.out.print("Enter Pick Up Date & Time (DD/MM/YYYY HH:MM): ");
            pickUpDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Date & Time (DD/MM/YYYY HH:MM): ");
            returnDateTime = inputDateFormat.parse(scanner.nextLine().trim());

            if (returnDateTime.before(pickUpDateTime)) {
                throw new DatePeriodInvalidException("Please Enter an Valid Date.");
            }

            List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
            System.out.printf("%5s%64s%20s%20s\n", "ID", "Outlet Name", "Opening Hour", "Closing Hour");
            SimpleDateFormat operatingHours = new SimpleDateFormat("HH:mm");
            for (Outlet outlet : outlets) {
                String openingHour = "24/7";
                String closingHour = "";
                if (outlet.getOutletOpeningTime() != null) {
                    openingHour = operatingHours.format(outlet.getOutletOpeningTime());
                }
                if (outlet.getOutletClosingTime() != null) {
                    closingHour = operatingHours.format(outlet.getOutletClosingTime());
                }
                System.out.printf("%5s%64s%20s%20s\n", outlet.getOutletId(), outlet.getOutletName(),
                        openingHour, closingHour);
            }

            System.out.print("Enter Pick Up Outlet ID: ");
            pickUpOutletId = scanner.nextLong();
            System.out.print("Enter Return Outlet ID: ");
            returnOutletId = scanner.nextLong();

            Outlet pickupOutlet = outletSessionBeanRemote.retrieveOutletById(pickUpOutletId);
            if (pickupOutlet.getOutletOpeningTime() != null) {
                if ((pickUpDateTime.getHours() < pickupOutlet.getOutletOpeningTime().getHours())
                        || (pickUpDateTime.getHours() == pickupOutlet.getOutletOpeningTime().getHours()
                        && pickUpDateTime.getMinutes() < pickupOutlet.getOutletOpeningTime().getMinutes())) {
                    throw new OutsideOutletOperatingHourException("The Pickup Time You Chose is Outside of the Outlet's Operating Hours.");
                }
                /*else if ((pickUpDateTime.getHours() > pickupOutlet.getOutletClosingTime().getHours())
                        || (pickUpDateTime.getHours() == pickupOutlet.getOutletClosingTime().getHours()
                        && pickUpDateTime.getMinutes() > pickupOutlet.getOutletClosingTime().getMinutes())) {
                    throw new OutsideOutletOperatingHourException("The Pickup Time You Chose is Outside of the Outlet's Operating Hours.");
                }*/
            }
            Outlet returnOutlet = outletSessionBeanRemote.retrieveOutletById(returnOutletId);
            if (returnOutlet.getOutletClosingTime() != null) {
                if ((returnDateTime.getHours() > returnOutlet.getOutletClosingTime().getHours())
                        || (returnDateTime.getHours() == returnOutlet.getOutletClosingTime().getHours()
                        && returnDateTime.getMinutes() > returnOutlet.getOutletClosingTime().getMinutes())) {
                    throw new OutsideOutletOperatingHourException("The Return Time You Chose is Outside of the Outlet's Operating Hours.");
                }
                /*else if ((returnDateTime.getHours() < returnOutlet.getOutletClosingTime().getHours())
                        || (returnDateTime.getHours() == returnOutlet.getOutletClosingTime().getHours()
                        && returnDateTime.getMinutes() < returnOutlet.getOutletClosingTime().getMinutes())) {
                    throw new OutsideOutletOperatingHourException("The Return Time You Chose is Outside of the Outlet's Operating Hours.");
                }*/
            }

            while (true) {
                System.out.println("*** Search by Car Category / Car Model ***\n");
                System.out.println("[1] Search by Car Category");
                System.out.println("[2] Search by Car Model");
                input = 0;

                while (input < 1 || input > 2) {
                    System.out.print("Your Choice: ");
                    input = scanner.nextInt();

                    if (input == 1) {
                        List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategory();
                        System.out.printf("%5s%60s\n", "ID", "Car Category");

                        for (CarCategory carCategory : carCategories) {
                            System.out.printf("%5s%60s\n",
                                    carCategory.getCategoryId(), carCategory.getCarCategoryName());
                        }
                        System.out.print("Enter Car Category ID: ");
                        carCategoryId = scanner.nextLong();
                        canReserve = reservationSessionBeanRemote.searchCarByCategory(pickUpDateTime, returnDateTime, pickUpOutletId, returnOutletId, carCategoryId);
                        break;
                    } else if (input == 2) {
                        List<CarModel> models = modelSessionBeanRemote.retrieveAllCarModels();
                        System.out.printf("%5s%30s%30s%30s\n", "ID", "Car Category", "Make", "Model");
                        for (CarModel model : models) {
                            System.out.printf("%5s%30s%30s%30s\n",
                                    model.getModelId(), model.getBelongsCategory().getCarCategoryName(),
                                    model.getMake(), model.getModel());
                        }
                        System.out.print("Enter Car Model ID: ");
                        modelId = scanner.nextLong();
                        carCategoryId = modelSessionBeanRemote.retrieveCarModelById(modelId).getBelongsCategory().getCategoryId();
                        canReserve = reservationSessionBeanRemote.searchCarByModel(pickUpDateTime, returnDateTime, pickUpOutletId, returnOutletId, modelId);
                        break;
                    } else {
                        System.out.println("Invalid option, please try again\n");
                    }
                }
                if (input == 1 || input == 2) {
                    break;
                }
            }
            scanner.nextLine();
            if (!canReserve) {
                System.out.println("There is No Available Car Under the Provided Criteria");
            } else {
                BigDecimal totalRentalFee = carCategorySessionBeanRemote.calculateRentalFee(carCategoryId, pickUpDateTime, returnDateTime);
                System.out.println("There are cars available! Total rental fee is SGD " + totalRentalFee + ". ");
                if (currCustomer != null) {
                    System.out.print("Reserve a car? (Enter 'YES' to reserve a car): ");
                    String response = scanner.nextLine().trim();
                    if (response.equals("YES")) {
                        reserveCar(input, carCategoryId, modelId, pickUpDateTime, returnDateTime, pickUpOutletId, returnOutletId, totalRentalFee);
                    }
                } else {
                    System.out.println("Please Login or Register to Reserve the Car!");
                }
            }
        } catch (ParseException ex) {
            System.out.println("Invalid Input.");
        } catch (NoAvailableRentalRateException ex) {
            System.out.println("There are no available rental rates for the period!\n");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId + "\n");
        } catch (CarModelNotFoundException ex) {
            System.out.println("Car Model Not Found\n");
        } catch (OutletNotFoundException ex) {
            System.out.println("Outlet Not Found \n");
        } catch (OutsideOutletOperatingHourException ex) {
            System.out.println(ex.getMessage());
        } catch (DatePeriodInvalidException ex) {
            System.out.println("Return Date Before Pickup Date. Please Enter Valid Dates!");
        }
        System.out.print("Press any key to continue.\n ");
        scanner.nextLine();
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

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** View Reservation Details ***\n");
        System.out.print("Enter Reservation ID: ");
        Long reservationId = scanner.nextLong();
        scanner.nextLine();

        try {
           Reservation reservation = reservationSessionBeanRemote.retrieveReservationByReservationId(reservationId);
            System.out.printf("%4s%20s%20s%20s%12s%12s%25s%25s\n",
                    "ID", "Start Date",
                    "End Date", "Rental Fee",
                    "Paid", "Cancelled",
                    "Car Category", "Model");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String modelName = "null";
            if (reservation.getReservedCarModel() != null) {
                modelName = reservation.getReservedCarModel().getMake() + " " + reservation.getReservedCarModel().getModel();
            }
            System.out.printf("%4s%20s%20s%20s%12s%12s%25s%25s\n",
                    reservation.getRentalReservationId(), sdf.format(reservation.getReservationStartDate()),
                    sdf.format(reservation.getReservationEndDate()), reservation.getReservationPrice().toString(),
                    reservation.getPaid().toString(), reservation.getIsCancelled().toString(),
                    reservation.getReservedCarCategory().getCarCategoryName(), modelName);
            System.out.print("Do You Want to Cancel the Reservation? (Enter 'YES' to cancel): ");
            String input = scanner.nextLine().trim();
            if (input.equals("YES")) {
                cancelReservation(reservationId);
            } else {
                System.out.print("Press any key to continue. ");
            }
        } catch (ReservationNotFoundException ex) {
            System.out.println("Rental Reservation Not Found for ID " + reservationId);
        }
        
        System.out.print("Press any key to continue.\n ");
        scanner.nextLine();


    }

    private void viewAllMyReservations() {

    }

    private void reserveCar(Integer input, Long carCategoryId, Long modelId, Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, BigDecimal totalRentalFee) {

    }
    
    
    private void cancelReservation(Long reservationId) {
        
    }

}
