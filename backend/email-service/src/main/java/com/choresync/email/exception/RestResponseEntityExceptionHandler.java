package com.choresync.email.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.choresync.email.external.exception.HouseholdNotFoundException;
import com.choresync.email.model.ErrorResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(EmailInternalCommunicationException.class)
	public ResponseEntity<ErrorResponse> handleEmailInternalCommunicationException(
			EmailInternalCommunicationException exception) {
		return new ResponseEntity<>(
				ErrorResponse
						.builder()
						.message(exception.getMessage())
						.build(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HouseholdNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleHouseholdNotFoundException(
			HouseholdNotFoundException exception) {
		return new ResponseEntity<>(
				ErrorResponse
						.builder()
						.message(exception.getMessage())
						.build(),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EmailInvalidBodyException.class)
	public ResponseEntity<ErrorResponse> handleEmailInvalidBodyException(
			EmailInvalidBodyException exception) {
		return new ResponseEntity<>(
				ErrorResponse
						.builder()
						.message(exception.getMessage())
						.build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmailSendException.class)
	public ResponseEntity<ErrorResponse> handleEmailSendException(
			EmailSendException exception) {
		return new ResponseEntity<>(
				ErrorResponse
						.builder()
						.message(exception.getMessage())
						.build(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
