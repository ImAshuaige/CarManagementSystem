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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.RentalRateType;

/**
 *
 * @author 60540
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    
    private RentalRateType rateType;

    @Column(nullable = false, length = 40) //Length constraint?
    private String rentalName;

    @Column(nullable = false)
    @DecimalMin("0.00")
    //Digits constraint?
    private BigDecimal dailyRate;

    @Column(nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date rateStartDate;

    @Column(nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date rateEndDate;

    @Column(nullable = false)
    private boolean isApplied;

    @Column(nullable = false)
    private boolean isDisabled;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    public RentalRate() {
        this.isApplied = false;
        this.isDisabled = false;
    }

    public RentalRate(String rentalName, BigDecimal dailyRate, Date rateStartDate, Date rateEndDate, CarCategory carCategory, RentalRateType rateType) {
        this.isApplied = false;
        this.isDisabled = false;
        this.rentalName = rentalName;
        this.dailyRate = dailyRate;
        this.rateStartDate = rateStartDate;
        this.rateEndDate = rateEndDate;
        this.carCategory = carCategory;
        this.rateType = rateType;
    }

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRate[ id=" + rentalRateId + " ]";
    }

    /**
     * @return the dailyRate
     */
    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    /**
     * @param dailyRate the dailyRate to set
     */
    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    /**
     * @return the rateStartDate
     */
    public Date getRateStartDate() {
        return rateStartDate;
    }

    /**
     * @param rateStartDate the rateStartDate to set
     */
    public void setRateStartDate(Date rateStartDate) {
        this.rateStartDate = rateStartDate;
    }

    /**
     * @return the rateEndDate
     */
    public Date getRateEndDate() {
        return rateEndDate;
    }

    /**
     * @param rateEndDate the rateEndDate to set
     */
    public void setRateEndDate(Date rateEndDate) {
        this.rateEndDate = rateEndDate;
    }

    /**
     * @return the isApplied
     */
    public boolean isIsApplied() {
        return isApplied;
    }

    /**
     * @param isApplied the isApplied to set
     */
    public void setIsApplied(boolean isApplied) {
        this.isApplied = isApplied;
    }

    /**
     * @return the carCategory
     */
    @XmlTransient
    public CarCategory getCarCategory() {
        return carCategory;
    }

    /**
     * @param carCategory the carCategory to set
     */
    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    /**
     * @return the rentalName
     */
    public String getRentalName() {
        return rentalName;
    }

    /**
     * @param rentalName the rentalName to set
     */
    public void setRentalName(String rentalName) {
        this.rentalName = rentalName;
    }

    /**
     * @return the isDisabled
     */
    public boolean isIsDisabled() {
        return isDisabled;
    }

    /**
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    /**
     * @return the rateType
     */
    public RentalRateType getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(RentalRateType rateType) {
        this.rateType = rateType;
    }

}
