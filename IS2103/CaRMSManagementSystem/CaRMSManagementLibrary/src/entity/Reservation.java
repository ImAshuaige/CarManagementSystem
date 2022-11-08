/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.DecimalMin;
import util.enumeration.ReservationStatusEnum;

/**
 *
 * @author 60540
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalReservationId;
    
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP) 
    @Column(nullable = false)
    private Date reservationStartDate;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP) 
    @Column(nullable = false)
    private Date reservationEndDate;
    
    @Enumerated
    private ReservationStatusEnum reservationStatus;
    
    @Column(nullable = false)//, precision = 11, scale = 2). Random precision. We should fix it later
    @DecimalMin("0.00")
    //Digits Constraint?
    private BigDecimal reservationPrice;
   
    @Column(nullable = false, length = 32)
    private String creditCardNumber;
    
    @Column(nullable = false)
    private Boolean paid;
    
    @Column(nullable = false)
    private Boolean isCancelled;
    
    @Column(nullable = false)
    private Boolean isCompleted;
    
    @Column(nullable = false)
    private Boolean isPicked;
    
    //Relationships
    //Not sure about CarCategory and CarModel relationship. There is conflict between the reference codes
    //@OneToOne(mappedBy = "rentalReservation")
    //@OneToOne
    
    @OneToOne(mappedBy = "rentalReservation")
    private TransitDriverDispatch reservationDispatchDriver;
    
    @ManyToOne //Optional setting and fetching (eager/lazy)
    @JoinColumn(nullable = true)
    private Partner reservationPartner;
    
    //@OneToOne(mappedBy = "currentReservation")
    @OneToOne(optional = true)
    //@JoinColumn(optional = true/*nullable = false*/)
    private Car car;
    
    @ManyToOne(optional = true)
    //@JoinColumn(nullable = false)
    private CarCategory reservedCarCategory;
    
    @ManyToOne(optional = true)
    //@JoinColumn(nullable = false)
    private CarModel reservedCarModel;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet reservationPickUpOutlet;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet reservationReturnOutlet;
    
    

    public Reservation() {
        this.isCancelled = false;
        this.isCompleted = false;
        this.isPicked = false;
    }

    public Reservation(Date reservationStartDate, Date reservationEndDate, ReservationStatusEnum reservationStatus, BigDecimal reservationPrice, TransitDriverDispatch reservationDispatchDriver, Partner reservationPartner, Car car, CarCategory reservedCarCategory, CarModel reservedCarModel, Customer customer, Outlet reservationPickUpOutlet, Outlet reservationReturnOutlet) {
        this();
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.reservationStatus = reservationStatus;
        this.reservationPrice = reservationPrice;
        this.reservationDispatchDriver = reservationDispatchDriver; //Not too sure if this should be in the constructor
        this.reservationPartner = reservationPartner;
        this.car = car;
        this.reservedCarCategory = reservedCarCategory;
        this.reservedCarModel = reservedCarModel;
        this.customer = customer;
        this.reservationPickUpOutlet = reservationPickUpOutlet;
        this.reservationReturnOutlet = reservationReturnOutlet;
    }

    

    public Long getRentalReservationId() {
        return rentalReservationId;
    }

    public void setRentalReservationId(Long rentalReservationId) {
        this.rentalReservationId = rentalReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalReservationId != null ? rentalReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalReservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.rentalReservationId == null && other.rentalReservationId != null) || (this.rentalReservationId != null && !this.rentalReservationId.equals(other.rentalReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + rentalReservationId + " ]";
    }

    /**
     * @return the reservationStartDate
     */
    public Date getReservationStartDate() {
        return reservationStartDate;
    }

    /**
     * @param reservationStartDate the reservationStartDate to set
     */
    public void setReservationStartDate(Date reservationStartDate) {
        this.reservationStartDate = reservationStartDate;
    }

    /**
     * @return the reservationEndDate
     */
    public Date getReservationEndDate() {
        return reservationEndDate;
    }

    /**
     * @param reservationEndDate the reservationEndDate to set
     */
    public void setReservationEndDate(Date reservationEndDate) {
        this.reservationEndDate = reservationEndDate;
    }

    /**
     * @return the reservationStatus
     */
    public ReservationStatusEnum getReservationStatus() {
        return reservationStatus;
    }

    /**
     * @param reservationStatus the reservationStatus to set
     */
    public void setReservationStatus(ReservationStatusEnum reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    /**
     * @return the reservationPrice
     */
    public BigDecimal getReservationPrice() {
        return reservationPrice;
    }

    /**
     * @param reservationPrice the reservationPrice to set
     */
    public void setReservationPrice(BigDecimal reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    /**
     * @return the reservationDispatchDriver
     */
    public TransitDriverDispatch getReservationDispatchDriver() {
        return reservationDispatchDriver;
    }

    /**
     * @param reservationDispatchDriver the reservationDispatchDriver to set
     */
    public void setReservationDispatchDriver(TransitDriverDispatch reservationDispatchDriver) {
        this.reservationDispatchDriver = reservationDispatchDriver;
    }

    /**
     * @return the reservationPartner
     */
    public Partner getReservationPartner() {
        return reservationPartner;
    }

    /**
     * @param reservationPartner the reservationPartner to set
     */
    public void setReservationPartner(Partner reservationPartner) {
        this.reservationPartner = reservationPartner;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * @return the reservedCarCategory
     */
    public CarCategory getReservedCarCategory() {
        return reservedCarCategory;
    }

    /**
     * @param reservedCarCategory the reservedCarCategory to set
     */
    public void setReservedCarCategory(CarCategory reservedCarCategory) {
        this.reservedCarCategory = reservedCarCategory;
    }

    /**
     * @return the reservedCarModel
     */
    public CarModel getReservedCarModel() {
        return reservedCarModel;
    }

    /**
     * @param reservedCarModel the reservedCarModel to set
     */
    public void setReservedCarModel(CarModel reservedCarModel) {
        this.reservedCarModel = reservedCarModel;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the reservationPickUpOutlet
     */
    public Outlet getReservationPickUpOutlet() {
        return reservationPickUpOutlet;
    }

    /**
     * @param reservationPickUpOutlet the reservationPickUpOutlet to set
     */
    public void setReservationPickUpOutlet(Outlet reservationPickUpOutlet) {
        this.reservationPickUpOutlet = reservationPickUpOutlet;
    }

    /**
     * @return the reservationReturnOutlet
     */
    public Outlet getReservationReturnOutlet() {
        return reservationReturnOutlet;
    }

    /**
     * @param reservationReturnOutlet the reservationReturnOutlet to set
     */
    public void setReservationReturnOutlet(Outlet reservationReturnOutlet) {
        this.reservationReturnOutlet = reservationReturnOutlet;
    }

    /**
     * @return the creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @param creditCardNumber the creditCardNumber to set
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * @return the paid
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    /**
     * @return the isCancelled
     */
    public Boolean getIsCancelled() {
        return isCancelled;
    }

    /**
     * @param isCancelled the isCancelled to set
     */
    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    /**
     * @return the isCompleted
     */
    public Boolean getIsCompleted() {
        return isCompleted;
    }

    /**
     * @param isCompleted the isCompleted to set
     */
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * @return the isPicked
     */
    public Boolean getIsPicked() {
        return isPicked;
    }

    /**
     * @param isPicked the isPicked to set
     */
    public void setIsPicked(Boolean isPicked) {
        this.isPicked = isPicked;
    }


    
}
