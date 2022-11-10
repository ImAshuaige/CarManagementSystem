/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Local
public interface ReservationSessionBeanLocal {

    public Reservation retrieveReservationByReservationId(Long rentalReservationId) throws ReservationNotFoundException;

    public List<Reservation> retrieveCustomerRentalReservationsByPickupOutletId(long outletId);

    public List<Reservation> retrieveCustomerRentalReservationsByReturnOutletId(Long outletId);

    public void pickupCar(Long rentalReservationId) throws ReservationNotFoundException;

    public void returnCar(Long rentalReservationId) throws ReservationNotFoundException;

    public Boolean searchCarByCategory(Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, Long carCategoryId) throws CarCategoryNotFoundException, OutletNotFoundException;

    public Boolean searchCarByModel(Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, Long modelId) throws CarCategoryNotFoundException, OutletNotFoundException, CarModelNotFoundException;
    
    public Long createNewReservation(Long carCategoryId, Long modelId, Long customerId, Long pickupOutletId, Long returnOutletId, Reservation newReservation) throws InputDataValidationException, UnknownPersistenceException, OutletNotFoundException, CustomerNotFoundException, CarCategoryNotFoundException, CarModelNotFoundException;
    
    public List<Reservation> retrieveCustomerReservations(Long customerId);
    
    public BigDecimal cancelReservation(Long reservationId) throws ReservationNotFoundException;
    
    public Long createNewPartnerRentalReservation(Long carCategoryId, Long partnerId, Long modelId, Long customerId, Long pickupOutletId, Long returnOutletId, Reservation newReservation) throws InputDataValidationException, UnknownPersistenceException, OutletNotFoundException, CustomerNotFoundException, CarCategoryNotFoundException, CarModelNotFoundException, PartnerNotFoundException;
    
    
    
    
}
