/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.PartnerNameExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMSManagementSystem-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PartnerSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewPartner(Partner newPartner) throws PartnerNameExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(newPartner);

            if (constraintViolations.isEmpty()) {
                em.persist(newPartner);
                em.flush();
                return newPartner.getPartnerId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new PartnerNameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Partner>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public Partner retrievePartnerByPartnerId(Long partnerId) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);

        if (partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Partner ID " + partnerId + " does not exist!");
        }
    }

    @Override
    public Long partnerLogin(String partnerName, String password) throws InvalidLoginException, PartnerNotFoundException {
        try {
            Query query = em.createQuery("SELECT p FROM Partner p WHERE p.partnerName = :inPartnerName");
            query.setParameter("inPartnerName", partnerName);
            Partner partner = (Partner) query.getSingleResult();

            if (partner.getPartnerPassword().equals(password)) {
                return partner.getPartnerId();
            } else {
                throw new InvalidLoginException("[Incorrect Password] Invalid Login! Please Try Again!");
            }
        } catch (NoResultException ex) {
            throw new PartnerNotFoundException("[Partner Not Found] Invalid Login! Please Try Again!");
        }
    }

}
