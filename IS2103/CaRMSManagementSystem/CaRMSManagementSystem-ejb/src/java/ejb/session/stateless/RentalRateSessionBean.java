/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.RentalRateType;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoAvailableRentalRateException;
import util.exception.RentalDateDeletionException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBean;

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    /*
        // Added in v4.2 for bean validation
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public ProductEntitySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
     */
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RentalRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    //Expose new method to the beans
    //Need to handle potential errors
    @Override
    public Long createNewRentalRate(Long carCategoryId, RentalRate rentalRate) throws CarCategoryNotFoundException, UnknownPersistenceException, InputDataValidationException {

        try {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

            if (constraintViolations.isEmpty()) {
                try {
                    CarCategory carCategory = carCategorySessionBean.retrieveCarCategoryByCarCategoryId(carCategoryId);
                    carCategory.addRentalRate(rentalRate);
                    rentalRate.setCarCategory(carCategory);
                    em.persist(rentalRate);
                    em.flush();
                    return rentalRate.getRentalRateId();
                } catch (CarCategoryNotFoundException ex) {
                    throw new CarCategoryNotFoundException("Car Category ID: " + carCategoryId + " not found!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }

    }

    /**
     *
     * @return
     */
    @Override
    public List<RentalRate> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT r FROM RentalRate r ORDER BY r.carCategory.carCategoryName ASC, r.rateStartDate ASC, r.rateEndDate ASC");
        return query.getResultList();

        /*  this.rentalName = rentalName;
        this.dailyRate = dailyRate;
        this.rateStartDate = rateStartDate;
        this.rateEndDate = rateEndDate;
        this.carCategory = carCategory;
         */
    }

    @Override
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);

        if (rentalRate != null) {
            return rentalRate;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }

    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException, RentalDateDeletionException {
        if (rentalRate != null && rentalRate.getRentalRateId() != null) {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

            if (constraintViolations.isEmpty()) {
                if (rentalRate.isIsDisabled() == true) {
                    throw new RentalDateDeletionException();
                }

                if (rentalRate.isIsDisabled() == false) {
                    RentalRate rentalRateToUpdate = retrieveRentalRateByRentalRateId(rentalRate.getRentalRateId());
                    rentalRateToUpdate.setRentalName(rentalRate.getRentalName());
                    rentalRateToUpdate.setCarCategory(rentalRate.getCarCategory());
                    rentalRateToUpdate.setDailyRate(rentalRate.getDailyRate());
                    rentalRateToUpdate.setRateStartDate(rentalRate.getRateStartDate());
                    rentalRateToUpdate.setRateEndDate(rentalRate.getRateEndDate());
                    rentalRateToUpdate.setRateType(rentalRate.getRateType());
                }

            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new RentalRateNotFoundException("Rental Rate Id not found for the rental rate that needs to be update");
        }
    }

    //Changed the implementation so that it is deleted if the rental rate is not being applied and disabled other
    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException {
        try {
            RentalRate rentalRateToRemove = retrieveRentalRateByRentalRateId(rentalRateId);
            if (rentalRateToRemove.isIsApplied() == false) { //Hester, I think we need a list of rental days and another isRentalRateEnabled attribute. 
                //It is different from IsApplied.
                em.remove(rentalRateToRemove);
            } else {
                rentalRateToRemove.setIsDisabled(true);
            }
        } catch (RentalRateNotFoundException ex) {
            throw new RentalRateNotFoundException("Rental rate of ID: " + rentalRateId + " not found!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public RentalRate retrieveLowestRentalRate(Long carCategoryId, Date currDate) throws NoAvailableRentalRateException {
        //promotion -> peak -> default

        //retrieve the promotion list first
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE (r.carCategory.categoryId = :inCarCategoryId)"
                + " AND (r.rateStartDate <= :inCurrDate AND r.rateEndDate >= :inCurrDate)"
                + " OR (r.rateStartDate IS NULL AND r.rateEndDate IS NULL) ORDER BY r.dailyRate ASC");
        query.setParameter("inCurrDate", currDate);
        query.setParameter("inCarCategoryId", carCategoryId);
        //retrieve the list of available rental rate in the current day
        List<RentalRate> rentalRates = query.getResultList();
        //first consider if there's any promotion rate
        
        
        //actually we have already sort it in asc order, we do not need to do the extra steps,just to be safe
        BigDecimal lowestDailyPrice = new BigDecimal(0);//set default price 
        RentalRate currRate = null;
        for (RentalRate rentalRate : rentalRates) {
            if (rentalRate.getRateType() == RentalRateType.PROMOTION) {
                System.out.println("HAVE PROMOTION");
                if (lowestDailyPrice.compareTo(new BigDecimal(0)) == 0) {
                    lowestDailyPrice = rentalRate.getDailyRate();
                    currRate = rentalRate;
                } else if (rentalRate.getDailyRate().compareTo(lowestDailyPrice) < 0) {
                    lowestDailyPrice = rentalRate.getDailyRate();
                    currRate = rentalRate;
                }
            }
        }
        if (currRate != null) {
            return currRate;
        }

        //if there is no promo, then try if there is rental Rates 
        for (RentalRate rentalRate : rentalRates) {
            
            if (rentalRate.getRateType() == RentalRateType.PEAK) {
                System.out.println("HAVE PEAK");
                if (lowestDailyPrice.compareTo(new BigDecimal(0)) == 0) {
                    lowestDailyPrice = rentalRate.getDailyRate();
                    currRate = rentalRate;
                } else if (rentalRate.getDailyRate().compareTo(lowestDailyPrice) < 0) {
                    lowestDailyPrice = rentalRate.getDailyRate();
                    currRate = rentalRate;
                }
            }
        }
        
        

        if (currRate != null) {
            return currRate;
        }

        //lastly, apply the lowest default rate
        for (RentalRate rentalRate : rentalRates) {
            if (rentalRate.getRateType() == RentalRateType.DEFAULT) {
                System.out.println("HAVE default");
                if (lowestDailyPrice.compareTo(new BigDecimal(0)) == 0) {
                    lowestDailyPrice = rentalRate.getDailyRate();
                    currRate = rentalRate;
                } else if (rentalRate.getDailyRate().compareTo(lowestDailyPrice) < 0) {
                    lowestDailyPrice = rentalRate.getDailyRate();
                    currRate = rentalRate;
                }
            }
        }
        return currRate;

    }
}

    /*    Query query = em.createQuery("SELECT r FROM RentalRate r WHERE (r.carCategory.categoryId = :inCarCategoryId)"
                + " AND (r.rateStartDate <= :inCurrDate AND r.rateEndDate >= :inCurrDate)"
                + " OR (r.rateStartDate IS NULL AND r.rateEndDate IS NULL) ORDER BY r.dailyRate ASC");
        query.setParameter("inCurrDate", currDate);
        query.setParameter("inCarCategoryId", carCategoryId);
        List<RentalRate> rentalRates = query.getResultList();
        if (rentalRates.isEmpty()) {
            throw new NoAvailableRentalRateException();
        }
        return rentalRates.get(0);
    }*/
//}
