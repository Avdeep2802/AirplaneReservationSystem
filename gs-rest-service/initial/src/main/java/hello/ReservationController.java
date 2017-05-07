package hello;
import javax.sql.DataSource;
import hello.dao.PassengerDao;
import hello.dao.FlightDao;
import hello.dao.ReservationDao;
import hello.models.Flight;
import hello.models.Reservation;
import hello.models.Passenger;
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
//import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;




@RequestMapping("/reservation")
@Controller
public class ReservationController {

@Autowired
  private ReservationDao reservationDao;

@Autowired
 private FlightDao flightDao;
  
@Autowired
private PassengerDao passengerDao;


public String randomIdgen(){

    Random rand = new Random();
    int  n1 = rand.nextInt(25)+65;
    int  n2 = rand.nextInt(25)+65;
    int  n3 = rand.nextInt(999)+100;
    char c1= (char)n1;
    char c2= (char)n2;
    int c3= n3;
    String randomg= ""+c1+c2+c3;
    return randomg;

  }

@RequestMapping(method=RequestMethod.POST, produces=MediaType.APPLICATION_XML_VALUE)
public ResponseEntity<Reservation> createReservation(@RequestParam Map<String,String> requestParams) throws Exception{
  
  String passenger_id= requestParams.get("passengerId");
  System.out.println("passenger id : "+passenger_id );
  Passenger passenger= passengerDao.findById(Integer.parseInt(passenger_id));
  
  String flight_list= requestParams.get("flightLists");
  String[] array= flight_list.split(",");
  System.out.println("flight list array : "+ array[0]);
  // List<String> listStrings = new ArrayList<String>()
  List<Flight> list= new ArrayList<Flight>();
  String orderNumber= randomIdgen();
  
  Flight flight;
  int price=0;
  int seatsLeft = 0;
  for(int i=0;i<array.length;i++){
    flight = flightDao.findBynumber(array[i]);
    List <Passenger> passenger_list= flight.getPassenger();
    passenger_list.add(passenger);
    flight.setPassenger(passenger_list);
    price= price+ flight.getPrice();
    seatsLeft= flight.getSeatsLeft() -1;
    if(seatsLeft<0){
          throw new Exception("Sorry, the capacity of the flight has exeeded. -400");
        }
    flight.setSeatsLeft(seatsLeft);
    flightDao.save(flight);


    list.add(flight);
  }
    System.out.println("flight list : " + list);

      Reservation reservation= reservationDao.findByorderNumber(orderNumber);
      if(reservation==null){
          System.out.println("reservation creation process started .....");
      reservation = new Reservation(orderNumber, passenger, price, list);
      reservationDao.save(reservation);
          System.out.println("reservation saved");
    }
    else{
    throw new Exception("Another reservation with same number "+orderNumber+" exists.-400");
    }
  
        Passenger p= reservation.getPassenger();
    p.setFlight(null);
    p.setReservation(null);

    List<Flight> flightList= reservation.getFlight();
    for(Flight f : flightList){
      f.setPassenger(null);
      f.setReservation(null);
    }
    Reservation printReservation = new Reservation(reservation.getOrderNumber(), p, reservation.getPrice(),flightList);

    System.out.println("done bro");
    Reservation res1= reservationDao.findByorderNumber(orderNumber);
    System.out.println(res1.getPassenger());
    return ResponseEntity.ok(printReservation);

  }


@RequestMapping(value="{num}", produces=MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST)
public ResponseEntity<Reservation> updatereservation(@PathVariable String num,@RequestParam Map<String,String> requestParams) throws Exception
{
     Reservation reservation= reservationDao.findByorderNumber(num);
     List<Flight> list= reservation.getFlight();
     int price = reservation.getPrice();
     System.out.println("pricev jhgfchgfgfddxfgdxfg : "+price);
     String add_flights =  requestParams.get("flightsAdded");
     String[] add_Array = add_flights.split(",");
     String delete_flights = requestParams.get("flightsRemoved");
     
     String[] del_Array = delete_flights.split(",");
     Flight f;
     
     for(int i=0; i< add_Array.length; i++)
     {
        f = flightDao.findBynumber(add_Array[i]);
        price+= f.getPrice();
        list.add(f);
     }
     
     for(int i=0; i< del_Array.length; i++)
     {
        f = flightDao.findBynumber(del_Array[i]);
        price -= f.getPrice();
        list.remove(f);
     }
     reservation.setFlight(list);
     reservationDao.save(reservation);
          
          Passenger passenger= reservation.getPassenger();
    passenger.setFlight(null);
    passenger.setReservation(null);

    List<Flight> flightList= reservation.getFlight();
    for(Flight flight : flightList){
      flight.setPassenger(null);
      flight.setReservation(null);
    }
     
     Reservation res = reservationDao.findByorderNumber(num);
     if(res == null)
     {
         throw new Exception("Reservation not there");
     }
     else
     {
         return ResponseEntity.ok(res);
     }

     
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



  @RequestMapping(value = "{number}", method=RequestMethod.GET)
  public ResponseEntity<Reservation> getReservation(@PathVariable("number") String number) throws Exception
  {
      
          Reservation reservation = reservationDao.findByorderNumber(number);
          
          Passenger passenger= reservation.getPassenger();
    passenger.setFlight(null);
    passenger.setReservation(null);

    List<Flight> flightList= reservation.getFlight();
    for(Flight flight : flightList){
      flight.setPassenger(null);
      flight.setReservation(null);
    }
    
    if(reservation==null)
        {
        throw new Exception("Reservation does not exists");
        }
        return ResponseEntity.ok(reservation);
}
      
  


@RequestMapping(value="{orderNumber}",method = RequestMethod.DELETE)
@ResponseBody
public String deleteReservation(@PathVariable("orderNumber") String orderNumber) throws Exception
{
  Reservation reservation = reservationDao.findByorderNumber(orderNumber);
    if(reservation==null)
    {
        throw new Exception("Reservation does not exists");
    }
    else
    {
        reservationDao.delete(reservation);
        throw new Exception("Reservation "+orderNumber+ " Deleted");
    }
}


    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<Reservation>> SearchReservation(@RequestParam Map<String, String> requestParams) throws Exception {
        String passenger_id = requestParams.get("passengerId");
        String from = requestParams.get("from");
        String to = requestParams.get("to");
        String flight_no = requestParams.get("flightNumber");
        List<Reservation> listOfReservations = new ArrayList<Reservation>();
        List<Reservation> listOfReservationsPrint = new ArrayList<Reservation>();

        List<String> finalReservationList = new ArrayList<String>();

        List<String> reservationList = new ArrayList<String>();

        List<String> flightListSource = new ArrayList<String>();

        List<String> reservationListFlight = new ArrayList<String>();

        if (passenger_id != null) {
            reservationList = reservationDao.GetReservationNumberfromPassId(passenger_id);

            if (finalReservationList.size() > 0) {
                finalReservationList.retainAll(reservationList);
            } else {
                for (String reservation : reservationList) {
                    finalReservationList.add(reservation);
                }

            }

        }

        if (flight_no != null) {
            reservationList = reservationDao.findReservationfromFlight(flight_no);
            if (finalReservationList.size() > 0) {
                finalReservationList.retainAll(reservationList);
            } else {
                for (String reservation : reservationList) {
                    finalReservationList.add(reservation);
                }
            }

        }

        if (from != null) {

            flightListSource = reservationDao.findFlightfromSource(from);

            for (String flight : flightListSource) {
                reservationListFlight = reservationDao.findReservationfromFlight(flight);
                for (String reservation : reservationListFlight) {
                    reservationList.add(reservation);
                }

            }

            if (finalReservationList.size() > 0) {
                finalReservationList.retainAll(reservationList);
            } else {
                for (String reservation : reservationList) {
                    finalReservationList.add(reservation);
                }
            }

            if (to != null) {

                flightListSource = reservationDao.findFlightfromDestination(to);

                for (String fl : flightListSource) {
                    reservationListFlight = reservationDao.findReservationfromFlight(fl);
                    for (String reservation : reservationListFlight) {
                        reservationList.add(reservation);
                    }
                }

                if (finalReservationList.size() > 0) {
                    finalReservationList.retainAll(reservationList);
                } else {
                    for (String reservation : reservationList) {
                        finalReservationList.add(reservation);
                    }
                }

            }

        }
        Reservation res = null;

        for (String reservation : finalReservationList) {
            res = reservationDao.findByorderNumber(reservation);
            listOfReservations.add(res);
        }

        for (Reservation ress : listOfReservations) {

            Passenger p = ress.getPassenger();
            p.setFlight(null);
            p.setReservation(null);

            List<Flight> flightList = ress.getFlight();
            for (Flight f : flightList) {
                f.setPassenger(null);
                f.setReservation(null);
            }

            Reservation printReservation = new Reservation(ress.getOrderNumber(), p, ress.getPrice(), flightList);
            listOfReservationsPrint.add(printReservation);

        }

        return ResponseEntity.ok(listOfReservations);

    }
    
    
//    
//@RequestMapping(value="{orderNumber}",method = RequestMethod.DELETE)
//  @ResponseBody
//  public String delete(@PathVariable("orderNumber") String orderNumber) throws Exception{
//      
//    Reservation reservation = reservationDao.findByorderNumber(orderNumber);
//      if(reservation==null){
//        throw new Exception("Reservation with orderNumber "+orderNumber+" does not exist.-404");
//      }
//      else{
//      
//      List<Flight> flightList=reservation.getFlight();
//
//      int seatsLeft=0;
//      for(Flight flight : flightList){
//        seatsLeft=flight.getSeatsLeft()+1;
//        flight.setSeatsLeft(seatsLeft);
//        flightDao.save(flight);
//      }
//      reservationDao.delete(reservation);
//      // getSeatsLeft()+1;
//      throw new Exception("Reservation orderNumber "+orderNumber+" deleted successfully.-200");
//    }
//  }




//
//  @ResponseBody
//  @RequestMapping("{number}")
//  public Reservation getByNumber(@PathVariable("number") String number) {
//   
//    Reservation reservation =null;
//
//    try {
//
//      reservation = reservationDao.findByorderNumber(number);
//      String orderNumber = reservation.getOrderNumber();
//      int price = reservation.getPrice();
//        System.out.println(price +" : price");
//        System.out.println("order number : " + orderNumber);
//        Passenger p = reservation.getPassenger();
//        reservation.setPassenger(p);
//        
//      reservation.setOrderNumber(orderNumber);
//      reservation.setPrice(price);
//      //reservation.setPassenger(p);
//      
//     
//      
//      
//    }
//    catch (Exception ex) {
//      //return "no reservation found";
//    }
//    return reservation;
//  }

}