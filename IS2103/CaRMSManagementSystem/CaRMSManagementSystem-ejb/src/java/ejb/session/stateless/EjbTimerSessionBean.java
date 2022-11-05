/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Reservation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.exception.NoAvailableCarException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author 60540
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    @EJB
    private TransitDriverDispatchSessionBeanLocal transitDriverDispatchSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @Schedule(hour = "2", minute = "0", second = "0", info = "allocateCarsToCurrentDayReservations")
    public void triggerCarAllocation() throws ReservationNotFoundException, NoAvailableCarException {
        Date date = new Date();
        allocateCarsToCurrentDayReservations(date);
    }

    public void allocateCarsToCurrentDayReservations(Date date) {
        Date start = date;
        //minimum transit time of 2 hours
        start.setHours(2);
        start.setMinutes(0);
        start.setSeconds(0);
        GregorianCalendar calendar = new GregorianCalendar(start.getYear() + 1900, start.getMonth(), start.getDate(), start.getHours(), start.getMinutes(), start.getSeconds());
        calendar.add(Calendar.DATE, 1);
        Date end = calendar.getTime();
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservationStartDate >= :inStartDate AND r.reservationStartDate <= :inEndDate");
        query.setParameter("inStartDate", start);
        query.setParameter("inEndDate", end);
        List<Reservation> reservations = query.getResultList();
        List<Reservation> requireTransitReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            boolean isAllocated = false;
            
            //CASE 1: the car (model) is available and allocatable
            if (reservation.getReservedCarModel() != null) { 
                List<Car> cars = carSessionBeanLocal.retrieveCarsByModelId(reservation.getReservedCarModel().getModelId());
                for (Car car : cars) {
                    if ((car.getCarStatus() == CarStatusEnum.AVAILABLE && car.getCurrentReservation() == null)
                            && (car.getLatestOutlet().getOutletName().equals(reservation.getReservationPickUpOutlet().getOutletName()))) {
                        //bidirectional set both sides 
                        reservation.setCar(car);
                        car.setCurrentReservation(reservation);
                        isAllocated = true;
                        break;
                    }
                }
                if (isAllocated) {//break out the for loop
                    continue;
                }
                
                //CASE 2: availble car, but return outlet different from pick up outlet, need transit 
                for (Car car : cars) {
                    if (car.getCarStatus() == CarStatusEnum.AVAILABLE && car.getCurrentReservation() == null) {
                        reservation.setCar(car);
                        car.setCurrentReservation(reservation);
                        isAllocated = true;
                        requireTransitReservations.add(reservation);
                        break;
                    }
                }
                if (isAllocated) {
                    continue; //break out the for loop
                }
                //Case 3: Check those currently on rental returning to SAME outlet
                // I do not get this part, if the car is currently in_rental, how come get CurrentReservation() == null??
                // This really need to check carefully when implementing add a reservation use case
                // I change the codes. 
                for (Car car : cars) {
                    if ((car.getCarStatus() == CarStatusEnum.IN_RENT )/*&& car.getCurrentReservation() == null)*/
                            && car.getCurrentReservation().getReservationReturnOutlet().getOutletId().equals(reservation.getReservationPickUpOutlet().getOutletId())) {
                        if (car.getCurrentReservation().getReservationEndDate().before(reservation.getReservationStartDate())) {
                            reservation.setCar(car);
                            //havent set the car reservation because it is currently serving another reservation. 
                            //NEED TO CONSIDER WHEN TO CHANGE THE CAR RESERVATION TO THE CURRENT ONE?
                            isAllocated = true;
                            break;
                        }
                    }
                }
                if (isAllocated) {//break out the for loop
                    continue;
                }
                
                
                // CASE 4: then check those currently on rental returning to a DIFFERENT outlet
                for (Car car : cars) {
                    if (car.getCarStatus() == CarStatusEnum.IN_RENT/*) && car.getRentalReservation() == null*/
                          /*  && car.getCurrentReservation().getReservationReturnOutlet().equals(reservation.getReservationPickupOutlet())*/) {
                        GregorianCalendar transitCalendar = new GregorianCalendar(
                                car.getCurrentReservation().getReservationEndDate().getYear() + 1900,
                                car.getCurrentReservation().getReservationEndDate().getMonth(),
                                car.getCurrentReservation().getReservationEndDate().getDate(),
                                start.getHours(), start.getMinutes(), start.getSeconds());
                        transitCalendar.add(Calendar.HOUR, 2);
                        Date transitEndTime = transitCalendar.getTime();
                        if (reservation.getReservationStartDate().after(transitEndTime)) {
                            reservation.setCar(car);
                            isAllocated = true;
                            requireTransitReservations.add(reservation);
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    continue;
                }
            } else { // Reservation By Car Category
                
                //CASE 1: the car is available and allocatable
                List<Car> cars = carSessionBeanLocal.retrieveCarsByCategoryId(reservation.getReservedCarCategory().getCategoryId());
                for (Car car : cars) {
                    if (car.getCarModel().getBelongsCategory().getCarCategoryName().equals(reservation.getReservedCarCategory().getCarCategoryName()) 
                            && car.getCurrentReservation() == null
                            && car.getLatestOutlet().getOutletId().equals(reservation.getReservationPickUpOutlet().getOutletId())) {
                        
                        reservation.setCar(car);
                        car.setCurrentReservation(reservation);
                        isAllocated = true;
                        break;
                    }
                }
                if (isAllocated) {
                    continue;
                }
                
                
                // //CASE 2: availble car, but return outlet different from pick up outlet, need transit
                Long carCategoryId = reservation.getReservedCarCategory().getCategoryId();
                List<Car> sameCategoryCars = carSessionBeanLocal.retrieveCarsByCategoryId(carCategoryId);
                for (Car car : sameCategoryCars) {
                    if ((car.getCarStatus() == CarStatusEnum.AVAILABLE) && car.getCurrentReservation() == null) { 
                        reservation.setCar(car);
                        car.setCurrentReservation(reservation);
                        isAllocated = true;
                        requireTransitReservations.add(reservation);
                        break;
                    }
                }
                if (isAllocated) {
                    continue;
                }
                
                
                //Case 3: Check those currently on rental returning to SAME outlet
                for (Car car : sameCategoryCars) {
                    if ((car.getCarStatus() == CarStatusEnum.IN_RENT) && car.getCurrentReservation().getReservationReturnOutlet().getOutletName()
                            .equals(reservation.getReservationPickUpOutlet().getOutletName())) {
                        if (car.getCurrentReservation().getReservationEndDate().before(reservation.getReservationStartDate())) {
                            reservation.setCar(car);
                            isAllocated = true;
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    continue;
                }
                
                
                
                //// CASE 4: then check those currently on rental returning to a DIFFERENT outlet
                for (Car car : sameCategoryCars) {
                    if ((car.getCarStatus() == CarStatusEnum.IN_RENT) 
                            && car.getCurrentReservation().getReservationReturnOutlet().equals(reservation.getReservationPickUpOutlet())) {
                        
                        GregorianCalendar transitCalendar = new GregorianCalendar(
                                car.getCurrentReservation().getReservationEndDate().getYear() + 1900,
                                car.getCurrentReservation().getReservationEndDate().getMonth(),
                                car.getCurrentReservation().getReservationEndDate().getDate(),
                                car.getCurrentReservation().getReservationEndDate().getHours(),
                                car.getCurrentReservation().getReservationEndDate().getMinutes(),
                                car.getCurrentReservation().getReservationEndDate().getSeconds());
                        transitCalendar.add(Calendar.HOUR, 2);
                        Date transitEndTime = transitCalendar.getTime();
                        if (reservation.getReservationStartDate().after(transitEndTime)) {
                            reservation.setCar(car);
                            isAllocated = true;
                            requireTransitReservations.add(reservation);
                            break;
                        }
                    }
                }
                if (isAllocated) {
                    continue;
                }
            }
        }
        generateTransitDriverDispatchRecords(date, requireTransitReservations);
    }

    public void generateTransitDriverDispatchRecords(Date date, List<Reservation> rentalReservationsToBeAllocated) {
        try {
            for (Reservation rentalReservation : rentalReservationsToBeAllocated) {
                Date transitStartDate = date;
                GregorianCalendar transitCalendar = new GregorianCalendar(
                        rentalReservation.getReservationStartDate().getYear() + 1900,
                        rentalReservation.getReservationStartDate().getMonth(),
                        rentalReservation.getReservationStartDate().getDate(),
                        rentalReservation.getReservationStartDate().getHours(),
                        rentalReservation.getReservationStartDate().getMinutes(),
                        rentalReservation.getReservationStartDate().getSeconds());
                transitCalendar.add(Calendar.HOUR, -2);
                transitStartDate = transitCalendar.getTime();
                 transitDriverDispatchSessionBeanLocal.createNewTranspatchDriverRecord(rentalReservation.getReservationPickUpOutlet().getOutletId(),
                                rentalReservation.getRentalReservationId(), transitStartDate);
            }
        } catch (ReservationNotFoundException | OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}


