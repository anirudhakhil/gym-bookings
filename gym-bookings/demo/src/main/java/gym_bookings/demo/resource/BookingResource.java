package gym_bookings.demo.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookingResource {

  @NotBlank
  private String memberName;

  @NotBlank
  private String className;

  @NotBlank
  private String participationDate;

  //This field is used to send in GET API response
  private LocalDateTime bookingDateTime;

  //This field is used to send in GET API response
  //This can be set to localDateTime. But since only class start time is asked, using LocalTime
  private LocalTime classStartTime;

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getParticipationDate() {
    return participationDate;
  }

  public void setParticipationDate(String participationDate) {
    this.participationDate = participationDate;
  }

  public LocalDateTime getBookingDateTime() {
    return bookingDateTime;
  }

  public void setBookingDateTime(LocalDateTime bookingDateTime) {
    this.bookingDateTime = bookingDateTime;
  }

  public LocalTime getClassStartTime() {
    return classStartTime;
  }

  public void setClassStartTime(LocalTime classStartTime) {
    this.classStartTime = classStartTime;
  }
}
