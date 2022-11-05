/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatch;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author 60540
 */
@Stateless
public class TransitDriverDispatchSessionBean implements TransitDriverDispatchSessionBeanRemote, TransitDriverDispatchSessionBeanLocal {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<TransitDriverDispatch> retrieveTransitDriverDispatchByOutletId(Date date, Long outletId) {
        date.setHours(2);
        date.setMinutes(0);
        date.setSeconds(0);
        GregorianCalendar calendar = new GregorianCalendar(date.getYear() + 1900,
                date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
        calendar.add(Calendar.DATE, 1);
        Date nextDay = calendar.getTime();
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatch t WHERE t.destinationOutlet.outletId = :inOutletId AND t.transitDate >= :inToday AND t.transitDate < :inNextDay");
        query.setParameter("inOutletId", outletId);
        query.setParameter("inToday", date);
        query.setParameter("inNextDay", nextDay);
        return query.getResultList();
    }
    
    @Override
    public long createNewTranspatchDriverRecord(Long destinationOutletId, Long reservationId, Date transitStartDate) throws OutletNotFoundException, ReservationNotFoundException {
        try {
            //Now our TransitDriverDispatch only takes in a date
            TransitDriverDispatch transitDriverDispatch = new TransitDriverDispatch(transitStartDate);
            //Importing the outletsessionbean
            Outlet destinationOutlet = outletSessionBean.retrieveOutletById(destinationOutletId);
            //Import the reservationSessionBeanLocal
            
            //Creating retrieveRentalReservationByRentalReservationId method in the reservationSessionBeanLocal
            Reservation rentalReservation = reservationSessionBean.retrieveReservationByReservationId(reservationId);
            transitDriverDispatch.setDestinationOutlet(destinationOutlet);
            destinationOutlet.getTransitDriverDispatchList().add(transitDriverDispatch);
            transitDriverDispatch.setRentalReservation(rentalReservation);
            rentalReservation.setReservationDispatchDriver(transitDriverDispatch);
            em.persist(transitDriverDispatch);
            em.flush();
            return transitDriverDispatch.getTransitDriverDispatchId();
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet ID: " + destinationOutletId + " not found!");
        } catch (ReservationNotFoundException ex) {
            throw new ReservationNotFoundException("Reservation ID: " + reservationId + " not found!");
        }
    }

}
