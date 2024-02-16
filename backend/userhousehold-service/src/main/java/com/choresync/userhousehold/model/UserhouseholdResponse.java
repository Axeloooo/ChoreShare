package com.choresync.userhousehold.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserhouseholdResponse {
  private String id;

  private String userId;

  private String householdId;

  private Date createdAt;

  private Date updatedAt;
}
