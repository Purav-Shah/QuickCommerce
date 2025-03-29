package com.example.authservice.exception;

import com.example.authservice.dto.DTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CredentialsNotValidException.class)
    public ResponseEntity<DTO<String>> handleNoUserException(CredentialsNotValidException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(DTO.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .build());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<DTO<String>> handleUserAlreadyExists(UserAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(DTO.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .build());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DTO<String>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DTO.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .build());
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DTO<String>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new DTO<>(false, "Invalid email or password", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DTO<String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DTO.<String>builder()
                .success(false)
                .message("Something went wrong: " + ex.getMessage())
                .data(null)
                .build());
    }
}
