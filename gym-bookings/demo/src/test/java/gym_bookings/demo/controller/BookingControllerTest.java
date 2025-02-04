package gym_bookings.demo.controller;

import gym_bookings.demo.assembler.BookingAssembler;
import gym_bookings.demo.domain.BookingDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.resource.BookingResource;
import gym_bookings.demo.resource.BookingsResource;
import gym_bookings.demo.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

  @InjectMocks
  private BookingController bookingController;

  @Mock
  private BookingService bookingService;

  @Mock
  private BookingAssembler bookingAssembler;

  @Test
  public void createBooking_shouldCreateBooking() {
    BookingResource bookingResource = getBookingResource();

    BookingDomain bookingDomain = getBookingDomain();

    when(bookingService.createBooking(bookingResource)).thenReturn(bookingDomain);
    when(bookingAssembler.toBookingResource(bookingDomain)).thenReturn(bookingResource);

    bookingController.createBooking(bookingResource);

    //Checking if the bookingService.createBooking has been invoked or not once
    verify(bookingService, times(1)).createBooking(bookingResource);
    verify(bookingAssembler, times(1)).toBookingResource(bookingDomain);

  }

  @Test
  public void getBooking_shouldReturnBooking() {
    BookingResource bookingResource = getBookingResource();

    String memberName = "Anirudh";
    String startDateString = "01-03-2025";
    String endDateString = "01-03-2025";
    LocalDate startDate = LocalDate.of(2025, 3, 1);
    LocalDate endDate = LocalDate.of(2025, 3, 1);

    ArrayList<BookingResource> bookingResources = new ArrayList<>();
    bookingResources.add(bookingResource);

    when(bookingService.getBookings(memberName, startDate, endDate)).thenReturn(bookingResources);

    ResponseEntity<BookingsResource> bookings = bookingController.getBookings(memberName, startDateString, endDateString);
    assertNotNull(bookings);
    assertEquals(bookings.getBody().getBookings().size(), bookingResources.size());
    verify(bookingService, times(1)).getBookings(memberName, startDate, endDate);
  }

  @Test
  public void getBooking_withNullStartDate_shouldThrowError() {
    BookingResource bookingResource = getBookingResource();

    String memberName = "Anirudh";
    String startDateString = "01-03-2025";
    String endDateString = "01-03-2025";
    LocalDate startDate = null;
    LocalDate endDate = LocalDate.of(2025, 3, 1);

    ArrayList<BookingResource> bookingResources = new ArrayList<>();
    bookingResources.add(bookingResource);

    CustomException ex = assertThrows(CustomException.class, () -> {
      bookingController.getBookings(memberName, null, endDateString);
    });
    assertEquals("startDate is required when endDate is provided", ex.getMessage());
  }

  @Test
  public void getBooking_withNullEndDate_shouldThrowError() {
    BookingResource bookingResource = getBookingResource();

    String memberName = "Anirudh";
    String startDateString = "01-03-2025";
    String endDateString = "01-03-2025";
    LocalDate startDate = LocalDate.of(2025, 3, 1);
    LocalDate endDate = LocalDate.of(2025, 3, 1);

    ArrayList<BookingResource> bookingResources = new ArrayList<>();
    bookingResources.add(bookingResource);

    CustomException ex = assertThrows(CustomException.class, () -> {
      bookingController.getBookings(memberName, startDateString, null);
    });
    assertEquals("endDate is required when startDate is provided", ex.getMessage());
  }

  private static BookingResource getBookingResource() {
    BookingResource bookingResource = new BookingResource();
    bookingResource.setMemberName("Anirudh");
    bookingResource.setClassName("yoga");
    bookingResource.setParticipationDate("01-03-2025");
    return bookingResource;
  }

  private static BookingDomain getBookingDomain() {
    BookingDomain bookingDomain = new BookingDomain();
    bookingDomain.setMemberName("Anirudh");
    bookingDomain.setClassName("yoga");
    bookingDomain.setParticipationDate(LocalDateTime.of(2025, 3, 1, 0, 0));
    return bookingDomain;
  }


}