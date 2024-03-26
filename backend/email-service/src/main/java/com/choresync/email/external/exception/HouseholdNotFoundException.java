package com.choresync.email.external.exception;

public class HouseholdNotFoundException extends RuntimeException {
  public HouseholdNotFoundException(String message) {
    super(message);
  }
}
