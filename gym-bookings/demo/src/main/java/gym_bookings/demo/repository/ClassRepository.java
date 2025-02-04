package gym_bookings.demo.repository;

import gym_bookings.demo.domain.ClassDomain;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
//@Repository
//ALL OF THESE METHODS WOULD BE REPLACED BY ACTUAL DB QUERIES - spring data jpa, jpql queries etc based on requirement
public class ClassRepository {

  ArrayList<ClassDomain> data = new ArrayList<>();

  //Thought of this apporach to store in a hash map where key would be the date and it's value is the list of classes that the date has
  //HashMap<LocalDate, ArrayList<ClassDomain>> dataSet = new HashMap<>();

  public boolean findIfOverlapExists(ArrayList<ClassDomain> inputClassDomains) {
    for(ClassDomain classDb : data) {
      for(ClassDomain classDomain : inputClassDomains) {
        if(classExistsOnSameDay(classDomain, classDb)
                && (sameClassExists(classDomain, classDb) ||
                overLapExistsDuringTheTime(classDomain, classDb))) {
          return true;
        }
      }
    }
    return false;

    //In general I would have used this way of checking overlaps if it is guaranteed that
    //The given time frame will constitute the same day. Since we are not unsure of that
    // I have iterated for every day in the input vs the data stored in DB
    /*
    for(ClassDomain classDb : data) {
      if(classExistsOnSameDay(classDomain, classDb)
              && (sameClassExists(classDomain, classDb) ||
              overLapExistsDuringTheTime(classDomain, classDb))) {
        return true;
      }
    }
    return false;
    */

  }

  public boolean saveOrUpdate(ClassDomain classDomain) {
    int index = -1;
    for(ClassDomain classDb : data) {
      if(classDomain.getId().equals(classDb.getId())) {
        index = data.indexOf(classDb);
        break;
      }
    }
    if (index != -1) {
      data.remove(index);
    }
    return data.add(classDomain);
  }

  public boolean saveAll(ArrayList<ClassDomain> classDomains) {
    return data.addAll(classDomains);
  }

  public ArrayList<ClassDomain> findByClassName(String className) {
    ArrayList<ClassDomain> list = new ArrayList<>();
    for(ClassDomain classDomain : data) {
      if(classDomain.getName().equals(className)) {
        list.add(classDomain);
      }
    }
    return list;
  }

  public ClassDomain findByClassNameAndDate(String className, LocalDate localDate) {
    for(ClassDomain classDomain : data) {
      if(classDomain.getName().equals(className)
              && classDomain.getClassStartDateTime().toLocalDate().isEqual(localDate)) {
        return classDomain;
      }
    }
    return null;
  }

  private static boolean classExistsOnSameDay(ClassDomain inputClassDomain, ClassDomain classDb) {
    return classDb.getClassStartDateTime().toLocalDate().isEqual(inputClassDomain.getClassStartDateTime().toLocalDate())
            || classDb.getClassEndDateTime().toLocalDate().isEqual(inputClassDomain.getClassStartDateTime().toLocalDate());
  }

  private static boolean sameClassExists(ClassDomain inputClassDomain, ClassDomain classDb) {
    return classDb.getName().equals(inputClassDomain.getName());
  }

  private static boolean overLapExistsDuringTheTime(ClassDomain inputClassDomain, ClassDomain classDb) {
    return classDb.getClassStartDateTime().isBefore(inputClassDomain.getClassEndDateTime())
            && classDb.getClassEndDateTime().isAfter(inputClassDomain.getClassStartDateTime());
  }

  public ArrayList<ClassDomain> findAll() {
    return data;
  }

  public ClassDomain findByClassId(Long classId) {
    for (ClassDomain classDomain : data) {
      if (classDomain.getId().equals(classId)) {
        return classDomain;
      }
    }
    return null;
  }
}
