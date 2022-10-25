/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author 60540
 */
@Remote
public interface RentalRateSessionBeanRemote {
    
    public Long createNewRentalRate(RentalRate rentalRate);
    
    public List<RentalRate> retrieveAllRentalRates();
    
}
