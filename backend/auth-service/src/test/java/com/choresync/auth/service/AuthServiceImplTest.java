package com.choresync.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.web.client.RestTemplate;

import com.choresync.auth.exception.AuthInternalCommunicationException;
import com.choresync.auth.exception.AuthRequestBodyException;
import com.choresync.auth.external.request.UserRequest;
import com.choresync.auth.external.response.UserAuthResponse;
import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;

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

  private final String token = "jwtToken";

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    authRegisterRequest = AuthRegisterRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("john.doe@example.com")
        .password("password")
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

  // @Description("POST /api/v1/auth/register - Test register a new user")
  // @Test
  // public void testRegisterUser() {
  // when(passwordEncoder.encode(any(String.class)))
  // .thenReturn(userResponse.getPassword());

  // when(restTemplate.postForObject(
  // "http://user-service/api/v1/user",
  // userRequest,
  // UserAuthResponse.class))
  // .thenReturn(userResponse);

  // when(jwtService.generateToken(any(String.class)))
  // .thenReturn(token);

  // String result = authService.registerUser(authRegisterRequest);

  // assertNotNull(result);
  // assertEquals(token, result);

  // verify(passwordEncoder, times(1)).encode(any(String.class));
  // verify(restTemplate, times(1)).postForObject(anyString(), any(), any());
  // verify(jwtService, times(1)).generateToken(any(String.class));
  // }

  @Description("POST /api/v1/auth/register - Test AuthInternalCommunicationException when communication with user-service fails")
  @Test
  public void testRegisterUserCommunicationError() {
    when(passwordEncoder.encode(any(String.class)))
        .thenReturn(userResponse.getPassword());
    when(restTemplate.postForObject(
        "http://user-service/api/v1/user",
        userRequest,
        UserAuthResponse.class))
        .thenThrow(new RuntimeException("Invalid request body"));

    assertThrows(AuthInternalCommunicationException.class, () -> {
      authService.registerUser(authRegisterRequest);
    });

    verify(passwordEncoder, times(1)).encode(any(String.class));
    verify(restTemplate, times(1)).postForObject(
        "http://user-service/api/v1/user",
        userRequest,
        UserAuthResponse.class);
    verify(jwtService, times(0)).generateToken(any(String.class));
  }

  @Description("POST /api/v1/auth/register - Test AuthRequestBodyException when request body is invalid")
  @Test
  public void testRegisterUserInvalidRequest() {
    AuthRegisterRequest invalidRequest = AuthRegisterRequest.builder().build();

    assertThrows(AuthRequestBodyException.class, () -> {
      authService.registerUser(invalidRequest);
    });

    verify(passwordEncoder, times(0)).encode(any(String.class));
    verify(restTemplate, times(0)).postForObject(
        "http://user-service/api/v1/user",
        userRequest,
        UserAuthResponse.class);
    verify(jwtService, times(0)).generateToken(any(String.class));
  }

  // @Description("POST /api/v1/auth/login - Test login user")
  // @Test
  // public void testLoginUser() {
  // when(jwtService.generateToken(any(String.class))).thenReturn(token);

  // String result = authService.loginUser(authLoginRequest);

  // assertNotNull(result);
  // assertEquals(token, result);

  // verify(jwtService, times(1)).generateToken(any(String.class));
  // }

  @Description("POST /api/v1/auth/login - Test AuthRequestBodyException when request body is invalid")
  @Test
  public void testLoginUserInvalidRequest() {
    AuthLoginRequest invalidRequest = AuthLoginRequest.builder().build();

    assertThrows(AuthRequestBodyException.class, () -> {
      authService.loginUser(invalidRequest);
    });

    verify(jwtService, times(0)).generateToken(any(String.class));
  }

  @Description("POST /api/v1/auth/validate - Test validate token")
  @Test
  public void testValidateToken() {
    authService.validateToken(token);

    verify(jwtService, times(1)).validateToken(token);
  }
}
