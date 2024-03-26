package com.choresync.event.external.exception;

public class HouseholdNotFoundException extends RuntimeException {
  public HouseholdNotFoundException(String message) {
    super(message);
  }
}
