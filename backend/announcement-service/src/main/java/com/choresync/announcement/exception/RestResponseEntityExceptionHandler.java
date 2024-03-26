package com.choresync.announcement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.choresync.announcement.external.exception.HouseholdNotFoundException;
import com.choresync.announcement.external.exception.UserNotFoundException;
import com.choresync.announcement.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler(AnnouncementNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskNotFound(AnnouncementNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AnnouncementInvalidBodyException.class)
  public ResponseEntity<ErrorResponse> handleTaskCreationError(AnnouncementInvalidBodyException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AnnouncementInvalidParamException.class)
  public ResponseEntity<ErrorResponse> handleTaskCreationError(AnnouncementInvalidParamException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AnnouncementInternalCommunicationException.class)
  public ResponseEntity<ErrorResponse> handleTaskCreationError(
      AnnouncementInternalCommunicationException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(HouseholdNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleHouseholdNotFound(HouseholdNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.NOT_FOUND);
  }
}
