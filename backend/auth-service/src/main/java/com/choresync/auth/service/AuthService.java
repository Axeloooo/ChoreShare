package com.choresync.auth.service;

import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;

public interface AuthService {
  String registerUser(AuthRegisterRequest authRequest);

  String loginUser(AuthLoginRequest authRequest);

  void validateToken(String token);
}
