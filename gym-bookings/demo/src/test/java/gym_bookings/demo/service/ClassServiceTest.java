package gym_bookings.demo.service;

import gym_bookings.demo.assembler.ClassAssembler;
import gym_bookings.demo.domain.ClassDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.repository.ClassRepository;
import gym_bookings.demo.resource.ClassResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

  @InjectMocks
  private ClassService classService;

  @Mock
  private ClassRepository classRepository;

  @Mock
  private ClassAssembler classAssembler;

  @Test
  public void createClass_shouldCreateClass() {
    ClassResource classResource = getClassResource();
    ClassDomain classDomain = getClassDomain();
    ArrayList<ClassDomain> classDomains = new ArrayList<>();
    classDomains.add(classDomain);

    when(classRepository.findIfOverlapExists(classDomains)).thenReturn(false);
    when(classAssembler.toClassDomains(classResource)).thenReturn(classDomains);

    classService.createClass(classResource);

    verify(classRepository, times(1)).findIfOverlapExists(classDomains);
    verify(classRepository, times(1)).saveAll(classDomains);
  }

  @Test
  public void createClass_overlapClass_shouldThrowError() {
    ClassResource classResource = getClassResource();
    ClassDomain classDomain = getClassDomain();
    ArrayList<ClassDomain> classDomains = new ArrayList<>();
    classDomains.add(classDomain);

    when(classRepository.findIfOverlapExists(classDomains)).thenReturn(true);
    when(classAssembler.toClassDomains(classResource)).thenReturn(classDomains);

    CustomException ex = assertThrows(CustomException.class, () -> {
      classService.createClass(classResource);
    });
    assertEquals("Class overlaps with another class", ex.getMessage());
  }

  private ClassResource getClassResource() {
    ClassResource classResource = new ClassResource();
    classResource.setName("yoga");
    classResource.setStartDate("01-03-2025");
    classResource.setEndDate("01-03-2025");
    classResource.setStartTime("10:00");
    classResource.setDuration("120");
    classResource.setCapacity(30);
    return classResource;
  }

  private ClassDomain getClassDomain() {
    ClassDomain classDomain = new ClassDomain();
    classDomain.setId(1L);
    classDomain.setNoOfSlotsBooked(20);
    classDomain.setCapacity(30);
    classDomain.setName("yoga");
    classDomain.setClassStartDateTime(LocalDateTime.of(2025, 3, 1, 10, 0));
    classDomain.setClassEndDateTime(LocalDateTime.of(2025, 3, 1, 12, 0));
    return classDomain;
  }


}