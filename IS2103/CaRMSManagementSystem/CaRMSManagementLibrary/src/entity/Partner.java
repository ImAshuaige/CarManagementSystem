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
import javax.persistence.OneToMany;

/**
 *
 * @author 60540
 */
@Entity
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(nullable = false, length = 40, unique = true) 
    private String partnerName;
    @Column(nullable = false, length = 16, unique = true) 
    private String partnerPassword;
    
    @OneToMany(mappedBy = "reservationPartner")
    private List<Reservation> reservationList;
    
    //List of customer?
    @OneToMany(mappedBy = "partner") //Keep it for now and change to ManyToMany later, if needed.
    private List<Customer> partnerCustomerList;

    public Partner() {
        this.reservationList = new ArrayList<>();
        this.partnerCustomerList = new ArrayList<>();
    }

    public Partner(String partnerName, String partnerPassword) {
        this();
        this.partnerName = partnerName;
        this.partnerPassword = partnerPassword;
    }
    

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + partnerId + " ]";
    }

    /**
     * @return the partnerName
     */
    public String getPartnerName() {
        return partnerName;
    }

    /**
     * @param partnerName the partnerName to set
     */
    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    /**
     * @return the partnerPassword
     */
    public String getPartnerPassword() {
        return partnerPassword;
    }

    /**
     * @param partnerPassword the partnerPassword to set
     */
    public void setPartnerPassword(String partnerPassword) {
        this.partnerPassword = partnerPassword;
    }

    /**
     * @return the reservationList
     */
    public List<Reservation> getReservationList() {
        return reservationList;
    }

    /**
     * @param reservationList the reservationList to set
     */
    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    /**
     * @return the partnerCustomerList
     */
    public List<Customer> getPartnerCustomerList() {
        return partnerCustomerList;
    }

    /**
     * @param partnerCustomerList the partnerCustomerList to set
     */
    public void setPartnerCustomerList(List<Customer> partnerCustomerList) {
        this.partnerCustomerList = partnerCustomerList;
    }
    
}
