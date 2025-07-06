package elearning.com.exam_service.repository;

import elearning.com.exam_service.model.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ExamRepository extends MongoRepository<Exam, String> {
    List<Exam> findByCourseId(String courseId);
}
