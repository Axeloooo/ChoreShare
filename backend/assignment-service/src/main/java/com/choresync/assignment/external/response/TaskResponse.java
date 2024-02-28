package com.choresync.assignment.external.response;

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

  private String name;

  private String description;

  private String status;

  private Date dueDate;

  private Date createdAt;

  private Date updatedAt;
}
