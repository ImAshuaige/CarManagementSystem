/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
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
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
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
    public Long createNewRentalRate (Long carCategoryId, RentalRate rentalRate) throws CarCategoryNotFoundException, UnknownPersistenceException, InputDataValidationException {
        
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
       Query query = em.createQuery("SELECT rate FROM RentalRate rate");
       return query.getResultList();
    }
    
        private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
}
