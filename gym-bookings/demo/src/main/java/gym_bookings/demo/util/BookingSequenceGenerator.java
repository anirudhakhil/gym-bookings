package gym_bookings.demo.util;

import java.util.concurrent.atomic.AtomicLong;

//This should be generally handled by DB
public class BookingSequenceGenerator {

  private static final AtomicLong sequence = new AtomicLong(0);

  public static long getNext() {
    return sequence.incrementAndGet();
  }

}
