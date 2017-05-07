package hello;
import javax.sql.DataSource;

import hello.dao.PassengerDao;
import hello.models.Flight;
import hello.models.Passenger;
import hello.models.Reservation;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/passenger")
@RestController
public class PassengerController {

    @Autowired
    private PassengerDao passengerDao;

    @ResponseBody
    @RequestMapping(value = "{id}",params="xml", produces=MediaType.APPLICATION_XML_VALUE, method = RequestMethod.GET)
    public ResponseEntity<Passenger> getByIdXML(@PathVariable("id") int id, @RequestParam boolean xml) throws Exception {

        Passenger passenger = null;

        passenger = passengerDao.findById(id);
        if (passenger == null) {
            throw new Exception("The requested passenger with id " + id + " does not exist-404");
        }
        List<Reservation> reservationList = passenger.getReservation();
        for (Reservation reservation : reservationList) {
            reservation.setPassenger(null);
            List<Flight> flightList = reservation.getFlight();
            for (Flight flight : flightList) {
                flight.setPassenger(null);
            }
            
            String fname = passenger.getFirstname();
            String lname = passenger.getLastname();
            int age = passenger.getAge();
            String gender = passenger.getGender();
            String phone = passenger.getPhone();
            passenger = new Passenger(fname, lname, age, gender, phone, flightList, reservationList);
            passenger.setFlight(null);
            passenger.setReservation(null);

        }

        return ResponseEntity.ok(passenger);
    }
    
    
    
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, String> errorResponse(Exception ex, HttpServletResponse response) {
        Map<String, String> errorMap = new HashMap<String, String>();
        String ans = ex.getMessage();
        String[] a = ans.split("-");
        String msg = a[0];
        String code = a[1];
        errorMap.put("code", code);
        errorMap.put("msg", msg);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();
//errorMap.put("errorStackTrace", stackTrace);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return errorMap;
    }

    
     @ResponseBody
    @RequestMapping(value = "{id}",params="json", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<Passenger> getByIdJSON(@PathVariable("id") int id, @RequestParam boolean json) throws Exception {

        Passenger passenger = null;

        passenger = passengerDao.findById(id);
        if (passenger == null) {
            throw new Exception("The requested passenger with id " + id + " does not exist-404");
        }
        List<Reservation> reservationList = passenger.getReservation();
        for (Reservation reservation : reservationList) {
            reservation.setPassenger(null);
            List<Flight> flightList = reservation.getFlight();
            for (Flight flight : flightList) {
                flight.setPassenger(null);
            }
            
            String fname = passenger.getFirstname();
            String lname = passenger.getLastname();
            int age = passenger.getAge();
            String gender = passenger.getGender();
            String phone = passenger.getPhone();
            
            passenger = new Passenger(fname, lname, age, gender, phone, flightList, reservationList);
            passenger.setFlight(null);
            passenger.setReservation(null);
        }

        return ResponseEntity.ok(passenger);
    }

@RequestMapping(method = RequestMethod.POST)   
public Passenger createNewUser(@RequestParam Map<String,String> allRequestParams){
    String fname = allRequestParams.get("firstname");
    String lname = allRequestParams.get("lastname");
    int age = Integer.parseInt(allRequestParams.get("age"));
    String gender = allRequestParams.get("gender");
    String phone  = allRequestParams.get("phone");
    Passenger p = null;
    try
    {
          p = new Passenger(fname, lname,age,gender,phone,null,null);
          passengerDao.save(p);
    }
    catch (Exception ex) {
      //return "sala nae chalya";

    }
    return p;
}


@ResponseBody
@RequestMapping(value="{id}",method = RequestMethod.PUT )
public Passenger updateById(@PathVariable("id") int id, @RequestParam Map<String,String> allRequestParams) { 
      
    Passenger passenger = null;
    try
    {
        passenger = passengerDao.findById(id);
        String fname = allRequestParams.get("firstname");
        String lname = allRequestParams.get("lastname");
        int age = Integer.parseInt(allRequestParams.get("age"));
        String gender = allRequestParams.get("gender");
        String phone  = allRequestParams.get("phone");
        passenger.setFirstname(fname);
        passenger.setLastname(lname);
        passenger.setAge(age);
        passenger.setGender(gender);
        passenger.setPhone(phone);
        passenger.setReservation(null);
        passenger.setFlight(null);
        passengerDao.save(passenger); 
    }
    catch(Exception e)
    {
        System.out.println("User Not Found");
    }
    return passenger;
 }

  
  @ResponseBody
  
  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  public String deleteByID(@PathVariable("id") int id) { 
      
       Passenger passenger = null;
      try
      {
        passenger = passengerDao.findById(id);
        passengerDao.delete(passenger);        
      }
      catch(Exception e)
      {
          System.out.println("User Not Found");
      }
      return "Record Deleted";
  }

  
 
}