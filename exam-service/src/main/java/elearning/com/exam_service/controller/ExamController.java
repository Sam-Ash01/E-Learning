package elearning.com.exam_service.controller;

import elearning.com.exam_service.model.*;
import elearning.com.exam_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.Data;


@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ExamController {

    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;

    // ✅ إنشاء اختبار جديد لكورس (من قبل المدرب)
    @PostMapping("/create")
    public Exam createExam(@RequestBody Exam exam) {
        return examRepository.save(exam);
    }

    // ✅ جلب اختبار كورس
    @GetMapping("/course/{courseId}")
    public List<Exam> getExamByCourse(@PathVariable String courseId) {
        return examRepository.findByCourseId(courseId);
    }

    // ✅ تقديم اختبار (من قبل الطالب)
    @PostMapping("/submit")
    public ExamResult submitExam(@RequestBody ExamSubmission submission) {
        Exam exam = examRepository.findById(submission.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        int score = 0;
        for (int i = 0; i < exam.getQuestions().size(); i++) {
            if (exam.getQuestions().get(i).getCorrectAnswer()
                    .equalsIgnoreCase(submission.getAnswers().get(i))) {
                score += 10; // كل سؤال 10 درجات
            }
        }

        String status = (score >= exam.getPassingScore()) ? "PASSED" : "FAILED";

        ExamResult result = ExamResult.builder()
                .studentId(submission.getStudentId())
                .examId(submission.getExamId())
                .score(score)
                .status(status)
                .build();

        return examResultRepository.save(result);
    }

    // ✅ استعراض نتائج الطالب
    @GetMapping("/results/student/{studentId}")
    public List<ExamResult> getStudentResults(@PathVariable String studentId) {
        return examResultRepository.findByStudentId(studentId);
    }

    // ✅ استعراض نتائج اختبار معين
    @GetMapping("/results/exam/{examId}")
    public List<ExamResult> getExamResults(@PathVariable String examId) {
        return examResultRepository.findByExamId(examId);
    }

    @Data
    static class ExamSubmission {
        private String examId;
        private String studentId;
        private List<String> answers;
    }
}
