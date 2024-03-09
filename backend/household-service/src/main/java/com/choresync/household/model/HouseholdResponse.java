package com.choresync.household.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseholdResponse {
  private String id;

  private String name;

  private Date createdAt;

  private Date updatedAt;
}