package elearning.com.course_service.controller;

import elearning.com.course_service.model.*;
import elearning.com.course_service.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CourseController {

    private final CourseRepository courseRepository;
    private final RestTemplate restTemplate;


    // لإضافة كورس من قبل المدرب (معتمد false)
    @PostMapping("/add")
    @Retry(name = "examRetry", fallbackMethod = "fallbackForExam")
    @CircuitBreaker(name = "examCB", fallbackMethod = "fallbackForExam")
    @RateLimiter(name = "examRateLimiter", fallbackMethod = "fallbackForExam")
    public Course addCourse(@RequestBody Course course) {
        course.setApproved(false);
        Course savedCourse = courseRepository.save(course);

        // تجهيز البيانات لإرسالها إلى exam-service
        String examServiceUrl = "http://EXAM-SERVICE/api/exams/create";

        // نموذج بسيط لاختبار أولي
        Exam exam = new Exam();
        exam.setCourseId(savedCourse.getId());
        exam.setTitle("اختبار أولي للكورس: " + savedCourse.getTitle());
        exam.setPassingScore(30); // مثلاً 3 أسئلة صحيحة من 5

        try {
            ResponseEntity<Exam> response = restTemplate.postForEntity(examServiceUrl, exam, Exam.class);
            System.out.println("Initial Exam Created Successfuly");
        } catch (Exception e) {
            System.out.println("Failed Creating Initial Exam " + e.getMessage());
        }

        return savedCourse;
    }

    public Course fallbackForExam(Course course, Exception e) {
        System.out.println("Fallback activated due to: " + e.getMessage());
        course.setTitle(course.getTitle() + " [Exam Creation Failed]");
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
