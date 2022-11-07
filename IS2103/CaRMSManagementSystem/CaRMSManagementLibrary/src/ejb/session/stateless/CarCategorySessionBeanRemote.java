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
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
import util.exception.NoAvailableRentalRateException;

/**
 *
 * @author 60540
 */
@Remote
public interface CarCategorySessionBeanRemote {
    
    public Long createNewCarCategory(CarCategory carCategory);
    
    public List<CarCategory> retrieveAllCarCategory();
    
    public CarCategory retrieveCarCategoryByCarCategoryId(Long CarCategoryId) throws CarCategoryNotFoundException;

    public BigDecimal calculateRentalFee(Long carCategoryId, Date pickUpDateTime, Date returnDateTime) throws NoAvailableRentalRateException;

    
}
