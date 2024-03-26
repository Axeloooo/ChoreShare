package com.choresync.event.external.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
