package hello.models;


 
import java.io.Serializable;
import java.util.List;
//import org.springframework.data.annotation.Id;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.sql.DataSource;

 
@Entity
@Table(name = "passenger")
public class Passenger implements Serializable {
 
    //private static final long serialVersionUID = -3009157732242241606L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
 
    @Column(name = "firstname")
    private String firstname;
 
    @Column(name = "lastname")
    private String lastname;


    @Column(name = "gender")
    private String gender;


    @Column(name = "age")
    private int age;


    @Column(name = "phone")
    private String phone;
    
    
    @OneToMany(mappedBy = "passenger")
    private List<Reservation> reservation;

    @ManyToMany(mappedBy = "passenger")
    private List<Flight> flight;
 
    public Passenger() {
    }
 
    public Passenger(String firstname, String lastname, int age,String gender,String phone, List<Flight> flight, List<Reservation> reservation) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.flight= flight;
        this.reservation=reservation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
    public List<Flight> getFlight() {
        return flight;
    }

    public List<Reservation> getReservation() {
        return reservation;
    }
    
    
    public void setFlight(List<Flight> flight) {
        this.flight = flight;
    }

    public void setReservation(List<Reservation> reservation) {
        this.reservation = reservation;
    }

}
 
    
