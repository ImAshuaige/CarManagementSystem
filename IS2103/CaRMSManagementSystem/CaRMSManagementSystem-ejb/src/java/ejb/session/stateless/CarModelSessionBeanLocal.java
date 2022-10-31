/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;


/**
 *
 * @author 60540
 */
@Local
public interface CarModelSessionBeanLocal {
        public Long createNewCarModel(Long carCategoryId, CarModel model) throws CarCategoryNotFoundException;
}
