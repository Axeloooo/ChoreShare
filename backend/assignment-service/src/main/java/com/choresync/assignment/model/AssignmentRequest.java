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
public class AssignmentRequest {
  private Date assignmentDate;

  private Date completionDate;

  private String huseholdId;

  private String taskId;

  private String userId;
}
