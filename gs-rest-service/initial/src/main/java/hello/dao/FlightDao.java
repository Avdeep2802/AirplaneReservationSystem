package hello.dao;


import javax.transaction.Transactional;
import javax.sql.DataSource;

import org.springframework.data.repository.CrudRepository;

import hello.models.Flight;

@Transactional
public interface FlightDao extends CrudRepository<Flight, Long> {

  public Flight findBynumber(String number);

}