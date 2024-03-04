package com.choresync.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.choresync.auth.entity.Auth;
import com.choresync.auth.model.AuthRegisterRequest;
import com.choresync.auth.repository.AuthRepository;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthRepository authRepository;

  @Override
  public String registerUser(AuthRegisterRequest authRequest) {
    authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));

    Auth auth = Auth
        .builder()
        .firstName(authRequest.getFirstName())
        .lastName(authRequest.getLastName())
        .username(authRequest.getUsername())
        .email(authRequest.getEmail())
        .password(authRequest.getPassword())
        .build();

    Auth newAuth = authRepository.save(auth);

    return jwtService.generateToken(newAuth.getUsername());
  }

  @Override
  public String loginUser(String username) {
    return jwtService.generateToken(username);
  }

  @Override
  public void validateToken(String token) {
    jwtService.validateToken(token);
  }
}
