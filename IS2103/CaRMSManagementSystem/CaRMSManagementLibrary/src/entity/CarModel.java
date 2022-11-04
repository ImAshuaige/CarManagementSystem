/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author 60540
 */
@Entity
public class CarModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;
    
    @Column(nullable = false, length = 30, unique = false)
    private String make;
    
    @Column(nullable = false, length = 30, unique = false)
    private String model;
    
    @Column(nullable = false)
    private Boolean disabled;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private CarCategory belongsCategory;
    
    @OneToMany(mappedBy = "carModel")
    private List<Car> listOfCars;

    public CarModel() {
        this.disabled = false;
    }

    public CarModel(String make, String model, CarCategory belongsCategory) {
        this();
        this.make = make;
        this.model = model;
        this.belongsCategory = belongsCategory;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelId != null ? modelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the modelId fields are not set
        if (!(object instanceof CarModel)) {
            return false;
        }
        CarModel other = (CarModel) object;
        if ((this.modelId == null && other.modelId != null) || (this.modelId != null && !this.modelId.equals(other.modelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarModel[ id=" + modelId + " ]";
    }

    /**
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * @param make the make to set
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
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
     * @return the belongsCategory
     */
    public CarCategory getBelongsCategory() {
        return belongsCategory;
    }

    /**
     * @param belongsCategory the belongsCategory to set
     */
    public void setBelongsCategory(CarCategory belongsCategory) {
        this.belongsCategory = belongsCategory;
    }
    
    public void addCar(Car car) {
        if (!this.listOfCars.contains(car)) {
            this.getListOfCars().add(car);
        }
    }

    /**
     * @return the listOfCars
     */
    public List<Car> getListOfCars() {
        return listOfCars;
    }

    /**
     * @param listOfCars the listOfCars to set
     */
    public void setListOfCars(List<Car> listOfCars) {
        this.listOfCars = listOfCars;
    }
    
}
