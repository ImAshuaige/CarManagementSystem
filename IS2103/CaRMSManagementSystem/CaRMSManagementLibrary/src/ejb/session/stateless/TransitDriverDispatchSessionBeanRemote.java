/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatch;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.TransitDriverDispatchNotFoundException;

/**
 *
 * @author 60540
 */
@Remote
public interface TransitDriverDispatchSessionBeanRemote {

    public List<TransitDriverDispatch> retrieveTransitDriverDispatchByOutletId(Date date, Long outletId);

    public long createNewTranspatchDriverRecord(Long destinationOutletId, Long reservationId, Date transitStartDate) throws OutletNotFoundException, ReservationNotFoundException;

    public void updateTransitAsCompleted(Long transitDriverDispatchId) throws TransitDriverDispatchNotFoundException;

    public TransitDriverDispatch retrieveTransitDriverDispatchByTransitDriverDispatchId(Long transitDriverDispatchId) throws TransitDriverDispatchNotFoundException;

}
