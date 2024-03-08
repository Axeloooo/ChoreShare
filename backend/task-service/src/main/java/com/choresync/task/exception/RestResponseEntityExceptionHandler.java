package com.choresync.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

  @ExceptionHandler(TaskCreationException.class)
  public ResponseEntity<ErrorResponse> handleTaskCreationException(TaskCreationException exception) {
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
}
