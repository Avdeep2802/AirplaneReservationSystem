package hello.dao;



import javax.sql.DataSource;

import hello.models.Passenger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PassengerDao extends CrudRepository<Passenger, Long> {

  public Passenger findById(int id);

}