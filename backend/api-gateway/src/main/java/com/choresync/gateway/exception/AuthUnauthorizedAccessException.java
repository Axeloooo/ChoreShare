package com.choresync.gateway.exception;

public class AuthUnauthorizedAccessException extends RuntimeException {
  public AuthUnauthorizedAccessException(String message) {
    super(message);
  }
}
