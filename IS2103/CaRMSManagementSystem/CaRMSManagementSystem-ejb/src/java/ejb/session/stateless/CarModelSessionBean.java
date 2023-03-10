/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.CarModel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelDeletionException;
import util.exception.CarModelNotFoundException;

/**
 *
 * @author 60540
 */
@Stateless
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCarModel(Long carCategoryId, CarModel model) throws CarCategoryNotFoundException {
        CarCategory carCategory = em.find(CarCategory.class, carCategoryId);

        if (carCategory == null) {
            throw new CarCategoryNotFoundException();
        }

        //bi-directional relationship, always remember to set both sides
        model.setBelongsCategory(carCategory);
        carCategory.getModelList().add(model);

        try {
            em.persist(model);
            em.flush();
            return model.getModelId();
        } catch (PersistenceException ex) {
            return (long) -1;
        }
    }

    @Override
    public List<CarModel> retrieveAllCarModels() {
        Query query = em.createQuery("SELECT m FROM CarModel m ORDER BY m.belongsCategory.categoryId ASC, m.make ASC, m.model ASC");
        return query.getResultList();
    }

    @Override
    public long updateModel(CarModel m, Long categoryId) throws CarCategoryNotFoundException, CarModelDeletionException {
        CarCategory currCategory = em.find(CarCategory.class, m.getBelongsCategory().getCategoryId());
        CarCategory newCategory = em.find(CarCategory.class, categoryId);

        if (newCategory == null) {
            throw new CarCategoryNotFoundException();
        }
        if (m.getDisabled() == true) {
            throw new CarModelDeletionException();
        }
        
        try {
            CarModel updatedModel = retrieveCarModelById(m.getModelId());
            updatedModel.setMake(m.getMake());
            updatedModel.setModel(m.getModel());
            //em.merge(m);
            if (updatedModel.getBelongsCategory().getCategoryId() != categoryId) { // need to update id 
                updatedModel.setBelongsCategory(newCategory);
                currCategory.getModelList().remove(updatedModel);
                newCategory.getModelList().add(updatedModel);
            }
            return updatedModel.getModelId();
        } catch (PersistenceException ex) {
            return (long) -1;
        } catch (CarModelNotFoundException ex1) {
            return (long) -1;
        }
    }

    @Override
    public void deleteModel(Long modelId) throws CarModelNotFoundException {
         //by now if a model is removed, it should not appear in the database
        //cannot just delete, can only delete when it is not used, otherwise set it to disable, does that mean we need a new attribute is applied?
        try {
            CarModel modelToRemove = retrieveCarModelById(modelId);
            if (modelToRemove.getListOfCars().isEmpty()) {
                em.remove(modelToRemove);
            } else {
                modelToRemove.setDisabled(true);
            }
        } catch (CarModelNotFoundException ex) {
            throw new CarModelNotFoundException("Model of ID: " + modelId + " not found!");
        }
    
    }
    
    @Override
    public CarModel retrieveCarModelById(long modelId) throws CarModelNotFoundException {
        CarModel m = em.find(CarModel.class, modelId);
        if (m == null) throw new CarModelNotFoundException("The Car Model is Not Found for ID " + modelId);
        return m;
    }

}
