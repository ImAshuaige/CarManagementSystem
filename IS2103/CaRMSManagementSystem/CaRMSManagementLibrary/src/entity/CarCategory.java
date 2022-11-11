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
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 60540
 */
@Entity
public class CarCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    
    @Column(nullable = false, length = 50, unique = true)
    private String carCategoryName;
    
    
    @OneToMany(mappedBy = "belongsCategory")
    private List<CarModel> modelList = new ArrayList<>();
    
    @OneToMany(mappedBy = "carCategory")
    private List<RentalRate> rateList = new ArrayList<>();

    public CarCategory() {
    }

    public CarCategory(String carCategoryName) {
        this.carCategoryName = carCategoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryId fields are not set
        if (!(object instanceof CarCategory)) {
            return false;
        }
        CarCategory other = (CarCategory) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarCategory[ id=" + categoryId + " ]";
    }

    /**
     * @return the carCategoryName
     */
    public String getCarCategoryName() {
        return carCategoryName;
    }

    /**
     * @param carCategoryName the carCategoryName to set
     */
    public void setCarCategoryName(String carCategoryName) {
        this.carCategoryName = carCategoryName;
    }

    /**
     * @return the modelList
     */
    @XmlTransient
    public List<CarModel> getModelList() {
        return modelList;
    }

    /**
     * @param modelList the modelList to set
     */
    
    public void setModelList(List<CarModel> modelList) {
        this.modelList = modelList;
    }

    /**
     * @return the rateList
     */
    //@XmlTransient
    public List<RentalRate> getRateList() {
        return rateList;
    }

    /**
     * @param rateList the rateList to set
     */
    public void setRateList(List<RentalRate> rateList) {
        this.rateList = rateList;
    }
    
    public void addRentalRate(RentalRate rentalRate) {
        if (!this.rateList.contains(rentalRate)) {
            this.rateList.add(rentalRate);
        }
    }
    
}
