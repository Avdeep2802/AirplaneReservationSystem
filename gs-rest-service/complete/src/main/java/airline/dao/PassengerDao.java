package dao;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.transaction.annotation.Transactional;
import models.Passenger;
import org.springframework.data.repository.CrudRepository;
       
/**
 *
 * @author avdeepsandhu
 */
@Transactional
public interface PassengerDao extends CrudRepository<Passenger, Long> {

 
  public Passenger findById(int id);

}