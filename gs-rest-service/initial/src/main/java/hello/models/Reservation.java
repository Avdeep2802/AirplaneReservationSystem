package hello.models;

import hello.models.Flight;
import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.sql.DataSource;
import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;


@Entity
@Table(name = "Reservation")
public class Reservation {
    
    // @Id
    // private int id;   
    @Id
    @Column(name="orderNumber")
    private String orderNumber;

    @Column(name="price")
    private int price;

   
    @OneToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="passenger_id")
  private Passenger passenger;

@OneToMany
@JoinColumn(name="reservation_id", referencedColumnName="orderNumber")
    private List<Flight> flight;

     public Reservation(String orderNumber,Passenger passenger,int price,List<Flight> flight) {

        // this.id=id;
        this.orderNumber = orderNumber;
        this.passenger = passenger;
        this.price = price;
        this.flight = flight;
    }
    public Reservation(){
        
    }

//getter methods

    public String getOrderNumber() {
        return orderNumber;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public int getPrice() {
        return price;
    }

    public List<Flight> getFlight() {
        return flight;
    }

//setter methods

    // public void setId(int id) {
    //     this.id=id;
    // }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber=orderNumber;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger=passenger;
    }

    public void setPrice(int price) {
        this.price=price;
    }

    public void setFlight(List<Flight> flight) {
        this.flight=flight;
    }

   
}
