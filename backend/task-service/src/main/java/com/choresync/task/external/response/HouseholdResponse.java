package com.choresync.task.external.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdResponse {
  private String id;

  private String name;

  private Date createdAt;

  private Date updatedAt;

}
