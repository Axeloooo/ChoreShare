package com.choresync.task.exception;

public class TaskUnforbiddenActionException extends RuntimeException {
  public TaskUnforbiddenActionException(String message) {
    super(message);
  }
}
