/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;


/**
 *
 * @author 60540
 */
@Stateless
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewCarCategory(CarCategory carCategory) {
        em.persist(carCategory);
        em.flush();
        return carCategory.getCategoryId();
    }
    
  
    
    @Override
    public List<CarCategory> retrieveAllCarCategory() {
       Query query = em.createQuery("SELECT cat FROM CarCategory cat");
       return query.getResultList();
    }
    
        

    public CarCategory retrieveCarCategoryByCarCategoryId(Long CarCategoryId) throws CarCategoryNotFoundException
    {
        CarCategory carCategory = em.find(CarCategory.class, CarCategoryId);
        
        if(carCategory != null)
        {
            return carCategory;
        }
        else
        {
            throw new CarCategoryNotFoundException("Car Category ID " + CarCategoryId + " does not exist!");
        }               
    }

    
}
