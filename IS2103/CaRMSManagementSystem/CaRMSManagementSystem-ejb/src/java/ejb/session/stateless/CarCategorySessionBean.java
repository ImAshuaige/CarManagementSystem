/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;
import util.exception.NoAvailableRentalRateException;


/**
 *
 * @author 60540
 */
@Stateless
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

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
    
        

    @Override
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
    
    
    @Override
    public BigDecimal calculateRentalFee(Long carCategoryId, Date pickUpDateTime, Date returnDateTime) throws NoAvailableRentalRateException {
        BigDecimal totalRentalFee = new BigDecimal(0);//change to 5 see if it changes 
         System.out.println("CAN I SEE THIS ONE CAR RATE");
        returnDateTime.setHours(pickUpDateTime.getHours());
        returnDateTime.setMinutes(pickUpDateTime.getMinutes());

        try {
            LocalDateTime pickUpTemporal = pickUpDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            System.out.println("pickUp" + pickUpTemporal.toString());
            LocalDateTime returnTemporal = returnDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            System.out.println("return" + pickUpTemporal.toString());
            Long rentingDays = ChronoUnit.DAYS.between(pickUpTemporal, returnTemporal);
            if (rentingDays == 0) {
                rentingDays = (long)1;//at least 1 day
            }
            System.out.println("rentingDays " + rentingDays);
            GregorianCalendar transitCalendar = new GregorianCalendar(
                    pickUpDateTime.getYear() + 1900,
                    pickUpDateTime.getMonth(),
                    pickUpDateTime.getDate(),
                    pickUpDateTime.getHours(),
                    pickUpDateTime.getMinutes(),
                    pickUpDateTime.getSeconds());
            //FOR each day of the reservation, we calculate the rental rate for it 
            for (long i = 0; i < rentingDays; i++) {
                RentalRate lowestRentalRate = rentalRateSessionBeanLocal.retrieveLowestRentalRate(carCategoryId, transitCalendar.getTime());
                transitCalendar.add(Calendar.DATE, 1);
                BigDecimal dailyLowestRentalRate = lowestRentalRate.getDailyRate();
                totalRentalFee = totalRentalFee.add(dailyLowestRentalRate);
                System.out.println("Rental Fee is " + dailyLowestRentalRate + " " + transitCalendar.toString());
            }
            return totalRentalFee;
        } catch (NoAvailableRentalRateException ex) {
            throw new NoAvailableRentalRateException();
        }
    }

    
}
