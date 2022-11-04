/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarModel;
import entity.Outlet;
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
import util.enumeration.CarStatusEnum;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidModelException;
import util.exception.LicensePlateExistException;
import util.exception.ModelDisabledException;
import util.exception.OutletNotFoundException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @EJB
    private CarModelSessionBeanLocal carModelSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

        
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CarSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }


    @Override
    public long createNewCar (long modelId, long outletId, Car newCar ) throws UnknownPersistenceException, LicensePlateExistException, ModelDisabledException, OutletNotFoundException, InputDataValidationException {
            try {
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);

            if (constraintViolations.isEmpty()) {
                try {
                    Outlet outlet = outletSessionBean.retrieveOutletById(outletId);
                    CarModel model = carModelSessionBean.retrieveCarModelById(modelId);
                    if (model.getDisabled() == false) {
                        newCar.setCarModel(model);
                        newCar.setLatestOutlet(outlet);
                        newCar.setCarStatus(CarStatusEnum.AVAILABLE);
                        outlet.addCar(newCar);
                        model.addCar(newCar);
                        em.persist(newCar);
                        em.flush();
                        return newCar.getCarId();
                    } else {
                        throw new ModelDisabledException();
                    }
                } catch (OutletNotFoundException ex) {
                    throw new OutletNotFoundException("Outlet Not Found for ID: " + outletId);
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new LicensePlateExistException("License Plate: " + newCar.getCarLicensePlateNum() + " exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
  
    @Override
    public List<Car> retrieveCars() {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.disabled = FALSE ORDER BY c.carModel.belongsCategory.categoryId ASC, c.carModel.make ASC, c.carLicensePlateNum ASC");  
        return query.getResultList();
    }
    
  
    @Override
    public Car retrieveCarByCarId(Long carId) throws CarNotFoundException {
        Car car = em.find(Car.class, carId);

        if (car != null) {
            return car;
        } else {
            throw new CarNotFoundException("Car ID " + carId + " does not exist!");
        }
    }
    
    @Override
    public long updateCar(Car c, long outletId, long modelId) throws InvalidModelException, OutletNotFoundException {
            CarModel model = em.find(CarModel.class, modelId);
            if(model == null) throw new InvalidModelException();
            if(model.getDisabled() == true) 
                throw new InvalidModelException();
            Outlet newOutlet = em.find(Outlet.class,outletId);
            if(newOutlet == null) throw new OutletNotFoundException();
            
            try 
            {
                em.merge(c);
                em.flush();

                long carId = c.getCarId();
                Car car = em.find(Car.class, carId);
                if(outletId != c.getLatestOutlet().getOutletId()){
                    Outlet old = em.find(Outlet.class,c.getLatestOutlet().getOutletId());
                    old.getCarsList().remove(c);
                    newOutlet.getCarsList().add(c);
                    car.setLatestOutlet(newOutlet);
                }

                if(modelId!=c.getCarModel().getModelId()){
                    CarModel oldModel = em.find(CarModel.class, c.getCarModel().getModelId());
                    oldModel.getListOfCars().remove(c);
                    model.getListOfCars().add(car);
                    car.setCarModel(oldModel);
                }
                return c.getCarId();
            }
            catch (PersistenceException ex) 
            {
                return -1;
            }
    }
    

        @Override
        public void deleteCar(Long carId) throws CarNotFoundException {
        try {
            Car carToRemove = retrieveCarByCarId(carId);
            if (carToRemove.getCurrentReservation() == null) {
                em.remove(carToRemove);//For now it is being deleted, but if you only want to disable it, then I will make the change accordingly
            } else {
                carToRemove.setLatestOutlet(null);
                carToRemove.setDisabled(true);
            }
        } catch (CarNotFoundException ex) {
            throw new CarNotFoundException("Car of ID: " + carId + " not found!");
        }
        
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    

