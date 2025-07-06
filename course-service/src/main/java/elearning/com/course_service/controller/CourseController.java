package elearning.com.course_service.controller;

import elearning.com.course_service.model.Course;
import elearning.com.course_service.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CourseController {

    private final CourseRepository courseRepository;

    // لإضافة كورس من قبل المدرب (معتمد false)
    @PostMapping("/add")
    public Course addCourse(@RequestBody Course course) {
        course.setApproved(false);
        return courseRepository.save(course);
    }

    // لإرجاع جميع الكورسات المعتمدة ليستعرضها المتعلم
    @GetMapping("/approved")
    public List<Course> getApprovedCourses() {
        return courseRepository.findByApprovedTrue();
    }

    // لإرجاع جميع كورسات المدرب حسب Id
    @GetMapping("/trainer/{trainerId}")
    public List<Course> getTrainerCourses(@PathVariable String trainerId) {
        return courseRepository.findByTrainerId(trainerId);
    }

    // موافقة الأدمن على الكورس
    @PutMapping("/approve/{courseId}")
    public Course approveCourse(@PathVariable String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setApproved(true);
        return courseRepository.save(course);
    }
}
