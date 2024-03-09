package com.choresync.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.auth.exception.AuthInternalCommunicationException;
import com.choresync.auth.external.response.UserAuthResponse;

@Component
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private RestTemplate restTemplate;

  @Override
  public UserDetails loadUserByUsername(String username) {
    try {
      UserAuthResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/username/" + username,
          UserAuthResponse.class);

      return new CustomUserDetails(userResponse);
    } catch (RestClientException e) {
      throw new AuthInternalCommunicationException(
          "User not found. " + e.getMessage());
    }
  }
}
