package gym_bookings.demo.assembler;

import gym_bookings.demo.domain.ClassDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.resource.ClassResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ClassAssemblerTest {

  @InjectMocks
  private ClassAssembler classAssembler;


  @Test
  public void toClassDomains_shouldReturnListOfDomains() {

    ClassResource classResource = getClassResource();

    ArrayList<ClassDomain> classDomains = classAssembler.toClassDomains(classResource);
    //Checking if it has created 3 class domains for 3 days
    assertEquals(classDomains.size(), 3);
    //Checking data for the first record, which should be sufficient
    assertEquals(classDomains.get(0).getName(), classResource.getName());
    assertEquals(classDomains.get(0).getCapacity(), classResource.getCapacity());
  }

  @Test
  public void toClassDomains_startDateGreaterThanEndDate_ShouldThrowError() {

    ClassResource classResource = getClassResource();
    classResource.setStartDate("03-03-2025");
    classResource.setEndDate("01-03-2025");

    CustomException ex = assertThrows(CustomException.class, () -> {
      classAssembler.toClassDomains(classResource);
    });

    assertEquals("End date time cannot be before start date time", ex.getMessage());
  }

  private static ClassResource getClassResource() {
    ClassResource classResource = new ClassResource();
    classResource.setName("yoga");
    classResource.setStartDate("01-03-2025");
    classResource.setEndDate("03-03-2025");
    classResource.setStartTime("10:00");
    classResource.setDuration("120");
    classResource.setCapacity(30);
    return classResource;
  }


}