package com.choresync.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.auth.exception.AuthInternalCommunicationException;
import com.choresync.auth.exception.AuthRequestBodyException;
import com.choresync.auth.external.request.UserRequest;
import com.choresync.auth.external.response.UserAuthResponse;
import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  @Override
  public String extractErrorMessage(RestClientException e) {
    String rawMessage = e.getMessage();

    try {
      String jsonSubstring = rawMessage.substring(rawMessage.indexOf("{"), rawMessage.lastIndexOf("}") + 1);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(jsonSubstring);

      if (rootNode.has("message")) {
        return rootNode.get("message").asText();
      }
    } catch (JsonProcessingException ex) {
      System.out.println("Error parsing JSON from exception message: " + ex.getMessage());
    } catch (StringIndexOutOfBoundsException ex) {
      System.out.println("Error extracting JSON substring from exception message: " + ex.getMessage());
    }
    return rawMessage;
  }

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
      UserAuthResponse userResponse = restTemplate.postForObject(
          "http://user-service/api/v1/user",
          userRequest,
          UserAuthResponse.class);
      return jwtService.generateToken(userResponse.getId());
    } catch (RestClientException e) {
      throw new AuthInternalCommunicationException(
          "Failed to create user. " + e.getMessage());
    }
  }

  @Override
  public String loginUser(AuthLoginRequest authRequest) {
    if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
      throw new AuthRequestBodyException("Invalid request body");
    }

    UserAuthResponse userResponse;

    try {
      userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/username/" + authRequest.getUsername(),
          UserAuthResponse.class);
    } catch (RestClientException e) {
      throw new AuthInternalCommunicationException(
          "Failed to login user. " + e.getMessage());
    }

    return jwtService.generateToken(userResponse.getId());
  }

  @Override
  public void validateToken(String token) {
    jwtService.validateToken(token);
  }
}
