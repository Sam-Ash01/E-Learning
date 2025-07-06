package elearning.com.enrollment_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "enrollments")
public class Enrollment {
    @Id
    private String id;
    private String studentId;
    private String courseId;
    private String status; // PAID, COMPLETED, PENDING
}
