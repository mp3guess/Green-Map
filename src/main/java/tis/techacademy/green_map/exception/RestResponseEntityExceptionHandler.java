package tis.techacademy.green_map.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tis.techacademy.green_map.exception.authentication.ExpiredTokenException;
import tis.techacademy.green_map.exception.authentication.ForgotPasswordCouldNotSendEmailException;
import tis.techacademy.green_map.exception.authentication.InvalidTokenException;
import tis.techacademy.green_map.exception.authentication.SetPasswordException;
import tis.techacademy.green_map.exception.poi.PoiCreationException;
import tis.techacademy.green_map.exception.poi.PoiNotFoundException;
import tis.techacademy.green_map.exception.user.ChangeRoleException;
import tis.techacademy.green_map.exception.user.UserAlreadyExistsException;
import tis.techacademy.green_map.exception.user.UserNotFoundException;
import tis.techacademy.green_map.exception.user.UserRetrievalException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    //General exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        String errorMessage = "You do not have permission to access this resource.";
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler({UsernameNotFoundException.class, UserNotFoundException.class, PoiNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage() != null ? ex.getMessage() : "Resource not found.");
    }

    //Authentication exceptions
    @ExceptionHandler({BadCredentialsException.class, ExpiredTokenException.class, InvalidTokenException.class})
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage() != null ? ex.getMessage() : "Authentication error.");
    }

    //User exceptions
    @ExceptionHandler({ForgotPasswordCouldNotSendEmailException.class, SetPasswordException.class, UserRetrievalException.class, ChangeRoleException.class, PoiCreationException.class})
    public ResponseEntity<Object> handleInternalServerError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage() != null ? ex.getMessage() : "An error occurred while processing the request.");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        String errorMessage = "User with this email already exists.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }
}
