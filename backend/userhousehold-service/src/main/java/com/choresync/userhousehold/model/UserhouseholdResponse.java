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

  private Household household;

  private Date createdAt;

  private Date updatedAt;

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Household {
    private String id;

    private String name;

    private Date createdAt;

    private Date updatedAt;
  }
}
