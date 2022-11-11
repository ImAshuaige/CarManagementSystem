/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementsystemclient;

import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Employee;
import entity.Reservation;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.EmployeeRoleEnum;
import util.enumeration.ReservationStatusEnum;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;
import util.exception.UnpaidRentalReservationException;

/**
 *
 * @author Mehak
 */
public class CustomerServiceModule {
    
    private Employee currEmployee;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;

    public CustomerServiceModule() {
    }

    public CustomerServiceModule(Employee currEmployee, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this();
        this.currEmployee = currEmployee;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }

    
    /**
     * @param args the command line arguments
     */
      public void menuCustomerService() throws InvalidAccessRightException /*, UnpaidRentalReservationException*/ {
          
          if(currEmployee.getEmployeeRole() != EmployeeRoleEnum.CUSTOMER_SERVICE_EXCUTIVE) {
              throw new InvalidAccessRightException("You don't have the right to access the customer service module.");
          }
          
          Scanner sc = new Scanner(System.in);
          Integer response = 0;
          
          while(true) {
              System.out.println("\n*** Welcome to CarMS Sales Management Menu ***\n");
              System.out.println("[1] Pick up car");
              System.out.println("[2] Return car");
              System.out.println("[3] Back\n");
              response = 0;
              
              while(response < 1 || response > 3) {
                  System.out.print("Your input: ");
                  response = sc.nextInt();
                  if(response == 1) {
                      pickUpCar();
                  } else if (response == 2) {
                      returnCar();
                  } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }
      
      private void pickUpCar() {
          Scanner sc = new Scanner(System.in);
          System.out.println("*** Pickup Car ***");
          //Make a new method in the reservationSessionBeanRemove called retrieveCustomerRentalReservationsByPickupOutletId
          List<Reservation> reservations = reservationSessionBeanRemote.retrieveCustomerRentalReservationsByPickupOutletId(currEmployee.getOutlet().getOutletId());
          if (reservations.isEmpty()) {
              System.out.println("No cars are avaialable to be picked up!");
          } else {
              try {
                System.out.printf("%4s%20s%20s\n", "ID", "Start Date", "End Date");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                for (Reservation reservation : reservations) {
                    System.out.printf("%4s%20s%20s\n", reservation.getRentalReservationId(),
                            sdf.format(reservation.getReservationStartDate()),
                            sdf.format(reservation.getReservationEndDate()));
                }
                
                System.out.print("Enter Rental Reservation ID> ");
                Long rentalReservationId = sc.nextLong();
                Reservation reservation = reservationSessionBeanRemote.retrieveReservationByReservationId(rentalReservationId);
                if(!reservation.getReservationStatus().equals(ReservationStatusEnum.PAID)) {
                    System.out.print("Pay rental fee? (Enter 'YES' to pay) : ");
                    String input = sc.nextLine().trim();
                    if (!input.equals("YES")) {
                        try {
                            throw new UnpaidRentalReservationException("Please pay for the rental reservation ID: " + rentalReservationId + " !");
                        } catch (UnpaidRentalReservationException ex) {
                            Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        System.out.println("Charged " + reservation.getReservationPrice().toString() + " to credit card: " + reservation.getCreditCardNumber());
                    }
                    try {
                        //Need to make the pickUpCar method in the reservation session bean
                        //And expose it to the local and remote interfaces
                        reservationSessionBeanRemote.pickupCar(rentalReservationId);
                    } catch (ReservationNotFoundException ex) {
                        Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Car successfully picked up by customer");
                }
                
                 System.out.print("Press any key to continue. ");
                sc.nextLine();
              } catch (ReservationNotFoundException ex) {
                  Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
      }
    
      private void returnCar() {
          Scanner sc = new Scanner(System.in);
          System.out.println("*** Return Car ***");
          List<Reservation> reservations = reservationSessionBeanRemote.retrieveCustomerRentalReservationsByPickupOutletId(currEmployee.getOutlet().getOutletId());
          
          if (!reservations.isEmpty()) {
              System.out.println("No cars to be returned!");
          } else {
            System.out.printf("%4s%20s%20s\n", "ID", "Start Date", "End Date");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (Reservation reservation : reservations) {
                    System.out.printf("%4s%20s%20s\n", reservation.getRentalReservationId(),
                            sdf.format(reservation.getReservationStartDate()),
                            sdf.format(reservation.getReservationEndDate()));
            }
            System.out.print("Enter Rental Reservation ID: ");
            Long rentalReservationId = sc.nextLong();
            sc.nextLine();
             try {
                 //Need to make the return car method in the session bean
                 //Expose it to the remote and local interface
                reservationSessionBeanRemote.returnCar(rentalReservationId);
                System.out.println("Car returned by customer");
            } catch (ReservationNotFoundException ex) {
                System.out.println("No Rental Reservation of ID: " + rentalReservationId);
            }
            System.out.print("Press any key to continue.");
            sc.nextLine();
        }
              
    }
        
}
