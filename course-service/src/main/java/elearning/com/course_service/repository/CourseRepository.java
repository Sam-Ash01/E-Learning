package elearning.com.course_service.repository;

import elearning.com.course_service.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findByTrainerId(String trainerId);
    List<Course> findByApprovedTrue();
}
