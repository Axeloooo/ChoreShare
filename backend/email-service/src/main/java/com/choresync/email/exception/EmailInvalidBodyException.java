package com.choresync.email.exception;

public class EmailInvalidBodyException extends RuntimeException {
  public EmailInvalidBodyException(String message) {
    super(message);
  }
}
