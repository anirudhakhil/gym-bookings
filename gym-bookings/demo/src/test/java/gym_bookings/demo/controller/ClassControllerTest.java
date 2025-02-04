package gym_bookings.demo.controller;

import gym_bookings.demo.resource.ClassResource;
import gym_bookings.demo.service.ClassService;
import gym_bookings.demo.validation.ClassValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClassControllerTest {

  @InjectMocks
  private ClassController classController;

  @Mock
  private ClassService classService;

  @Mock
  private ClassValidator classValidator;

  @Test
  public void createClass_shouldCreateClass() {

    ClassResource classResource = getClassResource();

    doNothing().when(classValidator).validate(classResource);
    doNothing().when(classService).createClass(classResource);

    ResponseEntity<ClassResource> responseEntity = classController.createClass(classResource);
    assertNotNull(responseEntity);
    //Verifying if they are called once
    verify(classValidator, times(1)).validate(classResource);
    verify(classService, times(1)).createClass(classResource);

  }

  private ClassResource getClassResource() {
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