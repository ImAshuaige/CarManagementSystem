/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author 60540
 */
@Entity
public class Outlet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    
    @Column(nullable = false, length = 40, unique = true) 
    private String outletName;
    
    @Column(nullable = false, length = 500, unique = false)
    private String outletAddress;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date outletOpeningTime; 
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date outletClosingTime;
    
    @OneToMany(mappedBy = "latestOutlet")
    private List<Car> carsList;
    
    @OneToMany(mappedBy = "outlet")
    private List<Employee> employeesList;
    
    @OneToMany(mappedBy = "destinationOutlet")
    private List<TransitDriverDispatch> transitDriverDispatchList;
    
    @OneToMany(mappedBy = "outlet")
    private List<Car> listOfCars;
    
    /* Since outlet has a relationship with reservation, should we include these codes as well
    @OneToMany(mappedBy = "pickupLocation")
    private List<Reservation> pickReservation = new ArrayList<>();
    
    @OneToMany(mappedBy = "returnLocation")
    private List<Reservation> returnReservation = new ArrayList<>();
    */

    public Outlet() {
        this.carsList = new ArrayList<> ();
        this.employeesList = new ArrayList<> ();
        this.transitDriverDispatchList = new ArrayList<> ();
    }

    public Outlet(String outletName, String outletAddress, Date outletOpeningTime, Date outletClosingTime) {
        this.outletName = outletName;
        this.outletAddress = outletAddress;
        this.outletOpeningTime = outletOpeningTime;
        this.outletClosingTime = outletClosingTime;
    }
    
    
    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + outletId + " ]";
    }

    /**
     * @return the outletName
     */
    public String getOutletName() {
        return outletName;
    }

    /**
     * @param outletName the outletName to set
     */
    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    /**
     * @return the outletAddress
     */
    public String getOutletAddress() {
        return outletAddress;
    }

    /**
     * @param outletAddress the outletAddress to set
     */
    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    /**
     * @return the outletOpeningTime
     */
    public Date getOutletOpeningTime() {
        return outletOpeningTime;
    }

    /**
     * @param outletOpeningTime the outletOpeningTime to set
     */
    public void setOutletOpeningTime(Date outletOpeningTime) {
        this.outletOpeningTime = outletOpeningTime;
    }

    /**
     * @return the outletClosingTime
     */
    public Date getOutletClosingTime() {
        return outletClosingTime;
    }

    /**
     * @param outletClosingTime the outletClosingTime to set
     */
    public void setOutletClosingTime(Date outletClosingTime) {
        this.outletClosingTime = outletClosingTime;
    }

    /**
     * @return the carsList
     */
    public List<Car> getCarsList() {
        return carsList;
    }

    /**
     * @param carsList the carsList to set
     */
    public void setCarsList(List<Car> carsList) {
        this.carsList = carsList;
    }

    /**
     * @return the employeesList
     */
    public List<Employee> getEmployeesList() {
        return employeesList;
    }

    /**
     * @param employeesList the employeesList to set
     */
    public void setEmployeesList(List<Employee> employeesList) {
        this.employeesList = employeesList;
    }

    /**
     * @return the transitDriverDispatchList
     */
    public List<TransitDriverDispatch> getTransitDriverDispatchList() {
        return transitDriverDispatchList;
    }

    /**
     * @param transitDriverDispatchList the transitDriverDispatchList to set
     */
    public void setTransitDriverDispatchList(List<TransitDriverDispatch> transitDriverDispatchList) {
        this.transitDriverDispatchList = transitDriverDispatchList;
    }

    public void addCar(Car car) {
        if (!this.carsList.contains(car)) {
            this.carsList.add(car);
        }
    }
    
    
    public void removeCar(Car car) {
        if (this.carsList.contains(car)) {
            this.carsList.remove(car);
        }
    }
    
}
