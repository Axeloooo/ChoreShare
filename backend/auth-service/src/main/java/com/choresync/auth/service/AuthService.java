package com.choresync.auth.service;

import org.springframework.web.client.RestClientException;

import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;
import com.choresync.auth.model.AuthTokenResponse;
import com.choresync.auth.model.AuthValidateResponse;

public interface AuthService {
  String extractErrorMessage(RestClientException e);

  AuthTokenResponse registerUser(AuthRegisterRequest authRequest);

  AuthTokenResponse loginUser(AuthLoginRequest authRequest);

  AuthValidateResponse validateToken(String token);
}
