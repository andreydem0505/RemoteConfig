package andreydem0505.remoteconfig.config;

import andreydem0505.remoteconfig.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DynPropertyAlreadyExistsException.class)
    public ResponseEntity<String> handleException(DynPropertyAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
    }

    @ExceptionHandler(NoUserWithUsernameException.class)
    public ResponseEntity<String> handleException(NoUserWithUsernameException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(DynPropertyDataValidationException.class)
    public ResponseEntity<String> handleException(DynPropertyDataValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(DynPropertyNameValidationException.class)
    public ResponseEntity<String> handleException(DynPropertyNameValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(DynPropertyNotFoundException.class)
    public ResponseEntity<String> handleException(DynPropertyNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No dynamic property with this name was found");
    }
}
