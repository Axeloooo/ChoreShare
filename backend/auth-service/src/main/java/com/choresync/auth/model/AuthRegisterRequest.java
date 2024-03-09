package com.choresync.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRequest {
  private String firstName;

  private String lastName;

  private String username;

  private String email;

  private String password;

  private String phone;
}
