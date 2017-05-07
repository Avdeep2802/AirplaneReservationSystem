package hello.dao;


import javax.sql.DataSource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


import hello.models.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface ReservationDao extends CrudRepository<Reservation, Long> {

  public Reservation findByorderNumber(String number);
  
  
  @Query(value = "select order_number from reservation where passenger_id = ?", nativeQuery = true) 
        List<String> GetReservationNumberfromPassId(String passenger_id);
        
        
  @Query(value = "select number from flight where source = ?", nativeQuery = true)
  List<String> findFlightfromSource(String source);
  
  

  @Query(value = "select number from flight where destination = ?", nativeQuery = true)
  List<String> findFlightfromDestination(String destination);
  
  @Query(value = "select reservation_id from flight where number = ?", nativeQuery = true)
  List<String> findReservationfromFlight(String number);

}



