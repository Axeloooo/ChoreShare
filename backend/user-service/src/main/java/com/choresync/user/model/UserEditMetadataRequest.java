package com.choresync.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditMetadataRequest {
  private String firstName;

  private String lastName;

  private String email;

  private String phone;
}