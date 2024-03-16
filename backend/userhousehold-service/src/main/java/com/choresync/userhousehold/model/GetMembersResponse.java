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
public class GetMembersResponse {

  private User user;

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

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class User {
    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private Date createdAt;

    private Date updatedAt;
  }
}
