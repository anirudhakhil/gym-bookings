package gym_bookings.demo.service;

import gym_bookings.demo.assembler.ClassAssembler;
import gym_bookings.demo.domain.ClassDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.repository.ClassRepository;
import gym_bookings.demo.resource.ClassResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClassService {

  @Autowired
  private ClassRepository classRepository;

  @Autowired
  private ClassAssembler classAssembler;

  public void createClass(ClassResource classResource) {
    try {
      ArrayList<ClassDomain> classDomains = classAssembler.toClassDomains(classResource);
      validateIfOverLapExists(classDomains);
      saveToDatabase(classDomains);
    } catch (Exception e) {
      throw new CustomException(e.getMessage());
    }
  }

  private void saveToDatabase(ArrayList<ClassDomain> classDomains) {
    classRepository.saveAll(classDomains);
  }

  private void validateIfOverLapExists(ArrayList<ClassDomain> classDomains) {
    if(classRepository.findIfOverlapExists(classDomains)) {
      throw new CustomException("Class overlaps with another class");
    }
  }

  public ArrayList<ClassDomain> getClassDetails(String className) {
    return classRepository.findByClassName(className.toLowerCase());
  }

  public ArrayList<ClassDomain> getAllClassDetails() {
    return classRepository.findAll();
  }

}
