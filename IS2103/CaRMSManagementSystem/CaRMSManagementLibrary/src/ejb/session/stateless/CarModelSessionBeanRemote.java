/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;


/**
 *
 * @author 60540
 */
@Remote
public interface CarModelSessionBeanRemote {
        public Long createNewCarModel(Long carCategoryId, CarModel model) throws CarCategoryNotFoundException;
        public List<CarModel> retrieveAllCarModels();
        public long updateModel(CarModel m, Long categoryId) throws CarCategoryNotFoundException;
        public CarModel retrieveCarModelById(long modelId);
        public void deleteModel(Long modelId) throws CarModelNotFoundException;
}
