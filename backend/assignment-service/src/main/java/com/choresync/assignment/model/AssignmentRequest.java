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

  private String name;

  private String description;

  private Date dueDate;

  private String householdId;

  private String userId;
}