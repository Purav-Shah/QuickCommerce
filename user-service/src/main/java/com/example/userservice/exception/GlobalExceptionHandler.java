package com.example.userservice.exception;

import com.example.userservice.dto.DTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoUsersFoundException.class)
    public ResponseEntity<DTO<String>> handleNoUserException(NoUsersFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DTO<String>(false,ex.getMessage(),null));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<DTO<String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new DTO<String>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DTO<String>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DTO<String>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DTO<String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DTO<String>(false,"Something went wrong: " + ex.getMessage(),null));
    }
}
