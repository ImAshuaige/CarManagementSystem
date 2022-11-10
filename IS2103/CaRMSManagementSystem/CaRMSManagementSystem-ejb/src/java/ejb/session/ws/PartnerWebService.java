/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Outlet;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.enterprise.inject.Model;
import javax.xml.bind.annotation.XmlTransient;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.NoAvailableRentalRateException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 60540
 */
@WebService(serviceName = "PartnerWebService")
@Stateless
public class PartnerWebService {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

        //Checking the customerSessionBean to see if this method exists
    @WebMethod(operationName = "createNewPartnerCustomer")
    public Long createNewPartnerCustomer(@WebParam(name = "partnerId") Long partnerId, @WebParam(name = "newCustomer") Customer newCustomer) throws PartnerNotFoundException, UnknownPersistenceException, InputDataValidationException {
        return customerSessionBeanLocal.createNewPartnerCustomer(partnerId, newCustomer);
    }
    
    @WebMethod(operationName = "createNewPartnerRentalReservation")
    public Long createNewPartnerRentalReservation(@WebParam Long carCategoryId, @WebParam Long partnerId, @WebParam Long modelId, @WebParam Long customerId,
            @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Reservation newReservation)
            throws OutletNotFoundException, CustomerNotFoundException, InputDataValidationException, UnknownPersistenceException,
            CarCategoryNotFoundException, CarModelNotFoundException, PartnerNotFoundException {
        return reservationSessionBeanLocal.createNewPartnerRentalReservation(carCategoryId, partnerId, modelId, customerId, pickupOutletId, returnOutletId, newReservation);
    }
    
    @WebMethod(operationName = "partnerLogin")
    public Long partnerLogin(@WebParam(name = "partnerName") String partnerName, @WebParam(name = "partnerPassword") String password) throws InvalidLoginException, PartnerNotFoundException {
        return partnerSessionBeanLocal.partnerLogin(partnerName, password);
    }
    
    
    @WebMethod(operationName = "retrieveCarCategoryByCarCategoryId")
    public CarCategory retrieveCarCategoryByCarCategoryId(@WebParam(name = "carCategoryId") Long carCategoryId) throws CarCategoryNotFoundException {
        return carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
    }
    
   
    @WebMethod(operationName = "retrieveOutletById")
    public Outlet retrieveOutletById(@WebParam(name = "outletId") Long outletId) throws OutletNotFoundException {
        return outletSessionBeanLocal.retrieveOutletById(outletId);
    }

    
    @WebMethod(operationName = "retrieveCarModelById")
    public CarModel retrieveCarModelById(@WebParam(name = "modelId") Long modelId) throws CarModelNotFoundException {
        return carModelSessionBeanLocal.retrieveCarModelById(modelId);
    }

    
    @WebMethod(operationName = "calculateRentalFee")
    public BigDecimal calculateRentalFee(@WebParam Long carCategoryId, @WebParam Date pickUpDateTime, @WebParam Date returnDateTime) throws CarCategoryNotFoundException, NoAvailableRentalRateException {
        return carCategorySessionBeanLocal.calculateRentalFee(carCategoryId, pickUpDateTime, returnDateTime);
    }

    
    @WebMethod(operationName = "searchCarByCategory")
    public Boolean searchCarByCategory(@WebParam Date pickUpDateTime, @WebParam Date returnDateTime, @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long carCategoryId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException {
        return reservationSessionBeanLocal.searchCarByCategory(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, carCategoryId);
    }

   
    @WebMethod(operationName = "searchCarByModel")
    public Boolean searchCarByModel(@WebParam Date pickUpDateTime, @WebParam Date returnDateTime, @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long modelId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException, CarModelNotFoundException {
        return reservationSessionBeanLocal.searchCarByModel(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, modelId);
    }

    
    @WebMethod(operationName = "retrieveAllCarModels")
    public List<CarModel> retrieveAllCarModels() {
        return carModelSessionBeanLocal.retrieveAllCarModels();
    }

    
    @WebMethod(operationName = "retrieveAllCarCategory")
    public List<CarCategory> retrieveAllCarCategory() {
        return carCategorySessionBeanLocal.retrieveAllCarCategory();
    }

    
    @WebMethod(operationName = "retrieveAllOutlets")
    public List<Outlet> retrieveAllOutlets() {
        return outletSessionBeanLocal.retrieveAllOutlets();
    }
   

}
