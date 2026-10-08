package in.fxec.todo.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring writes the SQL for these from the method names. */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
}
