/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import javax.ejb.Local;
import util.exception.OutletNotFoundException;

/**
 *
 * @author 60540
 */
@Local
public interface OutletSessionBeanLocal {

    public Long createNewOutlet(Outlet outlet);

    public List<Outlet> retrieveAllOutlets();
    
     public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException;
    
}
