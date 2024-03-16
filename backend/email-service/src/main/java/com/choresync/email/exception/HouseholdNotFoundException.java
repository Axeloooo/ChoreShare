package com.choresync.email.exception;

public class HouseholdNotFoundException extends RuntimeException {
  public HouseholdNotFoundException(String message) {
    super(message);
  }
}
