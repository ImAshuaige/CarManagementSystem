/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import entity.Reservation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.enumeration.ReservationStatusEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.NoAvailableRentalRateException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author 60540
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    
    
    
    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    //Expose it in the local and remote interfaces
    @Override
    public Reservation retrieveReservationByReservationId(Long rentalReservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, rentalReservationId);

        if (reservation != null) {
            return reservation;
        } else {
            throw new ReservationNotFoundException("Reservation ID " + rentalReservationId + " does not exist!");
        }
    }

    //Expose it to the local and remote interface
    @Override
    public List<Reservation> retrieveCustomerRentalReservationsByPickupOutletId(long outletId) {
        //Be extra careful with queries

        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.isPicked = FALSE"
                + " AND r.car IS NOT NULL"
                + " AND r.reservationPickUpOutlet.outletId =:inOutletId ");
        query.setParameter("inOutletId", outletId);
        return query.getResultList();
    }

    @Override
    public List<Reservation> retrieveCustomerRentalReservationsByReturnOutletId(Long outletId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.isCompleted = FALSE"
                + " AND r.isPicked = TRUE"
                + " AND r.car IS NOT NULL"
                + " AND r.reservationReturnOutlet.outletId = :inOutletId");
        query.setParameter("inOutletId", outletId);
        return query.getResultList();
    }

    @Override
    public void pickupCar(Long rentalReservationId) throws ReservationNotFoundException {
        try {
            Reservation reservation = retrieveReservationByReservationId(rentalReservationId);
            Car car = reservation.getCar();
            car.setCarStatus(CarStatusEnum.IN_RENT);
            car.setLatestOutlet(null);
            car.setCurrentReservation(reservation);

            reservation.setReservationStatus(ReservationStatusEnum.PAID);
            reservation.getReservationPickUpOutlet().removeCar(car);
        } catch (ReservationNotFoundException ex) {
            throw new ReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + "not found!");
        }
    }

    @Override
    public void returnCar(Long rentalReservationId) throws ReservationNotFoundException {
        try {
            Reservation reservation = retrieveReservationByReservationId(rentalReservationId);
            Outlet returnOutlet = reservation.getReservationReturnOutlet();
            Car car = reservation.getCar();
            car.setCarStatus(CarStatusEnum.AVAILABLE);
            car.setLatestOutlet(returnOutlet);
            car.setCurrentReservation(null);
            returnOutlet.addCar(car);
            reservation.setReservationStatus(ReservationStatusEnum.COMPLETED);
        } catch (ReservationNotFoundException ex) {
            throw new ReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + "not found!");
        }

        // Add business logic below. (Right-click in editor and choose
        // "Insert Code > Add Business Method")
    }

    @Override
    public Boolean searchCarByCategory(Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, Long carCategoryId) throws /*NoAvailableRentalRateException,*/ CarCategoryNotFoundException, OutletNotFoundException {
        List<Reservation> reservations = new ArrayList<>();

        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarCategory.categoryId = :inCategoryId"
                + " AND r.reservationStartDate < :inPickupDate AND r.reservationEndDate <= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        reservations.addAll(query.getResultList());

        query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarCategory.categoryId = :inCategoryId"
                + " AND r.reservationStartDate >= :inPickupDate AND r.reservationEndDate <= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        reservations.addAll(query.getResultList());
        //System.out.println("1 - query.getResultList() : " + query.getResultList());

        query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarCategory.categoryId = :inCategoryId"
                + " AND r.reservationStartDate >= :inPickupDate AND r.reservationEndDate > :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        reservations.addAll(query.getResultList());
        //System.out.println("2 - query.getResultList() : " + query.getResultList());

        query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarCategory.categoryId = :inCategoryId"
                + " AND r.reservationStartDate <= :inPickupDate AND r.reservationEndDate >= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        reservations.addAll(query.getResultList());
        //System.out.println("3 - query.getResultList() : " + query.getResultList());

        GregorianCalendar calendar = new GregorianCalendar(pickUpDateTime.getYear() + 1900,
                pickUpDateTime.getMonth(), pickUpDateTime.getDate(), pickUpDateTime.getHours(),
                pickUpDateTime.getMinutes(), pickUpDateTime.getSeconds());
        calendar.add(Calendar.HOUR, -2);
        Date transitDate = calendar.getTime();

        query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarCategory.categoryId = :inCategoryId"
                + " AND r.reservationStartDate < :inPickupDate AND r.reservationEndDate > :inTransitDate"
                + " AND r.reservationReturnOutlet.outletId <> :inPickupOutletId"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inTransitDate", transitDate);
        query.setParameter("inPickupOutletId", pickupOutletId);
        reservations.addAll(query.getResultList());
        //System.out.println("4 - query.getResultList() : " + query.getResultList());

        CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
        List<Car> cars = new ArrayList<>();
        for (CarModel model : carCategory.getModelList()) {
            cars.addAll(model.getListOfCars());
        }
        //System.out.println("cars : " + cars.size());
        //System.out.println("rentalReservations.size() : " + reservations.size());

        if (cars.size() > reservations.size()) {
            return true;
        } else {
            return false;
        }
    }
    
    
    
    @Override
    public Boolean searchCarByModel(Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, Long modelId) throws /*NoAvailableRentalRateException,*/ CarCategoryNotFoundException, OutletNotFoundException, CarModelNotFoundException {
        List<Reservation> reservations = new ArrayList<>();

        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarModel.modelId = :inModelId"
                + " AND r.reservationStartDate < :inPickupDate AND r.reservationEndDate <= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        reservations.addAll(query.getResultList());

        query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarModel.modelId = :inModelId"
                + " AND r.reservationStartDate >= :inPickupDate AND r.reservationEndDate <= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        reservations.addAll(query.getResultList());

        query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarModel.modelId = :inModelId"
                + " AND r.reservationStartDate >= :inPickupDate AND r.reservationEndDate > :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        reservations.addAll(query.getResultList());

        query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarModel.modelId = :inModelId"
                + " AND r.reservationStartDate <= :inPickupDate AND r.reservationEndDate >= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        reservations.addAll(query.getResultList());

        GregorianCalendar calendar = new GregorianCalendar(pickUpDateTime.getYear() + 1900,
                pickUpDateTime.getMonth(), pickUpDateTime.getDate(), pickUpDateTime.getHours(),
                pickUpDateTime.getMinutes(), pickUpDateTime.getSeconds());
        calendar.add(Calendar.HOUR, -2);
        Date transitDate = calendar.getTime();

        query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservedCarModel.modelId = :inModelId"
                + " AND r.reservationStartDate < :inPickupDate AND r.reservationEndDate > :inTransitDate"
                + " AND r.reservationReturnOutlet.outletId <> :inPickupOutletId"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inTransitDate", transitDate);
        query.setParameter("inPickupOutletId", pickupOutletId);
        reservations.addAll(query.getResultList());

        CarModel model = carModelSessionBeanLocal.retrieveCarModelById(modelId);
        if (model.getListOfCars().size() > reservations.size()) {
            return true;
        } else {
            return false;
        }
    }

}
