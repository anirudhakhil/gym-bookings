package gym_bookings.demo.repository;

import gym_bookings.demo.domain.BookingDomain;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
//@Repository
//ALL OF THESE METHODS WOULD BE REPLACED BY ACTUAL DB QUERIES - spring data jpa, jpql queries etc based on requirement
public class BookingRepository {

  ArrayList<BookingDomain> data = new ArrayList<>();

  public boolean saveOrUpdate(BookingDomain bookingDomain) {
    int index = -1;
    for(BookingDomain bookingDb : data) {
      if(bookingDomain.getId().equals(bookingDb.getId())) {
        index = data.indexOf(bookingDb);
        break;
      }
    }
    if (index != -1) {
      data.remove(index);
    }
    return data.add(bookingDomain);
  }

  public ArrayList<BookingDomain> findByMemberNameAndStartDateAndEndDate(String memberName, LocalDate start, LocalDate end) {
    ArrayList<BookingDomain> result = new ArrayList<>();
    for(BookingDomain bookingDomain : data) {
      if(bookingDomain.getMemberName().equals(memberName)
              && bookingDomain.getParticipationDate().toLocalDate().isAfter(start.minusDays(1))
              && bookingDomain.getParticipationDate().toLocalDate().isBefore(end.plusDays(1))) {
        result.add(bookingDomain);
      }
    }
    return result;
  }

  public ArrayList<BookingDomain> findByMemberName(String memberName) {
    ArrayList<BookingDomain> result = new ArrayList<>();
    for(BookingDomain bookingDomain : data) {
      if(bookingDomain.getMemberName().equals(memberName)) {
        result.add(bookingDomain);
      }
    }
    return result;
  }

  public ArrayList<BookingDomain> findAll() {
    return data;
  }

  public ArrayList<BookingDomain> findByStartDateAndEndDate(LocalDate start, LocalDate end) {
    ArrayList<BookingDomain> result = new ArrayList<>();
    for(BookingDomain bookingDomain : data) {
      if(bookingDomain.getParticipationDate().toLocalDate().isAfter(start.minusDays(1))
              && bookingDomain.getParticipationDate().toLocalDate().isBefore(end.plusDays(1))) {
        result.add(bookingDomain);
      }
    }
    return result;
  }
}
