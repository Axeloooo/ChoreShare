package com.choresync.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import io.jsonwebtoken.JwtException;

public class AuthServiceImplTest {
  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private AuthServiceImpl authService;

  private AuthRegisterRequest authRegisterRequest;
  private AuthLoginRequest authLoginRequest;
  private UserRequest userRequest;
  private UserAuthResponse userResponse;
  private AuthRegisterRequest invalidAuthRegisterRequest;
  private AuthLoginRequest invalidAuthLoginRequest;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    invalidAuthRegisterRequest = AuthRegisterRequest
        .builder()
        .build();

    invalidAuthLoginRequest = AuthLoginRequest
        .builder()
        .build();

    authRegisterRequest = AuthRegisterRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("john.doe@example.com")
        .password("password")
        .phone("1234567890")
        .build();

    authLoginRequest = AuthLoginRequest.builder()
        .username("johndoe")
        .password("password")
        .build();

    userRequest = UserRequest.builder()
        .firstName(authRegisterRequest.getFirstName())
        .lastName(authRegisterRequest.getLastName())
        .username(authRegisterRequest.getUsername())
        .email(authRegisterRequest.getEmail())
        .password(passwordEncoder.encode(authRegisterRequest.getPassword()))
        .phone(authRegisterRequest.getPhone())
        .build();

    userResponse = UserAuthResponse
        .builder()
        .id("1")
        .username(authRegisterRequest.getUsername())
        .password(userRequest.getPassword())
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    reset(passwordEncoder);
    reset(restTemplate);
  }

  @Description("POST /api/v1/auth/register - Test register user with invalid body")
  @Test
  public void testRegisterUserWithInvalidBody() {
    assertThrows(AuthRequestBodyException.class, () -> authService.registerUser(invalidAuthRegisterRequest));
  }

  @Description("POST /api/v1/auth/register - Test register user with internal communication exception")
  @Test
  public void testRegisterUserWithInternalCommunicationException() {
    when(passwordEncoder.encode(authRegisterRequest.getPassword())).thenReturn(userRequest.getPassword());
    when(restTemplate.postForObject("http://user-service/api/v1/user", userRequest, UserAuthResponse.class))
        .thenThrow(new RestClientException("Internal error"));

    assertThrows(AuthInternalCommunicationException.class, () -> authService.registerUser(authRegisterRequest));
  }

  @Description("POST /api/v1/auth/register - Test register user success")
  @Test
  public void testRegisterUserSuccess() {
    when(passwordEncoder.encode(authRegisterRequest.getPassword())).thenReturn(userRequest.getPassword());
    when(restTemplate.postForObject("http://user-service/api/v1/user", userRequest, UserAuthResponse.class))
        .thenReturn(userResponse);
    when(jwtService.generateToken(userResponse.getId())).thenReturn("token");

    AuthTokenResponse response = authService.registerUser(authRegisterRequest);

    assertNotNull(response);
    assertEquals("token", response.getToken());

    verify(passwordEncoder, times(1)).encode(anyString());
    verify(restTemplate, times(1)).postForObject("http://user-service/api/v1/user", userRequest,
        UserAuthResponse.class);
    verify(jwtService, times(1)).generateToken(userResponse.getId());
  }

  @Description("POST /api/v1/auth/login - Test login user with invalid body")
  @Test
  public void testLoginUserWithInvalidBody() {
    assertThrows(AuthRequestBodyException.class, () -> authService.loginUser(invalidAuthLoginRequest));
  }

  @Description("POST /api/v1/auth/login - Test login user with user not found")
  @Test
  public void testLoginUserWithUserNotFound() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/username/" + authLoginRequest.getUsername(),
        UserAuthResponse.class)).thenReturn(null);

    assertThrows(UserNotFoundException.class, () -> authService.loginUser(authLoginRequest));
  }

  @Description("POST /api/v1/auth/login - Test login user with internal communication exception")
  @Test
  public void testLoginUserWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/username/" + authLoginRequest.getUsername(),
        UserAuthResponse.class)).thenThrow(new RestClientException("Internal error"));

    assertThrows(AuthInternalCommunicationException.class, () -> authService.loginUser(authLoginRequest));
  }

  @Description("POST /api/v1/auth/login - Test login user success")
  @Test
  public void testLoginUserSuccess() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/username/" + authLoginRequest.getUsername(),
        UserAuthResponse.class)).thenReturn(userResponse);
    when(jwtService.generateToken(userResponse.getId())).thenReturn("token");

    AuthTokenResponse response = authService.loginUser(authLoginRequest);

    assertNotNull(response);
    assertEquals("token", response.getToken());

    verify(restTemplate, times(1)).getForObject(
        "http://user-service/api/v1/user/username/" + authLoginRequest.getUsername(),
        UserAuthResponse.class);

    verify(jwtService, times(1)).generateToken(userResponse.getId());
  }

  @Description("GET /api/v1/auth/validate - Test validate token with invalid param")
  @Test
  public void testValidateTokenWithInvalidParam() {
    assertThrows(AuthInvalidParamException.class, () -> authService.validateToken(null));
  }

  @Description("GET /api/v1/auth/validate - Test validate token with invalid token")
  @Test
  public void testValidateTokenWithInvalidToken() {
    doThrow(new JwtException("Invalid token")).when(jwtService).validateToken("invalidToken");

    AuthValidateResponse authValidateResponse = authService.validateToken("invalidToken");

    assertNotNull(authValidateResponse);
    assertFalse(authValidateResponse.getIsValid());
  }

  @Description("GET /api/v1/auth/validate - Test validate token success")
  @Test
  public void testValidateTokenSuccess() {
    doNothing().when(jwtService).validateToken("validToken");

    AuthValidateResponse authValidateResponse = authService.validateToken("validToken");

    assertNotNull(authValidateResponse);
    assertTrue(authValidateResponse.getIsValid());

    verify(jwtService, times(1)).validateToken("validToken");
  }
}
