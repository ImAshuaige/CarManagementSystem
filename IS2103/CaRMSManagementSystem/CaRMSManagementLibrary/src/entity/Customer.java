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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author 60540
 */
@Entity
public class Customer implements Serializable {

    /**
     * @return the customerReservation
     */
    public List<Reservation> getCustomerReservation() {
        return customerReservation;
    }

    /**
     * @param customerReservation the customerReservation to set
     */
    public void setCustomerReservation(List<Reservation> customerReservation) {
        this.customerReservation = customerReservation;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    
    @Column(nullable = false, length = 40)
    private String customerFirstName;
    
    @Column(nullable = false, length = 40)
    private String customerLastName;
    
    @Column(nullable = false, length = 250, unique = true)
    private String customerEmail;
    
    @Column(nullable = false, length = 16, unique = false)
    private String customerPassword;

    
    @OneToMany(mappedBy = "customer")
    private List<Reservation> customerReservation;
    @ManyToOne
    private Partner partner;
    /*
    @ManyToMany(mappedBy = "partnerCustomerList")
    private List<Partner> partners;
    */
    
    public Customer() {
        this.customerReservation = new ArrayList<> ();
    }

    public Customer(String customerFirstName, String customerLastName, String customerEmail,String customerPassword) {
        this.customerReservation = new ArrayList<> ();
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    /**
     * @return the customerFirstName
     */
    public String getCustomerFirstName() {
        return customerFirstName;
    }

    /**
     * @param customerFirstName the customerFirstName to set
     */
    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    /**
     * @return the customerLastName
     */
    public String getCustomerLastName() {
        return customerLastName;
    }

    /**
     * @param customerLastName the customerLastName to set
     */
    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    /**
     * @return the customerEmail
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * @param customerEmail the customerEmail to set
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * @return the customerPassword
     */
    public String getCustomerPassword() {
        return customerPassword;
    }

    /**
     * @param customerPassword the customerPassword to set
     */
    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }
    
    public void addReservation(Reservation reservation) {
        if (!this.customerReservation.contains(reservation)) {
            this.getCustomerReservation().add(reservation);
        }
    }

    
}
