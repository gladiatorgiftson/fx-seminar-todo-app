package in.fxec.todo.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import in.fxec.todo.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** The two doors into the app: POST /api/auth/signup and POST /api/auth/login. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    record SignupRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @Size(min = 6, message = "Password needs at least 6 characters") String password) {}

    record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

    /** What we send back. Notice: no password, no hash. */
    record AuthResponse(Long id, String name, String email, String token) {
        static AuthResponse from(User u) {
            return new AuthResponse(u.getId(), u.getName(), u.getEmail(), u.getToken());
        }
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signup(@Valid @RequestBody SignupRequest req) {
        return AuthResponse.from(auth.signup(req.name(), req.email(), req.password()));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return AuthResponse.from(auth.login(req.email(), req.password()));
    }
}
