package com.choresync.gateway.controller;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.gateway.model.AuthenticationResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  @GetMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(
      @AuthenticationPrincipal OidcUser oidcUser,
      Model model,
      @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient authorizedClient) {
    AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
        .id(oidcUser.getEmail())
        .accessToken(authorizedClient.getAccessToken().getTokenValue())
        .refreshToken(authorizedClient.getRefreshToken().getTokenValue())
        .expiresAt(authorizedClient.getAccessToken().getExpiresAt().getEpochSecond())
        .authorityList(
            oidcUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .build();

    return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
  }
}
