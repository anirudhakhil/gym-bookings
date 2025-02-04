package gym_bookings.demo.exceptions;

public class CustomException extends RuntimeException{

  private String errorMessage;

  public CustomException(String message) {
    super(message);
    setErrorMessage(message);
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
