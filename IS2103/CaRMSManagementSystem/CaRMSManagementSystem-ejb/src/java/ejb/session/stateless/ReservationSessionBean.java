/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.CarStatusEnum;
import util.enumeration.ReservationStatusEnum;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author 60540
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

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
        + " AND r.reservationReturnOutlet.outletId = :inOutletId" );
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
        } catch (ReservationNotFoundException ex){
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
        }  catch (ReservationNotFoundException ex) {
            throw new ReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + "not found!");
    }
   

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    }
}
