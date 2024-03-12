package com.choresync.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
  private String title;

  private String householdId;

  private String description;

  private String status;

  private String frequency;

  private String tag;

  private String userId;
}