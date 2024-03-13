package com.choresync.task.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
  private String id;

  private String title;

  private String householdId;

  private String description;

  private String status;

  private String frequency;

  private String tag;

  private String userId;

  private Date createdAt;

  private Date updatedAt;
}
