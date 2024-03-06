package com.choresync.auth.service;

import java.security.Key;
import java.util.Map;

public interface JwtService {
  void validateToken(final String token);

  String generateToken(String userName);

  String createToken(Map<String, Object> claims, String userName);

  Key getSignKey();
}
