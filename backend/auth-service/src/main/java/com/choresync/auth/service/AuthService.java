package com.choresync.auth.service;

import org.springframework.web.client.RestClientException;

import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;

public interface AuthService {
  String extractErrorMessage(RestClientException e);

  String registerUser(AuthRegisterRequest authRequest);

  String loginUser(AuthLoginRequest authRequest);

  void validateToken(String token);
}
