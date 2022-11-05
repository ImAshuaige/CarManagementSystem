/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author 60540
 */
@Entity
public class TransitDriverDispatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverDispatchId;
    
    @Column(nullable = true) 
    private boolean isCompleted;
    
    @Temporal(TemporalType.TIMESTAMP) 
    @Column(nullable = true) 
    private Date transitDate;
    
    //Relationships
    //@ManyToOne(optional = true)//Are we sure about this?
    //@JoinColumn(nullable = true) // //Are we sure about this?
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)//Double confirm with prof
    private Employee dispatchDriver;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false) 
    private Outlet destinationOutlet;
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false) 
    private Reservation rentalReservation;
    
    //Do we need transit time?

    public TransitDriverDispatch() {
        this.isCompleted = false;
    }

    public TransitDriverDispatch(Date transitDate) {
        this();
        this.transitDate = transitDate;
    }
    
    
    
    
    public Long getTransitDriverDispatchId() {
        return transitDriverDispatchId;
    }

    public void setTransitDriverDispatchId(Long transitDriverDispatchId) {
        this.transitDriverDispatchId = transitDriverDispatchId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchId != null ? transitDriverDispatchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverDispatchId fields are not set
        if (!(object instanceof TransitDriverDispatch)) {
            return false;
        }
        TransitDriverDispatch other = (TransitDriverDispatch) object;
        if ((this.transitDriverDispatchId == null && other.transitDriverDispatchId != null) || (this.transitDriverDispatchId != null && !this.transitDriverDispatchId.equals(other.transitDriverDispatchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatch[ id=" + transitDriverDispatchId + " ]";
    }

    /**
     * @return the isCompleted
     */
    public boolean isIsCompleted() {
        return isCompleted;
    }

    /**
     * @param isCompleted the isCompleted to set
     */
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * @return the transitDate
     */
    public Date getTransitDate() {
        return transitDate;
    }

    /**
     * @param transitDate the transitDate to set
     */
    public void setTransitDate(Date transitDate) {
        this.transitDate = transitDate;
    }

    /**
     * @return the dispatchDriver
     */
    public Employee getDispatchDriver() {
        return dispatchDriver;
    }

    /**
     * @param dispatchDriver the dispatchDriver to set
     */
    public void setDispatchDriver(Employee dispatchDriver) {
        this.dispatchDriver = dispatchDriver;
    }

    /**
     * @return the destinationOutlet
     */
    public Outlet getDestinationOutlet() {
        return destinationOutlet;
    }

    /**
     * @param destinationOutlet the destinationOutlet to set
     */
    public void setDestinationOutlet(Outlet destinationOutlet) {
        this.destinationOutlet = destinationOutlet;
    }

    /**
     * @return the rentalReservation
     */
    public Reservation getRentalReservation() {
        return rentalReservation;
    }

    /**
     * @param rentalReservation the rentalReservation to set
     */
    public void setRentalReservation(Reservation rentalReservation) {
        this.rentalReservation = rentalReservation;
    }
    
}
