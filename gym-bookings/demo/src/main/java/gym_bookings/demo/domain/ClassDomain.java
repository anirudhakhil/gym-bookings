package gym_bookings.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ClassDomain {

  @Id
  // treating this as sequence generated value
  //would be used as a FK reference in bookings table
  private Long id;

  private String name;

  @Column(name="class_start_date_time")
  private LocalDateTime classStartDateTime;

  @Column(name="class_end_date_time")
  private LocalDateTime classEndDateTime;

  private int capacity;

  private int noOfSlotsBooked = 0;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getClassStartDateTime() {
    return classStartDateTime;
  }

  public void setClassStartDateTime(LocalDateTime classStartDateTime) {
    this.classStartDateTime = classStartDateTime;
  }

  public LocalDateTime getClassEndDateTime() {
    return classEndDateTime;
  }

  public void setClassEndDateTime(LocalDateTime classEndDateTime) {
    this.classEndDateTime = classEndDateTime;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public int getNoOfSlotsBooked() {
    return noOfSlotsBooked;
  }

  public void setNoOfSlotsBooked(int noOfSlotsBooked) {
    this.noOfSlotsBooked = noOfSlotsBooked;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
