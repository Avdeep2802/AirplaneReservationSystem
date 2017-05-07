package hello;
import hello.dao.FlightDao;
import javax.sql.DataSource;
import hello.dao.PassengerDao;

import hello.dao.ReservationDao;
import hello.dao.PlaneDao;

import hello.models.Flight;
import hello.models.Reservation;
import hello.models.Passenger;
import hello.models.Plane;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;




@RequestMapping("/flight")
@Controller
public class FlightController {

@Autowired
private PlaneDao planeDao;
    
@Autowired
private FlightDao flightDao;
    
@Autowired
  private ReservationDao reservationDao;
  
@Autowired
private PassengerDao passengerDao;

@ResponseBody
@RequestMapping(value ="{flight_number}", produces=MediaType.APPLICATION_XML_VALUE,  method = RequestMethod.POST)
public Flight createFlight(@PathVariable String flight_number,@RequestParam Map<String,String> requestParams)
{
    Flight f = null;
   try
   {
        String from=requestParams.get("from");
        String to=requestParams.get("to");
        String p=requestParams.get("price");
        int price= Integer.parseInt(p);
        String description= requestParams.get("description");
        String departureTime=requestParams.get("departureTime");
        String arrivalTime=requestParams.get("arrivalTime");

        String model=requestParams.get("model");
        String manufacturer=requestParams.get("manufacturer");
        String c=requestParams.get("capacity");
        int capacity= Integer.parseInt(c);
        String yom=requestParams.get("yearOfManufacture");
        int yearOfManufacturer= Integer.parseInt(yom);

        f  = flightDao.findBynumber(flight_number);
        if(f == null)
        {
            System.out.println("flight is not there so creating a new one !!!!!!!!!!!!!!!");
             Plane plane = new Plane(model, manufacturer, capacity, yearOfManufacturer);
             planeDao.save(plane);
             Flight flight = new Flight(flight_number, from, price, to, capacity, departureTime, arrivalTime, description, plane);
             flightDao.save(flight);
             return flight;
        }
        else
        {
             f.setNumber(flight_number);
             f.setFrom(from);
             f.setTo(to);
             f.setPrice(price);
             f.setSeatsLeft(capacity);
             f.setDepartureTime(departureTime);
             f.setArrivalTime(arrivalTime);
             f.setDescription(description);

             Plane plane = f.getPlane();
             int plane_id = plane.getId();

             Plane p_dao = planeDao.findById(plane_id);
             p_dao.setModel(model);
             p_dao.setCapacity(capacity);
             p_dao.setYearOfManufacture(yearOfManufacturer);
             p_dao.setManufacturer(manufacturer);   

             flightDao.save(f);
             planeDao.save(p_dao);
        }
   }
   catch(Exception e)
    {
        //return ResponseEntity.ok(f);
    }
return f;
   
}


@ExceptionHandler(Exception.class)
@ResponseBody
public Map<String,String> errorResponse(Exception ex, HttpServletResponse response){
Map<String,String> errorMap = new HashMap<String,String>();
String ans=ex.getMessage();
String[] a=ans.split("-");
String msg=a[0];
String code=a[1];
errorMap.put("code",code);
errorMap.put("msg",msg);
StringWriter sw = new StringWriter();
PrintWriter pw = new PrintWriter(sw);
ex.printStackTrace(pw);
String stackTrace = sw.toString();
//errorMap.put("errorStackTrace", stackTrace);
response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
return errorMap;
}



@RequestMapping(value="{number}", 
            params="json",
            produces=MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET)

public ResponseEntity<Flight> getFlight(@PathVariable String number, @RequestParam boolean json) throws Exception{
    Flight flight = flightDao.findBynumber(number);

    if(flight==null)
    {
      throw new Exception("Sorry, the requested flight "+number+" does not exist-404");
    }

     List<Passenger> passengerList= flight.getPassenger();
    
    for(Passenger passenger : passengerList){
      passenger.setFlight(null);
      passenger.setReservation(null);
    }

    Flight printFlight=new Flight(flight.getNumber(), flight.getFrom(), flight.getPrice(),flight.getTo(),flight.getSeatsLeft(),flight.getDepartureTime(),flight.getArrivalTime(),flight.getDescription(),flight.getPlane());
          printFlight.setPassenger(passengerList);

    System.out.println("F: "+flight);
    if(flight==null)
    {
      throw new Exception("Sorry, the requested flight "+number+" does not exist-404");
    }
    return ResponseEntity.ok(printFlight);
} 


@RequestMapping(value="{number}", 
            params="xml",
            produces=MediaType.APPLICATION_XML_VALUE,method=RequestMethod.GET)

public ResponseEntity<Flight> getFlightXML(@PathVariable String number, @RequestParam boolean xml) throws Exception{
    Flight flight = flightDao.findBynumber(number);

    if(flight==null)
    {
      throw new Exception("Sorry, the requested flight "+number+" does not exist-404");
    }

     List<Passenger> passengerList= flight.getPassenger();
    
    for(Passenger passenger : passengerList){
      passenger.setFlight(null);
      passenger.setReservation(null);
    }

    Flight printFlight=new Flight(flight.getNumber(), flight.getFrom(), flight.getPrice(),flight.getTo(),flight.getSeatsLeft(),flight.getDepartureTime(),flight.getArrivalTime(),flight.getDescription(),flight.getPlane());
          printFlight.setPassenger(passengerList);

    System.out.println("F: "+flight);
    if(flight==null)
    {
      throw new Exception("Sorry, the requested flight "+number+" does not exist-404");
    }
    return ResponseEntity.ok(printFlight);
} 


@RequestMapping(value="{flight_number}",method = RequestMethod.DELETE)
@ResponseBody
public String delete(@PathVariable("flight_number") String flight_number) {
    try {
      Flight f = flightDao.findBynumber(flight_number);
      flightDao.delete(f);
    }
    catch (Exception ex) {
      return "Error deleting the flight:" + ex.toString();
    }
    return "FLIGHT DELETED";
  }





}