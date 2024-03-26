package com.choresync.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.auth.exception.AuthInternalCommunicationException;
import com.choresync.auth.exception.AuthInvalidParamException;
import com.choresync.auth.exception.AuthRequestBodyException;
import com.choresync.auth.external.exception.UserNotFoundException;
import com.choresync.auth.external.request.UserRequest;
import com.choresync.auth.external.response.UserAuthResponse;
import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;
import com.choresync.auth.model.AuthTokenResponse;
import com.choresync.auth.model.AuthValidateResponse;
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

  /*
   * Extracts the error message from a RestClientException
   * 
   * @param e
   * 
   * @return String
   */
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

  /*
   * Call the user-service to create a new user
   * 
   * @param authRequest
   * 
   * @return AuthTokenResponse
   * 
   * @throws AuthRequestBodyException
   * 
   * @throws AuthInternalCommunicationException
   */
  @Override
  public AuthTokenResponse registerUser(AuthRegisterRequest authRequest) {
    if (authRequest.getFirstName().isBlank() || authRequest.getFirstName() == null
        || authRequest.getLastName().isBlank() || authRequest.getLastName() == null
        || authRequest.getUsername().isBlank() || authRequest.getUsername() == null
        || authRequest.getEmail().isBlank() || authRequest.getEmail() == null
        || authRequest.getPassword().isBlank() || authRequest.getPassword() == null
        || authRequest.getPhone().isBlank() || authRequest.getPhone() == null) {
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

      String token = jwtService.generateToken(userResponse.getId());
      return AuthTokenResponse
          .builder()
          .token(token)
          .build();
    } catch (RestClientException e) {
      throw new AuthInternalCommunicationException(extractErrorMessage(e));
    }
  }

  /*
   * Call the user-service to get the user information by username
   * 
   * @param authRequest
   * 
   * @return AuthTokenResponse
   * 
   * @throws AuthRequestBodyException
   * 
   * @throws UserNotFoundException
   * 
   * @throws AuthInvalidCredentialsException
   * 
   * @throws AuthInternalCommunicationException
   */
  @Override
  public AuthTokenResponse loginUser(AuthLoginRequest authRequest) {
    if (authRequest.getUsername().isBlank() || authRequest.getUsername() == null || authRequest.getPassword().isBlank()
        || authRequest.getPassword() == null) {
      throw new AuthRequestBodyException("Invalid request body");
    }

    UserAuthResponse userResponse;

    try {
      userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/username/" + authRequest.getUsername(),
          UserAuthResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("User not found");
      }
    } catch (RestClientException e) {
      throw new AuthInternalCommunicationException(extractErrorMessage(e));
    }

    String token = jwtService.generateToken(userResponse.getId());

    return AuthTokenResponse
        .builder()
        .token(token)
        .build();
  }

  /*
   * Validate the token
   * 
   * @param token
   * 
   * @return AuthValidateResponse
   * 
   * @throws AuthInvalidParamException
   */
  @Override
  public AuthValidateResponse validateToken(String token) {
    if (token.isBlank() || token.isEmpty()) {
      throw new AuthInvalidParamException("Invalid request query");
    }
    try {
      jwtService.validateToken(token);
      return AuthValidateResponse
          .builder()
          .isValid(true)
          .build();
    } catch (Exception e) {
      return AuthValidateResponse
          .builder()
          .isValid(false)
          .build();
    }
  }

}
