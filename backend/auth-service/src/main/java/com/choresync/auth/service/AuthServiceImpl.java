package com.choresync.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.choresync.auth.exception.AuthInternalCommunicationException;
import com.choresync.auth.exception.AuthRequestBodyException;
import com.choresync.auth.external.request.UserRequest;
import com.choresync.auth.external.response.UserResponse;
import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  @Override
  public String registerUser(AuthRegisterRequest authRequest) {
    if (authRequest.getFirstName() == null || authRequest.getLastName() == null || authRequest.getUsername() == null
        || authRequest.getEmail() == null || authRequest.getPassword() == null) {
      throw new AuthRequestBodyException("Invalid request body");
    }

    authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));

    UserRequest userRequest = UserRequest
        .builder()
        .firstName(authRequest.getFirstName())
        .lastName(authRequest.getLastName())
        .username(authRequest.getUsername())
        .email(authRequest.getEmail())
        .password(authRequest.getPassword())
        .phone(authRequest.getPhone())
        .build();

    try {
      UserResponse userResponse = restTemplate.postForObject(
          "http://user-service/api/v1/user",
          userRequest,
          UserResponse.class);
      return jwtService.generateToken(userResponse.getUsername());
    } catch (Exception e) {
      throw new AuthInternalCommunicationException(
          "Internal communication error with user-service - " + e.getMessage());
    }
  }

  @Override
  public String loginUser(AuthLoginRequest authRequest) {
    if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
      throw new AuthRequestBodyException("Invalid request body");
    }
    return jwtService.generateToken(authRequest.getUsername());
  }

  @Override
  public void validateToken(String token) {
    jwtService.validateToken(token);
  }
}
