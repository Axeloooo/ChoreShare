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

  private Date assignmentDate;

  private Date completionDate;

  private String huseholdId;

  private String taskId;

  private String userId;

  private Date createdAt;

  private Date updatedAt;
}
