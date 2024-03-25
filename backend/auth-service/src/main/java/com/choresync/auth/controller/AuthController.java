package com.choresync.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.auth.exception.AuthInternalCommunicationException;
import com.choresync.auth.model.AuthLoginRequest;
import com.choresync.auth.model.AuthRegisterRequest;
import com.choresync.auth.service.AuthService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
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
  public String postRegister(@RequestBody AuthRegisterRequest authRegisterRequest) {
    return authService.registerUser(authRegisterRequest);
  }

  @PostMapping("/login")
  public String postLogin(@RequestBody AuthLoginRequest authLoginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authLoginRequest.getUsername(), authLoginRequest.getPassword()));

    if (authentication.isAuthenticated()) {
      return authService.loginUser(authLoginRequest);
    } else {
      throw new AuthInternalCommunicationException("Invalid credentials");
    }
  }

  @GetMapping("/validate")
  public String postValidate(@RequestParam("token") String token) {
    authService.validateToken(token);
    return "Token is valid";
  }
}
