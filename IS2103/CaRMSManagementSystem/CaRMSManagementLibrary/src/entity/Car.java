/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.CarStatusEnum;

/**
 *
 * @author 60540
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    
    @Column(nullable = false, length = 20, unique = true)
    private String carLicensePlateNum;
    
    @Column(nullable = false, length = 20, unique = false)
    private String colour;
    
    @Column(nullable = false)
    private Boolean disabled;
    
    @Enumerated(EnumType.STRING)
    private CarStatusEnum carStatus;
   
    @ManyToOne // ask prof the safer thing to do
    private Outlet latestOutlet;
    
    /* the damien guy has private Model model, private Reservation rentalReservation as
    another two attributes in car class, bc we didnt have it in our logic model for now, i havent
    add it in.
    */
    
    @OneToOne(optional = true)//Supposed to have a mapped by, check after changing reservation
    private Reservation currentReservation;
    
    //Uni-directional for now 
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarModel carModel;
    @ManyToOne
    private Outlet outlet;

    public Car() {
        this.disabled = false;
        this.carStatus = CarStatusEnum.AVAILABLE;
    }

    //ID is defined by defualt and the reservation is null by default
    public Car(String carLicensePlateNum, String colour) {
        this();
        this.carLicensePlateNum = carLicensePlateNum;
        this.colour = colour;
    }
    
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + carId + " ]";
    }

    /**
     * @return the carLicensePlateNum
     */
    public String getCarLicensePlateNum() {
        return carLicensePlateNum;
    }

    /**
     * @param carLicensePlateNum the carLicensePlateNum to set
     */
    public void setCarLicensePlateNum(String carLicensePlateNum) {
        this.carLicensePlateNum = carLicensePlateNum;
    }

    /**
     * @return the colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * @param colour the colour to set
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * @return the disabled
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the latestOutlet
     */
    public Outlet getLatestOutlet() {
        return latestOutlet;
    }

    /**
     * @param latestOutlet the latestOutlet to set
     */
    public void setLatestOutlet(Outlet latestOutlet) {
        this.latestOutlet = latestOutlet;
    }

    /**
     * @return the carStatus
     */
    public CarStatusEnum getCarStatus() {
        return carStatus;
    }

    /**
     * @param carStatus the carStatus to set
     */
    public void setCarStatus(CarStatusEnum carStatus) {
        this.carStatus = carStatus;
    }

    /**
     * @return the currentReservation
     */
    public Reservation getCurrentReservation() {
        return currentReservation;
    }

    /**
     * @param currentReservation the currentReservation to set
     */
    public void setCurrentReservation(Reservation currentReservation) {
        this.currentReservation = currentReservation;
    }

    /**
     * @return the carModel
     */
    public CarModel getCarModel() {
        return carModel;
    }

    /**
     * @param carModel the carModel to set
     */
    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }
    
}
