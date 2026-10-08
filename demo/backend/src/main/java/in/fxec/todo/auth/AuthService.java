package in.fxec.todo.auth;

import java.util.UUID;
import in.fxec.todo.user.User;
import in.fxec.todo.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/** The rules of signing up and logging in live here - on the server, where nobody can tamper with them. */
@Service
public class AuthService {

    private final UserRepository users;
    private final BCryptPasswordEncoder hasher = new BCryptPasswordEncoder();

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public User signup(String name, String email, String password) {
        if (users.findByEmail(email).isPresent()) {
            throw new IllegalStateException("That email is already registered");
        }
        var user = new User(name, email, hasher.encode(password));
        user.setToken(UUID.randomUUID().toString());
        return users.save(user);
    }

    public User login(String email, String password) {
        var user = users.findByEmail(email)
            .orElseThrow(() -> new IllegalStateException("Wrong email or password"));
        if (!hasher.matches(password, user.getPasswordHash())) {
            throw new IllegalStateException("Wrong email or password");
        }
        user.setToken(UUID.randomUUID().toString());
        return users.save(user);
    }

    /** Turns the "Authorization: Bearer <token>" header into the logged-in user, or fails. */
    public User currentUser(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotLoggedInException();
        }
        return users.findByToken(authorizationHeader.substring(7))
            .orElseThrow(NotLoggedInException::new);
    }

    public static class NotLoggedInException extends RuntimeException {
        public NotLoggedInException() { super("Please log in first"); }
    }
}
