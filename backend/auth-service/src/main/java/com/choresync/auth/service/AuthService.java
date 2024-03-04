package com.choresync.auth.service;

import com.choresync.auth.model.AuthRegisterRequest;

public interface AuthService {
  String registerUser(AuthRegisterRequest authRequest);

  String loginUser(String string);

  void validateToken(String token);
}
