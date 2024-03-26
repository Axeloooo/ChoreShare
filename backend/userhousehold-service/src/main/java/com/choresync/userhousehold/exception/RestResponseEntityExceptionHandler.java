package com.choresync.userhousehold.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.choresync.userhousehold.external.exception.HouseholdCreationException;
import com.choresync.userhousehold.external.exception.HouseholdNotFoundException;
import com.choresync.userhousehold.external.exception.UserNotFoundException;
import com.choresync.userhousehold.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler(HouseholdCreationException.class)
  public ResponseEntity<ErrorResponse> handleHouseholdCreationException(HouseholdCreationException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UserhouseholdInvalidBodyException.class)
  public ResponseEntity<ErrorResponse> handleuserhouseholdInvalidBodyException(
      UserhouseholdInvalidBodyException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HouseholdNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleHouseholdNotFoundException(HouseholdNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserhouseholdNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserhouseholdNotFoundException(UserhouseholdNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserhouseholdInvalidParamException.class)
  public ResponseEntity<ErrorResponse> handleUserhouseholdInvalidParamException(
      UserhouseholdInvalidParamException exception) {
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
}
