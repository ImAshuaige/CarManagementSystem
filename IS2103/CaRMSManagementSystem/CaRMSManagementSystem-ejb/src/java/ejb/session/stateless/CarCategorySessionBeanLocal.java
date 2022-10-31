/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;

/**
 *
 * @author 60540
 */
@Local
public interface CarCategorySessionBeanLocal {

    public Long createNewCarCategory(CarCategory carCategory);

    public List<CarCategory> retrieveAllCarCategory();

    public CarCategory retrieveCarCategoryByCarCategoryId(Long CarCategoryId) throws CarCategoryNotFoundException;

    
    
}
