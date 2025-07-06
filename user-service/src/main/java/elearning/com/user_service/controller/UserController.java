package elearning.com.user_service.controller;

import elearning.com.user_service.model.Role;
import elearning.com.user_service.model.User;
import elearning.com.user_service.repository.UserRepository;
import elearning.com.user_service.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    // ✅ تسجيل مستخدم جديد (متعلم)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Email already exists"));
        }

        user.setId(null);
        userRepository.save(user);
        return ResponseEntity.ok(new SuccessResponse("User registered successfully"));
    }

    // ✅ تسجيل الدخول
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            User user = userOpt.get();

            // إنشاء توكن JWT
            String token = jwtUtils.generateJwtToken(user.getEmail(), user.getRole());

            // إعادة التوكن مع بيانات المستخدم (اختياري)
            LoginResponse response = new LoginResponse(token, user.getEmail(), user.getRole());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials"));
    }

    // ✅ إضافة مدرب (يتطلب صلاحية أدمن)
    @PostMapping("/add-trainer")
    public ResponseEntity<?> addTrainer(
            @RequestBody @Valid TrainerRegistrationDto trainerDto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        if (!jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid or expired token"));
        }

        // التحقق من أن المستخدم المرسل له صلاحية ADMIN
        Role userRole = jwtUtils.getRoleFromJwtToken(token);
        if (userRole != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Only admins can add trainers"));
        }

        if (userRepository.findByEmail(trainerDto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email already exists"));
        }

        User trainer = User.builder()
                .username(trainerDto.getUsername())
                .email(trainerDto.getEmail())
                .password(passwordEncoder.encode(trainerDto.getPassword()))
                .role(Role.TRAINER)
                .build();

        userRepository.save(trainer);

        return ResponseEntity.ok()
                .body(new SuccessResponse("Trainer added successfully"));
    }

    // ✅ جلب المستخدمين حسب الدور
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userRepository.findByRole(role));
    }

    // ✅ جلب مستخدم بواسطة ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.ok(user.get());
    }

    // ✅ حذف مستخدم
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok(new SuccessResponse("User deleted"));
    }

    // ✅ كلاس داخلي لطلب تسجيل الدخول
    @Data
    static class LoginRequest {
        private String email;
        private String password;
    }

    // ✅ رسائل استجابة
    @Data
    @AllArgsConstructor
    static class SuccessResponse {
        private String message;
    }

    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        private String message;
    }

    // ✅ إضافة كلاس داخلي لاستجابة تسجيل الدخول (بما في ذلك التوكن)
    @Data
    @AllArgsConstructor
    static class LoginResponse {
        private String token;
        private String email;
        private Role role;
    }

    // DTO مخصص لتسجيل المدربين
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TrainerRegistrationDto {
        @NotBlank
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;
    }
}
