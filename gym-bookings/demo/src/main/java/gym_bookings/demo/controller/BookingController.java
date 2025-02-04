package gym_bookings.demo.controller;

import gym_bookings.demo.assembler.BookingAssembler;
import gym_bookings.demo.domain.BookingDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.resource.BookingResource;
import gym_bookings.demo.resource.BookingsResource;
import gym_bookings.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingController {

  private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Autowired
  private BookingService bookingService;

  @Autowired
  private BookingAssembler bookingAssembler;

  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<BookingResource> createBooking(@RequestBody BookingResource bookingResource) {
    BookingDomain booking = bookingService.createBooking(bookingResource);
    return ResponseEntity.ok(bookingAssembler.toBookingResource(booking));
  }

  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<BookingsResource> getBookings(
          @RequestParam(required = false) String memberName,
          @RequestParam(required = false) String startDate,
          @RequestParam(required = false) String endDate
  ) {
    LocalDate start = startDate != null ? LocalDate.parse(startDate, dateFormatter) : null;
    LocalDate end = endDate != null ? LocalDate.parse(endDate, dateFormatter) : null;
    validateStartAndEndDates(start, end);
    ArrayList<BookingResource> bookingResources = bookingService.getBookings(memberName, start, end);
    BookingsResource bookingsResource = new BookingsResource();
    bookingsResource.setBookings(bookingResources);
    return ResponseEntity.ok(bookingsResource);
  }

  private static void validateStartAndEndDates(LocalDate start, LocalDate end) {
    //Ideally if start date is not provided, we can have a default value for start date
    //We can maintain a configurable property like no of Days to look back for start date and calculate start date accordingly
    if(start == null && end !=null) {
      throw new CustomException("startDate is required when endDate is provided");
    }
    //Ideally if end date is not provided, we can have a default value for end date. For example - current time
    if(start != null && end == null) {
      throw new CustomException("endDate is required when startDate is provided");
    }
  }

}
