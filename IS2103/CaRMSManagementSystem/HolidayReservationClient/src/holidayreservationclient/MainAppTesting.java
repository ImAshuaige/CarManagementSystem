/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationclient;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import util.exception.DatePeriodInvalidException;
import util.exception.OutsideOutletOperatingHourException;
import ws.holidaySystemClient.CarCategory;
import ws.holidaySystemClient.CarCategoryNotFoundException_Exception;
import ws.holidaySystemClient.CarModel;
import ws.holidaySystemClient.CarModelNotFoundException_Exception;
import ws.holidaySystemClient.InvalidLoginException_Exception;
import ws.holidaySystemClient.NoAvailableRentalRateException_Exception;
import ws.holidaySystemClient.Outlet;
import ws.holidaySystemClient.OutletNotFoundException_Exception;
import ws.holidaySystemClient.PartnerNotFoundException_Exception;

/**
 *
 * @author 60540
 */
//public class MainAppTesting {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package holidayreservationclient;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import util.exception.DatePeriodInvalidException;
import util.exception.OutsideOutletOperatingHourException;
import ws.holidaySystemClient.CarCategory;
import ws.holidaySystemClient.CarCategoryNotFoundException;
import ws.holidaySystemClient.CarCategoryNotFoundException_Exception;
import ws.holidaySystemClient.CarModel;
import ws.holidaySystemClient.CarModelNotFoundException_Exception;
import ws.holidaySystemClient.InvalidLoginException_Exception;
import ws.holidaySystemClient.NoAvailableRentalRateException;
import ws.holidaySystemClient.NoAvailableRentalRateException_Exception;
import ws.holidaySystemClient.Outlet;
import ws.holidaySystemClient.OutletNotFoundException_Exception;
import ws.holidaySystemClient.PartnerNotFoundException_Exception;
import ws.holidaySystemClient.Reservation;

//import ws.holidaySystemClient.PartnerWebService;
/**
 *
 * @author 60540
 */
public class MainAppTesting {

    private Long currPartnerId = new Long(0);

    void runApp() {

        Scanner scanner = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("*** Welcome to Holiday Reservation System ! ***\n");
            System.out.println("[1] Login");
            System.out.println("[2] Search Car");
            System.out.println("[3] Exit\n");
            input = 0;

            while (input < 1 || input > 3) {
                System.out.print("Your Input: ");

                input = scanner.nextInt();

                if (input == 1) {
                    partnerLogin();
                    System.out.println("*** Login Successfully ***\n");
                    operationMenu();
                } else if (input == 2) {
                    searchCar();
                } else if (input == 3) {
                    break;
                } else {
                    System.out.print("Invalid Input! Please Try Again!\n");
                }
            }
            if (input == 3) {
                break;
            }
        }
    }

    private void partnerLogin() {
        Scanner scanner = new Scanner(System.in);
        String partnerName = "";
        String password = "";

        System.out.println("*** Partner Login ***\n");
        System.out.print("Enter partner name: ");
        partnerName = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        password = scanner.nextLine().trim();

        if (partnerName.length() > 0 && password.length() > 0) {
            try {
                currPartnerId = partnerLogin(partnerName, password);
            } catch (InvalidLoginException_Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            } catch (PartnerNotFoundException_Exception ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
    }

    private void operationMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer input = 0;

        while (true) {
            System.out.println("*** Holiday Reservation System ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: View Reservation Details");
            System.out.println("3: View All Partner Reservations");
            System.out.println("4: Logout\n");
            input = 0;

            while (input < 1 || input > 4) {
                System.out.print("Your Input: ");

                input = scanner.nextInt();
                if (input == 1) {
                    searchCar();
                } else if (input == 2) {
                    viewReservationDetails();
                } else if (input == 3) {
                    viewAllReservations();
                } else if (input == 4) {
                    break;
                } else {
                    System.out.println("Invalid Input! Please Try Again!\n");
                }
            }
            if (input == 4) {
                System.out.println("*** Logout Successfully ***\n");
                break;
            }
        }

    }


    private void viewReservationDetails() {

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

        GregorianCalendar gc = new GregorianCalendar();
        XMLGregorianCalendar pickUpGc = null;
        XMLGregorianCalendar returnGc = null;

        System.out.println("*** Search Car ***\n");
        Boolean canReserve = false;

        try {
            System.out.print("Enter Pick Up Date & Time (DD/MM/YYYY HH:MM): ");
            pickUpDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            gc.setTime(pickUpDateTime);
            pickUpGc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

            System.out.print("Enter Return Date & Time (DD/MM/YYYY HH:MM): ");
            returnDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            gc.setTime(pickUpDateTime);
            returnGc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

            if (returnDateTime.before(pickUpDateTime)) {
                throw new DatePeriodInvalidException("Please Enter an Valid Date.");
            }

            List<Outlet> outlets = retrieveAllOutlets();
            System.out.printf("%5s%64s%20s%20s\n", "ID", "Outlet Name", "Opening Hour", "Closing Hour");
            SimpleDateFormat operatingHours = new SimpleDateFormat("HH:mm");
            for (Outlet outlet : outlets) {
                String openingHour = "24/7";
                String closingHour = "";
                if (outlet.getOutletOpeningTime() != null) {
                    XMLGregorianCalendar openGregorianCalendar = outlet.getOutletOpeningTime();
                    Date openingTime = openGregorianCalendar.toGregorianCalendar().getTime();
                    openingHour = operatingHours.format(openingTime);
                }
                if (outlet.getOutletClosingTime() != null) {
                    XMLGregorianCalendar closeGregorianCalendar = outlet.getOutletClosingTime();
                    Date closingTime = closeGregorianCalendar.toGregorianCalendar().getTime();
                    closingHour = operatingHours.format(closingTime);
                }
                System.out.printf("%5s%64s%20s%20s\n", outlet.getOutletId(), outlet.getOutletName(),
                        openingHour, closingHour);
            }

            System.out.print("Enter Pick Up Outlet ID: ");
            pickUpOutletId = scanner.nextLong();
            System.out.print("Enter Return Outlet ID: ");
            returnOutletId = scanner.nextLong();

            Outlet pickupOutlet = retrieveOutletById(pickUpOutletId);
            if (pickupOutlet.getOutletOpeningTime() != null) {
                Date pickupOutletOpeningHour = pickupOutlet.getOutletOpeningTime().toGregorianCalendar().getTime();
                Date pickupOutletClosingHour = pickupOutlet.getOutletClosingTime().toGregorianCalendar().getTime();
                if ((pickUpDateTime.getHours() < pickupOutletOpeningHour.getHours())
                        || (pickUpDateTime.getHours() == pickupOutletOpeningHour.getHours()
                        && pickUpDateTime.getMinutes() < pickupOutletOpeningHour.getMinutes())) {
                    throw new OutsideOutletOperatingHourException("The Pickup Time You Chose is Outside of the Outlet's Operating Hours.");
                } else if ((pickUpDateTime.getHours() > pickupOutletClosingHour.getHours())
                        || (pickUpDateTime.getHours() == pickupOutletClosingHour.getHours()
                        && pickUpDateTime.getMinutes() > pickupOutletClosingHour.getMinutes())) {
                    throw new OutsideOutletOperatingHourException("The Pickup Time You Chose is Outside of the Outlet's Operating Hours.");
                }
            }
            Outlet returnOutlet = retrieveOutletById(returnOutletId);
            if (returnOutlet.getOutletClosingTime() != null) {
                Date returnOutletOpeningHour = returnOutlet.getOutletOpeningTime().toGregorianCalendar().getTime();
                Date returnOutletClosingHour = returnOutlet.getOutletClosingTime().toGregorianCalendar().getTime();
                if ((returnDateTime.getHours() < returnOutletOpeningHour.getHours())
                        || (returnDateTime.getHours() == returnOutletOpeningHour.getHours()
                        && returnDateTime.getMinutes() < returnOutletOpeningHour.getMinutes())) {
                    throw new OutsideOutletOperatingHourException("The Return Time You Chose is Outside of the Outlet's Operating Hours.");
                } else if ((returnDateTime.getHours() > returnOutletClosingHour.getHours())
                        || (returnDateTime.getHours() == returnOutletClosingHour.getHours()
                        && returnDateTime.getMinutes() > returnOutletClosingHour.getMinutes())) {
                    throw new OutsideOutletOperatingHourException("The Return Time You Chose is Outside of the Outlet's Operating Hours.");
                }
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
                        List<CarCategory> carCategories = retrieveAllCarCategory();
                        System.out.printf("%5s%60s\n", "ID", "Car Category");

                        for (CarCategory carCategory : carCategories) {
                            System.out.printf("%5s%60s\n",
                                    carCategory.getCategoryId(), carCategory.getCarCategoryName());
                        }
                        System.out.print("Enter Car Category ID: ");
                        carCategoryId = scanner.nextLong();
                        canReserve = searchCarByCategory(pickUpGc, returnGc, pickUpOutletId, returnOutletId, carCategoryId);
                        break;
                    } else if (input == 2) {
                        List<CarModel> models = retrieveAllCarModels();
                        System.out.printf("%5s%30s%30s%30s\n", "ID", "Car Category", "Make", "Model");
                        for (CarModel model : models) {
                            System.out.printf("%5s%30s%30s%30s\n",
                                    model.getModelId(), model.getBelongsCategory().getCarCategoryName(),
                                    model.getMake(), model.getModel());
                        }
                        System.out.print("Enter Car Model ID: ");
                        modelId = scanner.nextLong();
                        carCategoryId = retrieveCarModelById(modelId).getBelongsCategory().getCategoryId();
                        canReserve = searchCarByModel(pickUpGc, returnGc, pickUpOutletId, returnOutletId, modelId);
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
                BigDecimal totalRentalFee = calculateRentalFee(carCategoryId, pickUpGc, returnGc);
                System.out.println("There are cars available! Total rental fee is SGD " + totalRentalFee + ". ");
                if (currPartnerId != null) {
                    System.out.print("Reserve a car? (Enter 'YES' to reserve a car): ");
                    String response = scanner.nextLine().trim();
                    if (response.equals("YES")) {
                        //reserveCar(input, carCategoryId, modelId, pickUpDateTime, returnDateTime, pickUpOutletId, returnOutletId, totalRentalFee);
                    }
                } else {
                    System.out.println("Please Login or Register to Reserve the Car!");
                }
            }
        } catch (ParseException ex) {
            System.out.println("Invalid Input.");
        } catch (NoAvailableRentalRateException_Exception ex) {
            System.out.println("There are no available rental rates for the period!\n");
        } catch (CarCategoryNotFoundException_Exception ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId + "\n");
        } catch (CarModelNotFoundException_Exception ex) {
            System.out.println("Car Model Not Found\n");
        } catch (OutletNotFoundException_Exception ex) {
            System.out.println("Outlet Not Found \n");
        } catch (OutsideOutletOperatingHourException ex) {
            System.out.println(ex.getMessage());
        } catch (DatePeriodInvalidException ex) {
            System.out.println("Return Date Before Pickup Date. Please Enter Valid Dates!");
        } catch (DatatypeConfigurationException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.print("Press any key to continue.\n ");
        scanner.nextLine();
    }

    private static Long partnerLogin(java.lang.String partnerName, java.lang.String password) throws InvalidLoginException_Exception, PartnerNotFoundException_Exception {

        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.partnerLogin(partnerName, password);

        
    }
    //we need 
    private void viewAllReservations() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View All Reservations ***\n");
        List<Reservation> reservations = retrievePartnerReservations(currPartnerId);
        System.out.printf("%4s%20s%20s\n", "ID", "Start Date", "End Date");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Reservation reservation : reservations) {

            XMLGregorianCalendar startGregorianCalendar = reservation.getReservationStartDate();
            XMLGregorianCalendar endGregorianCalendar = reservation.getReservationEndDate();
            Date startDate = startGregorianCalendar.toGregorianCalendar().getTime();
            Date endDate = endGregorianCalendar.toGregorianCalendar().getTime();

            System.out.printf("%4s%20s%20s\n", reservation.getRentalReservationId(),
                    sdf.format(startDate),
                    sdf.format(endDate));
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    private static ws.holidaySystemClient.CarCategory retrieveCarCategoryByCarCategoryId(java.lang.Long carCategoryId) throws CarCategoryNotFoundException_Exception {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveCarCategoryByCarCategoryId(carCategoryId);
    }

    private static CarModel retrieveCarModelById(java.lang.Long carModelId) throws CarModelNotFoundException_Exception {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveCarModelById(carModelId);
    }

    private static Outlet retrieveOutletById(java.lang.Long outletId) throws OutletNotFoundException_Exception {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveOutletById(outletId);
    }

    private static java.util.List<ws.holidaySystemClient.CarCategory> retrieveAllCarCategory() {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAllCarCategory();
    }

    private static java.util.List<ws.holidaySystemClient.CarModel> retrieveAllCarModels() {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAllCarModels();
    }

    private static java.util.List<ws.holidaySystemClient.Outlet> retrieveAllOutlets() {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveAllOutlets();
    }

    private static BigDecimal calculateRentalFee(Long carCategoryId, XMLGregorianCalendar pickUpTime, XMLGregorianCalendar returnTime) throws CarCategoryNotFoundException_Exception, NoAvailableRentalRateException_Exception {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.calculateRentalFee(carCategoryId, pickUpTime, returnTime);
    }

    private static Boolean searchCarByCategory(XMLGregorianCalendar pickupTime, XMLGregorianCalendar returnTime, Long poutletId, Long routletId, Long categoryId) throws OutletNotFoundException_Exception, CarCategoryNotFoundException_Exception, NoAvailableRentalRateException_Exception {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.searchCarByCategory(pickupTime, returnTime, poutletId, routletId, categoryId);
    }

    private static Boolean searchCarByModel(XMLGregorianCalendar pickupTime, XMLGregorianCalendar returnTime, Long poutletId, Long routletId, Long modelId) throws NoAvailableRentalRateException_Exception, CarCategoryNotFoundException_Exception, CarModelNotFoundException_Exception, OutletNotFoundException_Exception {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.searchCarByModel(pickupTime, returnTime, poutletId, routletId, modelId);
    }

    private static java.util.List<ws.holidaySystemClient.Reservation> retrievePartnerReservations(Long partnerId) {
        ws.holidaySystemClient.PartnerWebService_Service service = new ws.holidaySystemClient.PartnerWebService_Service();
        ws.holidaySystemClient.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrievePartnerReservations(partnerId);
    }


}


