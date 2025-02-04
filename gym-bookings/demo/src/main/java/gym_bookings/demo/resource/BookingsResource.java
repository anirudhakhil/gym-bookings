package gym_bookings.demo.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingsResource {

  private ArrayList<BookingResource> bookings;

  public ArrayList<BookingResource> getBookings() {
    if (bookings == null) {
      return new ArrayList<>();
    }
    return bookings;
  }

  public void setBookings(ArrayList<BookingResource> bookings) {
    this.bookings = bookings;
  }
}
