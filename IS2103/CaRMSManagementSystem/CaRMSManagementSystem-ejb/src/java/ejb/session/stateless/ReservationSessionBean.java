/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Outlet;
import entity.Partner;
import entity.Reservation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.enumeration.ReservationStatusEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoAvailableRentalRateException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewReservation(Long carCategoryId, Long modelId, Long customerId,
            Long pickupOutletId, Long returnOutletId, Reservation newReservation) throws InputDataValidationException, UnknownPersistenceException, OutletNotFoundException, CustomerNotFoundException, CarCategoryNotFoundException, CarModelNotFoundException {
        try {
            Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(newReservation);

            if (constraintViolations.isEmpty()) {
                //RetrieveCustomerByCustomerId in the customersessionbean
                Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                Outlet pickUpOutlet = outletSessionBeanLocal.retrieveOutletById(pickupOutletId);
                Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
                newReservation.setCustomer(customer);
                newReservation.setReservationPickUpOutlet(pickUpOutlet);
                newReservation.setReservationReturnOutlet(returnOutlet);

                customer.addReservation(newReservation);
                CarCategory carCategory = null;
                CarModel carModel = null;

                if (modelId > 0) {
                    carModel = carModelSessionBeanLocal.retrieveCarModelById(modelId);
                    carCategory = carModel.getBelongsCategory();
                    newReservation.setReservedCarModel(carModel);
                    newReservation.setReservedCarCategory(carCategory);
                } else {
                    carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
                    newReservation.setReservedCarCategory(carCategory);
                }
                em.persist(newReservation);
                em.flush();
                return newReservation.getRentalReservationId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet IDs: " + pickupOutletId + " and " + returnOutletId + " either or both does not exist!");
        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist!");
        } catch (CarCategoryNotFoundException ex) {
            throw new CarCategoryNotFoundException("Car Category ID: " + carCategoryId + " does not exist!");
        } catch (CarModelNotFoundException ex) {
            throw new CarModelNotFoundException("Model ID: " + modelId + " does not exist!");
        }

    }

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

    @Override
    public List<Reservation> retrieveCustomerReservations(Long customerId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.customer.customerId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }

    //Expose it to the local and remote interfaces
    @Override
    public BigDecimal cancelReservation(Long reservationId) throws ReservationNotFoundException {
        try {
            Reservation reservation = retrieveReservationByReservationId(reservationId);
            reservation.setIsCancelled(Boolean.TRUE);
            LocalDateTime startDateTemporal = reservation.getReservationStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Long numofDaysToStartTheReservation = ChronoUnit.DAYS.between(today, startDateTemporal);
            BigDecimal reservationPrice = reservation.getReservationPrice();
            BigDecimal penalty = null;

            if (numofDaysToStartTheReservation >= 14) {
                penalty = new BigDecimal(0);
            } else if (numofDaysToStartTheReservation < 14 && numofDaysToStartTheReservation >= 7) {
                penalty = reservationPrice.multiply(new BigDecimal(0.2));
            } else if (numofDaysToStartTheReservation < 7 && numofDaysToStartTheReservation >= 3) {
                penalty = reservationPrice.multiply(new BigDecimal(0.5));
            } else if (numofDaysToStartTheReservation < 3) {
                penalty = reservationPrice.multiply(new BigDecimal(0.7));
            }

            return penalty;

        } catch (ReservationNotFoundException ex) {
            throw new ReservationNotFoundException("Rental Reservation of ID: " + reservationId + " not found!");
        }

    }

    @Override
    public Long createNewPartnerRentalReservation(Long carCategoryId, Long partnerId, Long modelId, Long customerId,
            Long pickupOutletId, Long returnOutletId, Reservation newReservation) throws InputDataValidationException, UnknownPersistenceException, OutletNotFoundException, CustomerNotFoundException, CarCategoryNotFoundException, CarModelNotFoundException, PartnerNotFoundException {
        try {
            Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(newReservation);

            if (constraintViolations.isEmpty()) {
                Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                Partner partner = partnerSessionBeanLocal.retrievePartnerByPartnerId(partnerId);
                Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletById(pickupOutletId);
                Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletById(returnOutletId);
                newReservation.setCustomer(customer);
                newReservation.setReservationPartner(partner);
                newReservation.setReservationPickUpOutlet(pickupOutlet);
                newReservation.setReservationReturnOutlet(returnOutlet);
                customer.addReservation(newReservation);

                CarCategory carCategory = null;
                CarModel model = null;
                if (carCategoryId > -1) {
                    carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
                    newReservation.setReservedCarCategory(carCategory);
                } else {
                    model = carModelSessionBeanLocal.retrieveCarModelById(modelId);
                    newReservation.setReservedCarModel(model);
                }
                em.persist(newReservation);
                em.flush();
                return newReservation.getRentalReservationId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet IDs: " + pickupOutletId + " and " + returnOutletId + " either or both does not exist!");
        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist!");
        } catch (CarCategoryNotFoundException ex) {
            throw new CarCategoryNotFoundException("Car Category ID: " + carCategoryId + " does not exist!");
        } catch (CarModelNotFoundException ex) {
            throw new CarModelNotFoundException("Model ID: " + modelId + " does not exist!");
        } catch (PartnerNotFoundException ex) {
            throw new PartnerNotFoundException("Partner ID: " + partnerId + " does not exist!");
        }

    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }
    
    @Override
    public List<Reservation> retrievePartnerReservations(Long partnerId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.reservationPartner.partnerId = :inPartnerId");
        query.setParameter("inPartnerId", partnerId);
        return query.getResultList();
    }

}
