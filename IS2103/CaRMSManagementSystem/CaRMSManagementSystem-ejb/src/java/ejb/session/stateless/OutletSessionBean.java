/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OutletNotFoundException;

/**
 *
 * @author 60540
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewOutlet(Outlet outlet) {
        em.persist(outlet);
        em.flush();
        return outlet.getOutletId();
    }
    
    @Override
    public List<Outlet> retrieveAllOutlets() {
       Query query = em.createQuery("SELECT o FROM Outlet o");
       return query.getResultList();
    }
    
   
    @Override
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException {
        try {
            return em.find(Outlet.class, outletId);
        }
        catch(NoResultException ex) {
            throw new OutletNotFoundException("Outlet not found!");
        }
    }
    
    
    
}
