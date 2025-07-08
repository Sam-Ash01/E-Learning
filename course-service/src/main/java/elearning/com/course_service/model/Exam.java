package elearning.com.course_service.model;

import lombok.Data;
import java.util.List;

@Data
public class Exam {
    private String courseId;
    private String title;
    private int passingScore;
    private List<Question> questions;
}
