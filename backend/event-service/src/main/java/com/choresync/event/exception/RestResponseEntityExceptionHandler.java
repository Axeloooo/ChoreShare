package com.choresync.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.choresync.event.external.exception.HouseholdNotFoundException;
import com.choresync.event.external.exception.UserNotFoundException;
import com.choresync.event.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler(EventNotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleTaskNotFound(EventNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EventInvalidBodyException.class)
  protected ResponseEntity<ErrorResponse> handleTaskCreationError(EventInvalidBodyException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EventInternalCommunicationException.class)
  protected ResponseEntity<ErrorResponse> handleInternalCommunicationError(
      EventInternalCommunicationException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HouseholdNotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleHouseholdNotFound(HouseholdNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EventInvalidParamException.class)
  protected ResponseEntity<ErrorResponse> handleInvalidParam(EventInvalidParamException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }
}
