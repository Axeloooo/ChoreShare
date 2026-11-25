package com.choresync.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "EUREKA_USERNAME=test-user", "EUREKA_PASSWORD=test-pass" })
class RegistrySecurityTest {

  @Autowired
  private TestRestTemplate rest;

  @Test
  void registryApiRequiresCredentials() {
    assertEquals(HttpStatus.UNAUTHORIZED, rest.getForEntity("/eureka/apps", String.class).getStatusCode());
  }

  @Test
  void dashboardRequiresCredentials() {
    assertEquals(HttpStatus.UNAUTHORIZED, rest.getForEntity("/", String.class).getStatusCode());
  }

  @Test
  void unauthenticatedRegistrationIsRejected() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String body = "{\"instance\":{\"instanceId\":\"evil\",\"hostName\":\"evil.example\",\"app\":\"AUTH-SERVICE\","
        + "\"ipAddr\":\"10.0.0.66\",\"status\":\"UP\",\"port\":{\"$\":80,\"@enabled\":\"true\"},"
        + "\"dataCenterInfo\":{\"@class\":\"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\",\"name\":\"MyOwn\"}}}";

    assertEquals(HttpStatus.UNAUTHORIZED,
        rest.postForEntity("/eureka/apps/AUTH-SERVICE", new HttpEntity<>(body, headers), String.class)
            .getStatusCode());
  }

  @Test
  void registryApiWorksWithCredentials() {
    assertEquals(HttpStatus.OK,
        rest.withBasicAuth("test-user", "test-pass").getForEntity("/eureka/apps", String.class).getStatusCode());
  }
}
