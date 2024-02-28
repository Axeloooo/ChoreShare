package com.choresync.gateway.model;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
  private String id;

  private String accessToken;

  private String refreshToken;

  private long expiresAt;

  private Collection<String> authorityList;
}
