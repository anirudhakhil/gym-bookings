package gym_bookings.demo.controller;

import gym_bookings.demo.assembler.ClassAssembler;
import gym_bookings.demo.domain.ClassDomain;
import gym_bookings.demo.resource.ClassResource;
import gym_bookings.demo.service.ClassService;
import gym_bookings.demo.validation.ClassValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("api/v1/classes")
public class ClassController {


  @Autowired
  private ClassAssembler classAssembler;

  @Autowired
  private ClassService classService;

  @Autowired
  private ClassValidator classValidator;

  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<ClassResource> createClass(@Valid @RequestBody ClassResource classResource) {
    classValidator.validate(classResource);
    classService.createClass(classResource);
    //returning back the same response as the request after successful creation of a class
    return ResponseEntity.ok(classResource);
  }

  //Using for internal testing purpose
  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path="/{className}")
    public ResponseEntity<ArrayList<ClassDomain>> getClass(@PathVariable String className) {
    ArrayList<ClassDomain> classDetails = classService.getClassDetails(className);
    return ResponseEntity.ok(classDetails);
    }

  //Using for internal testing purpose
  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<ArrayList<ClassDomain>> getAllClass() {
    ArrayList<ClassDomain> classDetails = classService.getAllClassDetails();
    return ResponseEntity.ok(classDetails);
  }


}
