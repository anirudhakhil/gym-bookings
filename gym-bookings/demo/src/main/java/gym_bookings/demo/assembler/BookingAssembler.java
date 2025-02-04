package gym_bookings.demo.assembler;

import gym_bookings.demo.domain.BookingDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.resource.BookingResource;
import gym_bookings.demo.util.BookingSequenceGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BookingAssembler {

  private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public BookingDomain toBookingDomain(BookingResource bookingResource) {
    BookingDomain bookingDomain = new BookingDomain();
    //Manually setting the id here. Ideally, this will be done at the DB level
    bookingDomain.setId(BookingSequenceGenerator.getNext());
    bookingDomain.setClassName(bookingResource.getClassName().toLowerCase());
    bookingDomain.setMemberName(bookingResource.getMemberName().toLowerCase());
    LocalDate participationDate = LocalDate.parse(bookingResource.getParticipationDate(), dateFormatter);
    if (participationDate.isBefore(LocalDate.now())) {
      throw new CustomException("Participation date cannot be in the past");
    }
    bookingDomain.setParticipationDate(participationDate.atStartOfDay());

    //Current time would be the booking time
    bookingDomain.setBookingDateTime(LocalDateTime.now());
    return bookingDomain;
  }

  public BookingResource toBookingResource(BookingDomain bookingDomain) {
    BookingResource bookingResource = new BookingResource();
    bookingResource.setClassName(bookingDomain.getClassName());
    bookingResource.setMemberName(bookingDomain.getMemberName());
    //Trying to maintain datetime as much as possible to have both time and date available always if required later
    bookingResource.setParticipationDate(bookingDomain.getParticipationDate().format(dateFormatter));
    bookingResource.setBookingDateTime(bookingDomain.getBookingDateTime());
    return bookingResource;
  }
}
