package com.choresync.assignment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.choresync.assignment.entity.Assignment;
import com.choresync.assignment.external.request.TaskRequest;
import com.choresync.assignment.external.response.TaskResponse;
import com.choresync.assignment.model.AssignmentRequest;
import com.choresync.assignment.model.AssignmentResponse;
import com.choresync.assignment.repository.AssignmentRepository;

@SpringBootTest
public class AssignmentServiceImplTest {
  @Mock
  private AssignmentRepository assignmentRepository;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  AssignmentService assignmentService = new AssignmentServiceImpl();

  @DisplayName("Test Get Successful Assignment")
  @Test
  void testGetSuccessfulAssignment() {
    Assignment assignment = getMockAssignment();

    when(assignmentRepository.findById(anyString())).thenReturn(Optional.of(assignment));

    when(restTemplate
        .getForObject(
            "http://task-service/api/v1/task/" + assignment.getTaskId(),
            TaskResponse.class))
        .thenReturn(getMockTaskResponse());

    AssignmentResponse assignmentResponse = assignmentService.getAssignmentById("1");

    verify(assignmentRepository, times(1)).findById(anyString());

    verify(restTemplate, times(1))
        .getForObject(
            "http://task-service/api/v1/task/" + assignment.getTaskId(),
            TaskResponse.class);

    assertNotNull(assignmentResponse);
    assertEquals(assignment.getId(), assignmentResponse.getId());
    assertEquals(assignment.getTaskId(), assignmentResponse.getTask().getId());
  }

  @DisplayName("Test Get Unsuccessful Assignment")
  @Test
  void testGetUnsuccessfulAssignment() {
    // TODO: Update test with proper exception
    when(assignmentRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

    assertThrows(Exception.class, () -> assignmentService.getAssignmentById("1"));

    verify(assignmentRepository, times(1)).findById(anyString());
  }

  @DisplayName("Test Post Successful Assignment")
  @Test
  void testPostSuccessfulAssignment() {
    AssignmentRequest assignmentRequest = getMockAssignmentRequest();
    Assignment assignment = getMockAssignment();
    TaskRequest taskRequest = getMockTaskRequest();

    when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

    when(restTemplate
        .postForObject(
            "http://task-service/api/v1/task",
            taskRequest,
            String.class))
        .thenReturn("1");

    String assignmentId = assignmentService.createAssignment(assignmentRequest);

    verify(assignmentRepository, times(1)).save(any(Assignment.class));

    verify(restTemplate, times(1))
        .postForObject(
            "http://task-service/api/v1/task",
            taskRequest,
            String.class);

    assertNotNull(assignmentId);
    assertEquals(assignment.getId(), assignmentId);
  }

  @DisplayName("Test Post Unsuccessful Assignment")
  @Test
  void testPostUnsuccessfulAssignment() {
    // TODO: Implement this test
  }

  private TaskRequest getMockTaskRequest() {
    TaskRequest taskRequest = TaskRequest
        .builder()
        .name("Task 1")
        .description("Task 1 Description")
        .dueDate(new Date())
        .build();
    return taskRequest;
  }

  private AssignmentRequest getMockAssignmentRequest() {
    AssignmentRequest assignmentRequest = AssignmentRequest
        .builder()
        .name("Task 1")
        .description("Task 1 Description")
        .dueDate(new Date())
        .householdId("1")
        .userId("1")
        .build();
    return assignmentRequest;
  }

  private TaskResponse getMockTaskResponse() {
    TaskResponse taskResponse = TaskResponse
        .builder()
        .id("1")
        .name("Task 1")
        .description("Task 1 Description")
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();
    return taskResponse;
  }

  private Assignment getMockAssignment() {
    Assignment assignment = Assignment
        .builder()
        .id("1")
        .householdId("1")
        .taskId("1")
        .userId("1")
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();
    return assignment;
  }
}
