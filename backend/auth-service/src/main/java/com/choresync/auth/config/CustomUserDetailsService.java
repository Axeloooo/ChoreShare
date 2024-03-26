package com.choresync.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.auth.exception.AuthInternalCommunicationException;
import com.choresync.auth.external.exception.UserNotFoundException;
import com.choresync.auth.external.response.UserAuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private RestTemplate restTemplate;

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
  public UserDetails loadUserByUsername(String username) {
    try {
      UserAuthResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/username/" + username,
          UserAuthResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("User not found");
      }
      return new CustomUserDetails(userResponse);
    } catch (RestClientException e) {
      throw new AuthInternalCommunicationException(extractErrorMessage(e));
    }
  }
}
