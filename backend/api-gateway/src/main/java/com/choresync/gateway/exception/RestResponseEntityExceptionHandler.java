package com.choresync.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import com.choresync.gateway.Model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	public RestResponseEntityExceptionHandler() {
		super();
	}

	@ExceptionHandler(AuthMissingTokenException.class)
	public ResponseEntity<ErrorResponse> handleAuthMissingTokenException(AuthMissingTokenException exception) {
		return new ResponseEntity<>(
				ErrorResponse
						.builder()
						.message(exception.getMessage())
						.build(),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AuthUnauthorizedAccessException.class)
	public ResponseEntity<ErrorResponse> handleAuthUnauthorizedAccessException(
			AuthUnauthorizedAccessException exception) {
		return new ResponseEntity<>(
				ErrorResponse
						.builder()
						.message(exception.getMessage())
						.build(),
				HttpStatus.FORBIDDEN);
	}
}
