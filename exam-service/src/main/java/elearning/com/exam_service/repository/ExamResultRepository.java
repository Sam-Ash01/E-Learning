package elearning.com.exam_service.repository;

import elearning.com.exam_service.model.ExamResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ExamResultRepository extends MongoRepository<ExamResult, String> {
    List<ExamResult> findByStudentId(String studentId);
    List<ExamResult> findByExamId(String examId);
}
