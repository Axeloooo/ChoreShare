package com.choresync.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.choresync.task.external.exception.HouseholdNotFoundException;
import com.choresync.task.external.exception.UserNotFoundException;
import com.choresync.task.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TaskInvalidBodyException.class)
  public ResponseEntity<ErrorResponse> handleTaskCreationException(TaskInvalidBodyException exception) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TaskUnforbiddenActionException.class)
  public ResponseEntity<ErrorResponse> handleTaskUnforbiddenActionException(TaskUnforbiddenActionException exception) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(TaskInvalidParamException.class)
  public ResponseEntity<ErrorResponse> handleTaskInvalidParamException(TaskInvalidParamException exception) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HouseholdNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleHouseholdNotFoundException(HouseholdNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TaskInternalCommunicationException.class)
  public ResponseEntity<ErrorResponse> handleTaskInternalCommunicationException(
      TaskInternalCommunicationException exception) {
    return new ResponseEntity<>(
        ErrorResponse.builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
