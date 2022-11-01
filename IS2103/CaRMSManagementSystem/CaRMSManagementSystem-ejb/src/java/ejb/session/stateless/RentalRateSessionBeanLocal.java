/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RentalDateDeletionException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Local
public interface RentalRateSessionBeanLocal {
    
    public Long createNewRentalRate(Long carCategoryId, RentalRate rentalRate) throws CarCategoryNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<RentalRate> retrieveAllRentalRates();

    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;

    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException, RentalDateDeletionException;

    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException;
}
