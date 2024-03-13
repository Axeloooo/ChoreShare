package com.choresync.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.choresync.user.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler(UserCreationException.class)
  public ResponseEntity<ErrorResponse> handleUserCreationException(UserCreationException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.CONFLICT);
  }
}
