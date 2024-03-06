package com.choresync.gateway.exception;

public class AuthMissingTokenException extends RuntimeException {
  public AuthMissingTokenException(String message) {
    super(message);
  }
}
