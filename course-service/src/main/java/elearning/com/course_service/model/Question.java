package elearning.com.course_service.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String questionText;
    private String correctAnswer;
}
