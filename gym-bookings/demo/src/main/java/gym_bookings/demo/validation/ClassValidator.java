package gym_bookings.demo.validation;

import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.resource.ClassResource;
import org.springframework.stereotype.Service;

@Service
public class ClassValidator {


  public void validate(ClassResource classResource) {
    if(classResource.getCapacity() == 0 || classResource.getCapacity() < 0) {
      throw new CustomException("Capacity cannot be 0 or negative");
    }
  }
}
