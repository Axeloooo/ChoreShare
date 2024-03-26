package com.choresync.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;
import com.choresync.auth.model.AuthTokenResponse;
import com.choresync.auth.model.AuthValidateResponse;
import com.choresync.auth.service.AuthService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  @Autowired
  private AuthService authService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping("/register")
  public ResponseEntity<AuthTokenResponse> postRegister(@RequestBody AuthRegisterRequest authRegisterRequest) {
    AuthTokenResponse authTokenResponse = authService.registerUser(authRegisterRequest);
    return new ResponseEntity<>(authTokenResponse, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthTokenResponse> postLogin(@RequestBody AuthLoginRequest authLoginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authLoginRequest.getUsername(), authLoginRequest.getPassword()));

    if (authentication.isAuthenticated()) {
      AuthTokenResponse authTokenResponse = authService.loginUser(authLoginRequest);
      return new ResponseEntity<>(authTokenResponse, HttpStatus.OK);
    } else {
      throw new RuntimeException("Invalid credentials");
    }
  }

  @GetMapping("/validate")
  public ResponseEntity<AuthValidateResponse> postValidate(@RequestParam("token") String token) {
    AuthValidateResponse authValidateResponse = authService.validateToken(token);
    return new ResponseEntity<>(authValidateResponse, HttpStatus.OK);
  }
}
