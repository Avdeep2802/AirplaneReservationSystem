package hello.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.sql.DataSource;


@Entity
@Table(name = "Plane")
public class Plane  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column(name="model")
    private String model;

    @Column(name="manufacturer")
    private String manufacturer;

    @Column(name="capacity")
    private int capacity;

    @Column(name="yearOfManufacture")
    private int yearOfManufacture;

     public Plane(String model,String manufacturer,int capacity,int yearOfManufacture) {
        this.model = model;
        this.manufacturer = manufacturer;
        this.capacity = capacity;
        this.yearOfManufacture = yearOfManufacture;

    }
    public Plane(){
        
    }

//getter methods

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public int getId() {
        return id;
    }

    


//setter methods

    public void setModel(String model) {
       this.model = model;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCapacity(int capacity) {
      this.capacity = capacity;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

     public void setId(int id) {
       this.id = id;
    }

   
}
