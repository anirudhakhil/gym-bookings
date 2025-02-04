package gym_bookings.demo.assembler;

import gym_bookings.demo.domain.ClassDomain;
import gym_bookings.demo.exceptions.CustomException;
import gym_bookings.demo.resource.ClassResource;
import gym_bookings.demo.util.ClassSequenceGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class ClassAssembler {

  private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

  public ArrayList<ClassDomain> toClassDomains(ClassResource classResource) {
    LocalDate startDate = LocalDate.parse(classResource.getStartDate(), dateFormatter);
    LocalDateTime startDateTIme = startDate.atTime(LocalTime.parse(classResource.getStartTime(), timeFormatter));
    if(startDateTIme.isBefore(LocalDateTime.now())) {
      throw new CustomException("Start date time cannot be in the past");
    }
    LocalDate endDate = LocalDate.parse(classResource.getEndDate(), dateFormatter);
    LocalDateTime endDateTime = endDate.atTime(LocalTime.parse(classResource.getStartTime(), timeFormatter));
    if(endDateTime.isBefore(startDateTIme)) {
      throw new CustomException("End date time cannot be before start date time");
    }
    LocalDateTime current = startDateTIme;

    ArrayList<ClassDomain> classDomains = new ArrayList<>();
    //Iterating day by day and creating class domain for day
    while(!current.isAfter(endDateTime)) {
      ClassDomain classDomain = getClassDomain(classResource, current);
      classDomains.add(classDomain);
      current = current.plusDays(1);
    }
    return classDomains;
  }

  private static ClassDomain getClassDomain(ClassResource classResource, LocalDateTime current) {
    ClassDomain classDomain = new ClassDomain();
    classDomain.setId(ClassSequenceGenerator.getNext());
    classDomain.setCapacity(classResource.getCapacity());

    //Since this is only used to store in DB, I'm directly setting it as lowercase here.
    // Ideally, we don't need to worry about case sensitivity in DB.
    classDomain.setName(classResource.getName().toLowerCase());
    classDomain.setClassStartDateTime(current);
    classDomain.setClassEndDateTime(current.plusMinutes(Integer.parseInt(classResource.getDuration())));
    return classDomain;
  }


}
