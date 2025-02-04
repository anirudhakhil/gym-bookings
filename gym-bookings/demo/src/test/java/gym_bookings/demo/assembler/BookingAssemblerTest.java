package gym_bookings.demo.assembler;

import gym_bookings.demo.domain.BookingDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.resource.BookingResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookingAssemblerTest {

  @InjectMocks
  private BookingAssembler bookingAssembler;

  private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Test
  public void toBookingDomain_ShouldReturnBookingDomain() {
    BookingResource bookingResource = getBookingResource();

    BookingDomain bookingDomain = bookingAssembler.toBookingDomain(bookingResource);
    assertEquals(bookingDomain.getMemberName(), bookingResource.getMemberName().toLowerCase());
    assertEquals(bookingDomain.getClassName(), bookingResource.getClassName().toLowerCase());
    LocalDateTime partitionDate = LocalDate.parse(bookingResource.getParticipationDate(), dateFormatter).atStartOfDay();
    assertEquals(bookingDomain.getParticipationDate(), partitionDate);
  }

  @Test()
  public void toBookingDomain_PastDate_ShouldThrowError() {
    BookingResource bookingResource = getBookingResource();
    //Setting past date for exception to be thrown
    bookingResource.setParticipationDate("01-03-2024");

    CustomException ex = assertThrows(CustomException.class, () -> {
      bookingAssembler.toBookingDomain(bookingResource);
    });
    assertEquals("Participation date cannot be in the past", ex.getMessage());
  }

  @Test
  public void toBookingResource_ShouldReturnBookingResource() {
    BookingDomain bookingDomain = getBookingDomain();

    BookingResource bookingResource = bookingAssembler.toBookingResource(bookingDomain);
    assertEquals(bookingResource.getMemberName(), bookingDomain.getMemberName());
    assertEquals(bookingResource.getClassName(), bookingDomain.getClassName());
    assertEquals(bookingResource.getParticipationDate(), bookingDomain.getParticipationDate().format(dateFormatter));
    assertEquals(bookingResource.getBookingDateTime(), bookingDomain.getBookingDateTime());
  }

  private static BookingResource getBookingResource() {
    BookingResource bookingResource = new BookingResource();
    bookingResource.setMemberName("Anirudh");
    bookingResource.setClassName("Yoga");
    bookingResource.setParticipationDate("01-03-2025");
    return bookingResource;
  }

  private BookingDomain getBookingDomain() {
    BookingDomain bookingDomain = new BookingDomain();
    bookingDomain.setMemberName("Anirudh");
    bookingDomain.setClassName("Yoga");
    bookingDomain.setParticipationDate(LocalDate.parse("01-03-2025", dateFormatter).atStartOfDay());
    bookingDomain.setBookingDateTime(LocalDateTime.now());
    return bookingDomain;
  }

}