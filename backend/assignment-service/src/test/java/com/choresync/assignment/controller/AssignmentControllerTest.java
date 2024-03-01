package com.choresync.assignment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.choresync.assignment.AssignmentServiceConfig;
import com.choresync.assignment.entity.Assignment;
import com.choresync.assignment.model.AssignmentRequest;
import com.choresync.assignment.repository.AssignmentRepository;
import com.choresync.assignment.service.AssignmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

@SpringBootTest({ "server.port=0" })
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = { AssignmentServiceConfig.class })
public class AssignmentControllerTest {

  @Autowired
  private AssignmentService assignmentService;

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Autowired
  private MockMvc mockMvc;

  @RegisterExtension
  static WireMockExtension wireMockServer = WireMockExtension
      .newInstance()
      .options(WireMockConfiguration
          .wireMockConfig()
          .port(8080))
      .build();

  private ObjectMapper objectMapper = new ObjectMapper()
      .findAndRegisterModules()
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @BeforeEach
  public void setup() {
    postTask();
  }

  private void postTask() {
    wireMockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/api/v1/task"))
        .willReturn(WireMock.aResponse()
            .withStatus(HttpStatus.CREATED.value())
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
  }

  @Test
  public void testPostAssignment() throws Exception {

    AssignmentRequest assignmentRequest = AssignmentRequest.builder()
        .name("Task 1")
        .description("Task 1 Description")
        .dueDate(new Date())
        .householdId("1")
        .userId("1")
        .build();

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/task")
        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("Customer")))
        .content(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(assignmentRequest)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    String assignmentId = mvcResult.getResponse().getContentAsString();

    Optional<Assignment> assignment = assignmentRepository.findById(String.valueOf(assignmentId));

    assert (assignment.isPresent());

    Assignment assignmentEntity = assignment.get();

    assertEquals(String.valueOf(assignmentId), assignmentEntity.getId());
    assertEquals(assignmentRequest.getHouseholdId(), assignmentEntity.getHouseholdId());
    assertEquals(assignmentRequest.getUserId(), assignmentEntity.getUserId());
  }

}