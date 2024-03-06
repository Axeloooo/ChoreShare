package com.choresync.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.choresync.auth.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler({ AuthRequestBodyException.class })
  protected ResponseEntity<ErrorResponse> handleInvalidRequest(AuthRequestBodyException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .message(exception.getMessage())
            .build(),
        HttpStatus.BAD_REQUEST);
  }
}