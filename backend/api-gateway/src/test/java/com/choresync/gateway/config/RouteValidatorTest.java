package com.choresync.gateway.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

public class RouteValidatorTest {

  private final RouteValidator validator = new RouteValidator();

  @Test
  public void loginAndRegisterAreOpen() {
    assertFalse(validator.isSecured.test(MockServerHttpRequest.post("/api/v1/auth/login").build()));
    assertFalse(validator.isSecured.test(MockServerHttpRequest.post("/api/v1/auth/register").build()));
  }

  @Test
  public void otherPathsAreSecured() {
    assertTrue(validator.isSecured.test(MockServerHttpRequest.get("/api/v1/task/household/h1").build()));
    assertTrue(validator.isSecured.test(MockServerHttpRequest.get("/api/v1/auth/validate").build()));
  }

  @Test
  public void eurekaIsNoLongerOpen() {
    assertTrue(validator.isSecured.test(MockServerHttpRequest.get("/eureka/apps").build()));
    assertTrue(validator.isSecured.test(MockServerHttpRequest.get("/api/v1/user/eureka").build()));
  }

  @Test
  public void openPathAsSubstringDoesNotSkipAuth() {
    assertTrue(validator.isSecured.test(
        MockServerHttpRequest.get("/api/v1/user/username/auth/login").build()));
    assertTrue(validator.isSecured.test(
        MockServerHttpRequest.get("/api/v1/task/household/h1;x=/auth/login").build()));
  }
}
