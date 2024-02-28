package com.choresync.assignment.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponse {
  private String id;

  private String householdId;

  private Task task;

  private String userId;

  private Date createdAt;

  private Date updatedAt;

  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Task {
    private String id;

    private String name;

    private String description;

    private String status;

    private Date dueDate;

    private Date createdAt;

    private Date updatedAt;
  }
}
