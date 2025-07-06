package elearning.com.exam_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exam_results")
public class ExamResult {
    @Id
    private String id;
    private String studentId;
    private String examId;
    private int score;
    private String status; // PASSED, FAILED
}
