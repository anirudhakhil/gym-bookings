package gym_bookings.demo.service;

import gym_bookings.demo.assembler.BookingAssembler;
import gym_bookings.demo.domain.BookingDomain;
import gym_bookings.demo.domain.ClassDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.repository.BookingRepository;
import gym_bookings.demo.repository.ClassRepository;
import gym_bookings.demo.resource.BookingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class BookingService {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private BookingAssembler bookingAssembler;

  @Autowired
  private ClassRepository classRepository;

  public BookingDomain createBooking(BookingResource bookingResource) {
    try {
      BookingDomain bookingDomain = bookingAssembler.toBookingDomain(bookingResource);
      ClassDomain classDomain =
              classRepository.findByClassNameAndDate(bookingDomain.getClassName(),
                      bookingDomain.getParticipationDate().toLocalDate());
      if (classDomain != null) {
        validateCapacity(classDomain);
        processAndSaveBookingAndClassEntities(bookingDomain, classDomain);
      } else {
       throw new CustomException("There is no class based on the requested input. Please check if the name or date is incorrect");
      }
      return bookingDomain;
    } catch (Exception e) {
      throw new CustomException(e.getMessage());
    }
  }

  public ArrayList<BookingResource> getBookings(String memberName, LocalDate start, LocalDate end) {
    try {
      ArrayList<BookingDomain> bookingDomains = new ArrayList<>();
      //Ideally this is not the way to search in DB. We make use of JPQL query to form dynamic query, by checking for null values
      //Another approach is we can also use criteria API or build sql query on the fly instead of these if else conditions
      if(memberName != null && start != null && end != null) {
        bookingDomains = bookingRepository.findByMemberNameAndStartDateAndEndDate(memberName.toLowerCase(), start, end);
      }
      else if(start != null && end != null) {
        bookingDomains = bookingRepository.findByStartDateAndEndDate(start, end);
      }
      else if (memberName != null) {
        bookingDomains = bookingRepository.findByMemberName(memberName.toLowerCase());
      }
      else {
        bookingDomains = bookingRepository.findAll();
      }

      ArrayList<BookingResource> bookingResources = new ArrayList<>();
      for(BookingDomain bookingDomain : bookingDomains) {
        BookingResource bookingResource = bookingAssembler.toBookingResource(bookingDomain);
        getClassStartTimeAndSetToBooking(bookingDomain, bookingResource);
        bookingResources.add(bookingResource);
      }
      return bookingResources;
    } catch (Exception e) {
      throw new CustomException(e.getMessage());
    }
  }

  private static void validateCapacity(ClassDomain classDomain) {
    if (classDomain.getCapacity() - classDomain.getNoOfSlotsBooked() <= 0) {
      throw new CustomException("Sorry, the requested class is already full");
    }
  }

  //Ideally this must be an atomic operation. We can make use of @Transactional annotation - done in another class for a public method
  //Also, it's better if we have a lock during these operations because of parallel processing
  //since there are 2 save/update calls happening here
  private void processAndSaveBookingAndClassEntities(BookingDomain bookingDomain, ClassDomain classDomain) {
    //mapping booking and class via the FK reference
    bookingDomain.setClassId(classDomain.getId());
    bookingRepository.saveOrUpdate(bookingDomain);
    classDomain.setNoOfSlotsBooked(classDomain.getNoOfSlotsBooked() + 1);
    classRepository.saveOrUpdate(classDomain);
  }

  private void getClassStartTimeAndSetToBooking(BookingDomain bookingDomain, BookingResource bookingResource) {
    //Ideally this is not required because we will have a FK reference, so we'll get the object directly instead of searching again
    ClassDomain classDomain = classRepository.findByClassId(bookingDomain.getClassId());
    if (classDomain != null) {
      bookingResource.setClassStartTime(classDomain.getClassStartDateTime().toLocalTime());
    }
  }


}
