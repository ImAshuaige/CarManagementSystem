/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.enumeration.EmployeeRoleEnum;

/**
 *
 * @author Mehak
 */
@Entity
public class Employee implements Serializable {
    // we didn't define the foreign key and we're unsure about our java classes(map class?)
    // by now we only create the attribute without relationship and the constructors
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    
    @Column(nullable = false, length = 40)
    private String employeeFirstName;
    
    @Column(nullable = false, length = 40)
    private String employeeLastName;
    
    @Column(nullable = false, length = 40, unique = true)
    private String employeeUserName;
    
    @Column(nullable = false, length = 16, unique = false)
    private String employeePassword; 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRoleEnum employeeRole;
  
    @OneToMany(mappedBy = "dispatchDriver")
    private List<TransitDriverDispatch> transitDriverDispatchRecord;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Outlet outlet;  
    
    
    
    public Employee() {
        this.transitDriverDispatchRecord = new ArrayList<> ();
    }

    public Employee(String employeeFirstName, String employeeLastName, String employeeUserName, String employeePassword, EmployeeRoleEnum employeeRole) {
        this.transitDriverDispatchRecord = new ArrayList<> ();
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeeUserName = employeeUserName;
        this.employeePassword = employeePassword;
        this.employeeRole = employeeRole;
        this.outlet = new Outlet();
    }
    
    

    
    
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }

    /**
     * @return the employeeFirstName
     */
    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    /**
     * @param employeeFirstName the employeeFirstName to set
     */
    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    /**
     * @return the employeeLastName
     */
    public String getEmployeeLastName() {
        return employeeLastName;
    }

    /**
     * @param employeeLastName the employeeLastName to set
     */
    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    /**
     * @return the employeeUserName
     */
    public String getEmployeeUserName() {
        return employeeUserName;
    }

    /**
     * @param employeeUserName the employeeUserName to set
     */
    public void setEmployeeUserName(String employeeUserName) {
        this.employeeUserName = employeeUserName;
    }

    /**
     * @return the employeePassword
     */
    public String getEmployeePassword() {
        return employeePassword;
    }

    /**
     * @param employeePassword the employeePassword to set
     */
    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    /**
     * @return the employeeRole
     */
    public EmployeeRoleEnum getEmployeeRole() {
        return employeeRole;
    }

    /**
     * @param employeeRole the employeeRole to set
     */
    public void setEmployeeRole(EmployeeRoleEnum employeeRole) {
        this.employeeRole = employeeRole;
    }

    /**
     * @return the transitDriverDispatchRecord
     */
    public List<TransitDriverDispatch> getTransitDriverDispatchRecord() {
        return transitDriverDispatchRecord;
    }

    /**
     * @param transitDriverDispatchRecord the transitDriverDispatchRecord to set
     */
    public void setTransitDriverDispatchRecord(List<TransitDriverDispatch> transitDriverDispatchRecord) {
        this.transitDriverDispatchRecord = transitDriverDispatchRecord;
    }

    /**
     * @return the outlet
     */
    public Outlet getOutlet() {
        return outlet;
    }

    /**
     * @param outlet the outlet to set
     */
    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }
    
}
