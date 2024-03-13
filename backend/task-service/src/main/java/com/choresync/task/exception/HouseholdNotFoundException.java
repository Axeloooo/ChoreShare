package com.choresync.task.exception;

public class HouseholdNotFoundException extends RuntimeException {
  public HouseholdNotFoundException(String message) {
    super(message);
  }
}
