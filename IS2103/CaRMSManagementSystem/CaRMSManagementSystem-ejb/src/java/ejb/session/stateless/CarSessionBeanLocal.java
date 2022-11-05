/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidModelException;
import util.exception.LicensePlateExistException;
import util.exception.ModelDisabledException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Local
public interface CarSessionBeanLocal {
    public void deleteCar(Long carId) throws CarNotFoundException;

    public long updateCar(Car c, long outletId, long modelId) throws InvalidModelException, CarModelNotFoundException, CarModelNotFoundException, OutletNotFoundException;

    public Car retrieveCarByCarId(Long carId) throws CarNotFoundException;

    public List<Car> retrieveCars();

    public long createNewCar(long modelId, long outletId, Car newCar) throws CarModelNotFoundException, UnknownPersistenceException, LicensePlateExistException, ModelDisabledException, OutletNotFoundException, InputDataValidationException;
    
}
