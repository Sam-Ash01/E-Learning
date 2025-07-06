package elearning.com.enrollment_service.controller;

import elearning.com.enrollment_service.model.Enrollment;
import elearning.com.enrollment_service.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;

    // ✅ اشتراك المستخدم في كورس بعد الدفع
    @PostMapping("/enroll")
    public Enrollment enrollInCourse(@RequestBody Enrollment enrollment) {
        enrollment.setStatus("PAID");
        return enrollmentRepository.save(enrollment);
    }

    // ✅ جلب جميع الدورات التي اشترك بها المستخدم
    @GetMapping("/student/{studentId}")
    public List<Enrollment> getStudentEnrollments(@PathVariable String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    // ✅ جلب جميع الطلاب المشتركين في كورس
    @GetMapping("/course/{courseId}")
    public List<Enrollment> getCourseEnrollments(@PathVariable String courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }
}
