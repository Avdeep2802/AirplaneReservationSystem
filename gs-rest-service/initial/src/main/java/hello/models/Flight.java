package hello.models;

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

//import hello.models.Plane;
import java.util.*;

@Entity
@Table(name = "Flight")
public class Flight {
    
    // @Column(name= "id")
    // private int id;

    @Id
    @Column(name = "number")
    private String number;

   @Column(name = "price")
    private int price;   

    @Column(name = "source")
    private String from;

    @Column(name = "seatsLeft")
    private int seatsLeft;

    @Column(name = "destination")
    private String to;

    @Column(name = "departureTime")
    private String departureTime;

    @Column(name = "arrivalTime")
    private String arrivalTime;

    @Column(name = "description")
    private String description;

    // @Column(name = "plane_id")
    // private int plane_id;


    // @Column(name="passenger_id")
    // private String passenger_id;

  @OneToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="plane_id")

  private Plane plane;


    
@ManyToMany

@JoinTable(name="pftable", joinColumns={@JoinColumn(name="number")},inverseJoinColumns={@JoinColumn(name="id")})

private List<Passenger> passenger;
    
    
   @ManyToMany(mappedBy="flight")
      @JsonIgnore

    private List<Reservation> reservation; 
    
    public Flight(List<Passenger> passenger){

        this.passenger=passenger;
        
    }

     public Flight(String number,String from,int price,String to,int seatsLeft,String departureTime,String arrivalTime,String description,Plane plane) {
    
        this.number = number;
        this.from = from;
        this.to = to;
        this.price = price;
        this.seatsLeft = seatsLeft;
        this.departureTime = departureTime;
        this.arrivalTime=arrivalTime;
        this.description=description;
        this.plane=plane;

    }

    public Flight(){
        
    }



     public String getNumber() {
        return number;
    }

     
     public void setNumber(String number) {
        this.number=number;
    }
     
     
     
     
     
     
     
    public String getFrom() {
        return from;
    }

    
     public void setFrom(String from) {
        this.from=from;
    }

     
     
     
     
    public String getTo() {
        return to;
    }
    
    
     public void setTo(String to) {
        this.to=to;
    }
     
     
     
     
     
    

    public int getSeatsLeft() {
        return seatsLeft;
    }
    
    
     public void setSeatsLeft(int seatsLeft) {
        this.seatsLeft=seatsLeft;
    }
     
     
     
     
     

    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price=price;
    }

    
    
    
    
    

    public String getDepartureTime() {
        return departureTime;
    }
    
    
     public void setDepartureTime(String departureTime) {
        this.departureTime=departureTime;
    }
     
     
     
     
    
    public String getArrivalTime() {
        return arrivalTime;
    }
    
    
    
     public void setArrivalTime(String arrivalTime) {
        this.arrivalTime=arrivalTime;
    }
     
     
     
     
     

    public String getDescription() {
        return description;
    }
    
    
    public void setDescription(String description) {
        this.description=description;
    }
    
    
    
    public Plane getPlane() {
        return plane;
    }
    
    
    public void setPlane(Plane plane) {
        this.plane=plane;
    }
    
    
    
    
    public List<Passenger> getPassenger() {
        return passenger;
    }
    
    
     
    public void setPassenger(List<Passenger> passenger) {
        this.passenger=passenger;
    }
    
    public List<Reservation> getReservation() {
        return reservation;
    }
    
    
    
     public void setReservation(List<Reservation> reservation) {
        this.reservation=reservation;
    }
}

   

   
   

   

    
   
   

   
    
