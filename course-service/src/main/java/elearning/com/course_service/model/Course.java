package elearning.com.course_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "courses")
public class Course {

    @Id
    private String id;
    private String title;
    private String description;
    private Double price;
    private String trainerId;   
    private boolean approved;   

}
