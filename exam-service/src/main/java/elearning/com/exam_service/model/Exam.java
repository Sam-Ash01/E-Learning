package elearning.com.exam_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exams")
public class Exam {
    @Id
    private String id;
    private String courseId;
    private List<Question> questions;
    private int passingScore;
}
