package com.choresync.email.exception;

public class EmailSendException extends RuntimeException {
  public EmailSendException(String message) {
    super(message);
  }
}
