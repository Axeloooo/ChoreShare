package com.choresync.auth.service;

public interface JwtService {
  void validateToken(final String token);

  String generateToken(String userName);
}
