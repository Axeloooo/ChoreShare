package com.choresync.task.external.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private String id;

  private String firstName;

  private String lastName;

  private String username;

  private String email;

  private String phone;

  private int streak;

  private int missedChores;

  private Date createdAt;

  private Date updatedAt;
}
