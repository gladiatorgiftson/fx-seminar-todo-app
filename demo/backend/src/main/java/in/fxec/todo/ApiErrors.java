package in.fxec.todo;

import java.util.Map;
import in.fxec.todo.auth.AuthService.NotLoggedInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Turns our exceptions into clear JSON errors with the right status code. */
@RestControllerAdvice
public class ApiErrors {

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<Map<String, String>> badRequest(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(NotLoggedInException.class)
    ResponseEntity<Map<String, String>> notLoggedIn(NotLoggedInException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> invalid(MethodArgumentNotValidException e) {
        var first = e.getBindingResult().getFieldErrors().get(0);
        return ResponseEntity.badRequest()
            .body(Map.of("error", first.getField() + ": " + first.getDefaultMessage()));
    }
}
