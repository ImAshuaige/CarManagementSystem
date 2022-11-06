/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
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
import util.exception.DriverNotWorkingInSameOutletException;
import util.exception.EmployeeNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.TransitDriverDispatchNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFoundException;

/**
 *
 * @author 60540
 */
@Stateless
public class TransitDriverDispatchSessionBean implements TransitDriverDispatchSessionBeanRemote, TransitDriverDispatchSessionBeanLocal {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

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

    @Override
    public void updateTransitAsCompleted(Long transitDriverDispatchId) throws TransitDriverDispatchNotFoundException {
        try {
            TransitDriverDispatch transitDriverDispatch = retrieveTransitDriverDispatchByTransitDriverDispatchId(transitDriverDispatchId);
            transitDriverDispatch.setIsCompleted(true);
        } catch (TransitDriverDispatchNotFoundException ex) {
            throw new TransitDriverDispatchNotFoundException("Transit Driver Dispatch with Id: " + transitDriverDispatchId + " does not exist!");
        }
    }

    @Override
    public TransitDriverDispatch retrieveTransitDriverDispatchByTransitDriverDispatchId(Long transitDriverDispatchId) throws TransitDriverDispatchNotFoundException {
        TransitDriverDispatch transitDriverDispatch = em.find(TransitDriverDispatch.class, transitDriverDispatchId);

        if (transitDriverDispatch == null) {
            throw new TransitDriverDispatchNotFoundException("Transit Driver Dispatch with Id: " + transitDriverDispatchId + " does not exist!");
        } else {
            return transitDriverDispatch;
        }
    }
        
    @Override
        public void assignDriver(Long dispatchDriverId, Long transitDriverDispatchRecordId) throws DriverNotWorkingInSameOutletException, TransitDriverDispatchRecordNotFoundException, EmployeeNotFoundException, TransitDriverDispatchNotFoundException {
        try {
            //Importing the employee sessionBean first
            Employee dispatchDriver = employeeSessionBean.retrieveEmployeeByEmployeeId(dispatchDriverId);
            TransitDriverDispatch transitDriverDispatch = retrieveTransitDriverDispatchByTransitDriverDispatchId(transitDriverDispatchRecordId);
            if (dispatchDriver.getOutlet().getOutletName().equals(transitDriverDispatch.getDestinationOutlet().getOutletName())) {
                transitDriverDispatch.setDispatchDriver(dispatchDriver);
                dispatchDriver.addTransitDriverDispatchRecord(transitDriverDispatch);
            } else {
                throw new DriverNotWorkingInSameOutletException("Employee is not working in the current outlet");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new EmployeeNotFoundException("Employee ID: " + dispatchDriverId + " not found!");
        }
    }
}
