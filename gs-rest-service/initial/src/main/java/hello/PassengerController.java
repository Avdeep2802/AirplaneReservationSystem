package hello;
import javax.sql.DataSource;

import hello.dao.PassengerDao;
import hello.models.Passenger;
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

@RequestMapping("/passenger")
@RestController
public class PassengerController {

@Autowired
  private PassengerDao passengerDao;
  
  @ResponseBody
  @RequestMapping("{id}")
  public Passenger getById(@PathVariable("id") int id) { 
      
       Passenger passenger = null;
      try
      {
        passenger = passengerDao.findById(id);
        String fname = passenger.getFirstname();
        String lname = passenger.getLastname();
        int age = passenger.getAge();
        String gender = passenger.getGender();
        String phone = passenger.getPhone();
        
      }
      catch(Exception e)
      {
          System.out.println("User Not Found");
      }
      return passenger;
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
          p = new Passenger(fname, lname,age,gender,phone);
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