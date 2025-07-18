package elearning.com.user_service.repository;

import elearning.com.user_service.model.Role;
import elearning.com.user_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}
