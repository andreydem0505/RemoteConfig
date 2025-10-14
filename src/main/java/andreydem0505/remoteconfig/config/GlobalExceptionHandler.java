package andreydem0505.remoteconfig.config;

import andreydem0505.remoteconfig.exceptions.DynPropertyAlreadyExistsException;
import andreydem0505.remoteconfig.exceptions.NoUserWithUsernameException;
import andreydem0505.remoteconfig.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DynPropertyAlreadyExistsException.class)
    public ResponseEntity<String> handleDynPropertyAlreadyExistsException(DynPropertyAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
    }

    @ExceptionHandler(NoUserWithUsernameException.class)
    public ResponseEntity<String> handleNoUserWithUsernameException(NoUserWithUsernameException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
