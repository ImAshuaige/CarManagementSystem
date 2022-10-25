/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author 60540
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewRentalRate (RentalRate rentalRate) {
        em.persist(rentalRate);
        em.flush();
        return rentalRate.getRentalRateId();
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
    
    
}
