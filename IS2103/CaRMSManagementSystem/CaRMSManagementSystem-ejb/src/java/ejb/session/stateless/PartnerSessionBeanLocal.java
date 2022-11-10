/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.PartnerNameExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Local
public interface PartnerSessionBeanLocal {
    public Long createNewPartner(Partner newPartner) throws PartnerNameExistException, UnknownPersistenceException, InputDataValidationException;
    
    public Long partnerLogin(String partnerName, String password) throws InvalidLoginException, PartnerNotFoundException;

    public Partner retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException;
}
