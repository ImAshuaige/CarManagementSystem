/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatch;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author 60540
 */
@Local
public interface TransitDriverDispatchSessionBeanLocal {
    public List<TransitDriverDispatch> retrieveTransitDriverDispatchByOutletId(Date date, Long outletId);
}
