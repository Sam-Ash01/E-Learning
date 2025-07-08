package elearning.com.enrollment_service.repository;

import elearning.com.enrollment_service.model.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    List<Enrollment> findByStudentId(String studentId);
    List<Enrollment> findByCourseId(String courseId);
    boolean existsByStudentIdAndCourseId(String studentId, String courseId);

}
