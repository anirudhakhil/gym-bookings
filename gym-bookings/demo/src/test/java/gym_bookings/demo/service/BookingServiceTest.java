package gym_bookings.demo.service;

import gym_bookings.demo.assembler.BookingAssembler;
import gym_bookings.demo.domain.BookingDomain;
import gym_bookings.demo.domain.ClassDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.repository.BookingRepository;
import gym_bookings.demo.repository.ClassRepository;
import gym_bookings.demo.resource.BookingResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @InjectMocks
  private BookingService bookingService;

  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private BookingAssembler bookingAssembler;

  @Mock
  private ClassRepository classRepository;


  @Test
  public void createBooking_shouldReturnBooking() {

    BookingResource bookingResource = getBookingResource();
    BookingDomain bookingDomain = getBookingDomain();
    ClassDomain classDomain = new ClassDomain();
    classDomain.setCapacity(20);
    classDomain.setNoOfSlotsBooked(10);
    classDomain.setId(1L);

    when(bookingAssembler.toBookingDomain(bookingResource)).thenReturn(bookingDomain);
    when(classRepository.findByClassNameAndDate(bookingDomain.getClassName(),
            bookingDomain.getParticipationDate().toLocalDate())).thenReturn(classDomain);

    BookingDomain booking = bookingService.createBooking(bookingResource);

    assertNotNull(booking);
    //Just to check if the classID is being set properly to booking
    assertEquals(booking.getClassId(), classDomain.getId());
    verify(bookingAssembler, times(1)).toBookingDomain(bookingResource);
    verify(classRepository, times(1)).findByClassNameAndDate(bookingDomain.getClassName(),
            bookingDomain.getParticipationDate().toLocalDate());
    verify(bookingRepository, times(1)).saveOrUpdate(bookingDomain);
    verify(classRepository, times(1)).saveOrUpdate(classDomain);

  }


  @Test
  public void createBooking_withNoSLotsLeft_shouldthrowError() {

    BookingResource bookingResource = getBookingResource();
    BookingDomain bookingDomain = getBookingDomain();
    ClassDomain classDomain = new ClassDomain();
    classDomain.setCapacity(20);
    classDomain.setNoOfSlotsBooked(20);
    classDomain.setId(1L);

    when(bookingAssembler.toBookingDomain(bookingResource)).thenReturn(bookingDomain);
    when(classRepository.findByClassNameAndDate(bookingDomain.getClassName(),
            bookingDomain.getParticipationDate().toLocalDate())).thenReturn(classDomain);

    CustomException ex = assertThrows(CustomException.class, () -> {
      bookingService.createBooking(bookingResource);
    });
    assertEquals("Sorry, the requested class is already full", ex.getMessage());

  }

  @Test
  public void createBooking_withInvalidClass_shouldthrowError() {

    BookingResource bookingResource = getBookingResource();
    BookingDomain bookingDomain = getBookingDomain();
    ClassDomain classDomain = new ClassDomain();
    classDomain.setCapacity(20);
    classDomain.setNoOfSlotsBooked(20);
    classDomain.setId(1L);

    when(bookingAssembler.toBookingDomain(bookingResource)).thenReturn(bookingDomain);
    when(classRepository.findByClassNameAndDate(bookingDomain.getClassName(),
            bookingDomain.getParticipationDate().toLocalDate())).thenReturn(null);

    CustomException ex = assertThrows(CustomException.class, () -> {
      bookingService.createBooking(bookingResource);
    });
    assertEquals("There is no class based on the requested input. " +
            "Please check if the name or date is incorrect", ex.getMessage());

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