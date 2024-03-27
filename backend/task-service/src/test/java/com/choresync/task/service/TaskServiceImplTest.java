package com.choresync.task.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.task.entity.Frequency;
import com.choresync.task.entity.Status;
import com.choresync.task.entity.Tag;
import com.choresync.task.entity.Task;
import com.choresync.task.exception.TaskInternalCommunicationException;
import com.choresync.task.exception.TaskInvalidBodyException;
import com.choresync.task.exception.TaskInvalidParamException;
import com.choresync.task.exception.TaskNotFoundException;
import com.choresync.task.exception.TaskUnforbiddenActionException;
import com.choresync.task.external.exception.HouseholdNotFoundException;
import com.choresync.task.external.exception.UserNotFoundException;
import com.choresync.task.external.response.HouseholdResponse;
import com.choresync.task.external.response.UserResponse;
import com.choresync.task.model.TaskEditMetadataRequest;
import com.choresync.task.model.TaskEditStatusRequest;
import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;
import com.choresync.task.repository.TaskRepository;

public class TaskServiceImplTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskServiceImpl taskService;

  private Task task;
  private TaskRequest taskRequest;
  private TaskResponse taskResponse;
  private TaskRequest taskInvalidRequest;
  private UserResponse userResponse;
  private HouseholdResponse householdResponse;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    taskInvalidRequest = TaskRequest
        .builder()
        .build();

    taskRequest = TaskRequest
        .builder()
        .title("Test Task")
        .description("This is a test task")
        .status("PENDING")
        .frequency("EVERYDAY")
        .tag("GENERAL")
        .userId("user1")
        .householdId("household1")
        .build();

    task = Task.builder()
        .id("1")
        .title(taskRequest.getTitle())
        .description(taskRequest.getDescription())
        .status(Status.valueOf(taskRequest.getStatus()))
        .frequency(Frequency.valueOf(taskRequest.getFrequency()))
        .tag(Tag.valueOf(taskRequest.getTag()))
        .userId(null)
        .householdId("household1")
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    taskResponse = TaskResponse.builder()
        .id(task.getId())
        .title(task.getTitle())
        .description(task.getDescription())
        .status(task.getStatus().name())
        .frequency(task.getFrequency().name())
        .tag(task.getTag().name())
        .userId(task.getUserId())
        .householdId(task.getHouseholdId())
        .createdAt(task.getCreatedAt())
        .updatedAt(task.getUpdatedAt())
        .build();

    userResponse = UserResponse
        .builder()
        .id("user1")
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("axel@gmail.com")
        .phone("1234567890")
        .missedChores(0)
        .streak(0)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    householdResponse = HouseholdResponse
        .builder()
        .id("household1")
        .name("Household 1")
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();
  }

  @Description("POST /api/v1/task - Test create task with invalid body")
  @Test
  public void testCreateTaskWithInvalidBody() {
    assertThrows(TaskInvalidBodyException.class, () -> taskService.createTask(taskInvalidRequest));
  }

  @Description("POST /api/v1/task - Test create task with household not found")
  @Test
  public void testCreateTaskWithHouseholdNotFound() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class))
        .thenReturn(userResponse);
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class))
        .thenReturn(null);

    assertThrows(HouseholdNotFoundException.class, () -> taskService.createTask(taskRequest));
  }

  @Description("POST /api/v1/task - Test create task with user not found")
  @Test
  public void testCreateTaskWithUserNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class))
        .thenReturn(householdResponse);
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class))
        .thenReturn(null);

    assertThrows(UserNotFoundException.class, () -> taskService.createTask(taskRequest));
  }

  @Description("POST /api/v1/task - Test create task with internal communication exception in household service")
  @Test
  public void testCreateTaskWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class))
        .thenThrow(new RestClientException("Internal error"));
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class))
        .thenReturn(userResponse);

    assertThrows(TaskInternalCommunicationException.class, () -> taskService.createTask(taskRequest));
  }

  @Description("POST /api/v1/task - Test create task with internal communication exception in user service")
  @Test
  public void testCreateTaskWithInternalCommunicationExceptionInUserService() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class))
        .thenReturn(householdResponse);
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class))
        .thenThrow(new RestClientException("Internal error"));

    assertThrows(TaskInternalCommunicationException.class, () -> taskService.createTask(taskRequest));
  }

  @Description("POST /api/v1/task - Test create task")
  @Test
  public void testCreateTask() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class))
        .thenReturn(userResponse);

    when(restTemplate.getForObject("http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class))
        .thenReturn(householdResponse);

    when(taskRepository.save(any(Task.class)))
        .thenReturn(task);

    TaskResponse response = taskService.createTask(taskRequest);

    assertNotNull(response);
    assertEquals(taskResponse, response);

    verify(restTemplate, times(1)).getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(),
        UserResponse.class);
    verify(restTemplate, times(1)).getForObject(
        "http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class);
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("GET /api/v1/task/{id} - Test get task by id with invalid parameter")
  @Test
  public void testGetTaskByIdWithInvalidParam() {
    assertThrows(TaskInvalidParamException.class, () -> taskService.getTaskById(null));
  }

  @Description("GET /api/v1/task/{id} - Test get task by id with task not found")
  @Test
  public void testGetTaskByIdWithTaskNotFound() {
    when(taskRepository.findById("invalidId")).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById("invalidId"));
  }

  @Description("GET /api/v1/task/{id} - Test get task by id success")
  @Test
  public void testGetTaskByIdSuccess() {
    when(taskRepository.findById("1")).thenReturn(Optional.of(task));
    TaskResponse response = taskService.getTaskById("1");

    assertNotNull(response);
    assertEquals(taskResponse, response);

    verify(taskRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/task/user/{userId} - Test get all tasks by user id with invalid param")
  @Test
  public void testGetAllTasksByUserIdWithInvalidParam() {
    assertThrows(TaskInvalidParamException.class, () -> taskService.getAllTasksByUserId(null));
  }

  @Description("GET /api/v1/task/user/{userId} - Test get all tasks by user id with user not found")
  @Test
  public void testGetAllTasksByUserIdWithUserNotFound() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class))
        .thenReturn(null);

    assertThrows(UserNotFoundException.class, () -> taskService.getAllTasksByUserId("user1"));
  }

  @Description("GET /api/v1/task/user/{userId} - Test get all tasks by user id with internal communication exception")
  @Test
  public void testGetAllTasksByUserIdWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class))
        .thenThrow(new RestClientException("Internal error"));

    assertThrows(TaskInternalCommunicationException.class, () -> taskService.getAllTasksByUserId("user1"));
  }

  @Description("GET /api/v1/task/user/{userId} - Test get all tasks by user id success")
  @Test
  public void testGetAllTasksByUserIdSuccess() {
    List<Task> tasks = Arrays.asList(task);

    when(restTemplate.getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class))
        .thenReturn(userResponse);
    when(taskRepository.findByUserId(anyString())).thenReturn(tasks);

    List<TaskResponse> responses = taskService.getAllTasksByUserId("user1");

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(taskResponse, responses.get(0));

    verify(restTemplate, times(1)).getForObject("http://user-service/api/v1/user/" + taskRequest.getUserId(),
        UserResponse.class);
    verify(taskRepository, times(1)).findByUserId(anyString());
  }

  @Description("GET /api/v1/task/household/{householdId} - Test get all tasks by household id with invalid param")
  @Test
  public void testGetAllTasksByHouseholdIdWithInvalidParam() {
    assertThrows(TaskInvalidParamException.class, () -> taskService.getAllTasksByHouseholdId(null));
  }

  @Description("GET /api/v1/task/household/{householdId} - Test get all tasks by household id with household not found")
  @Test
  public void testGetAllTasksByHouseholdIdWithHouseholdNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class))
        .thenReturn(null);

    assertThrows(HouseholdNotFoundException.class, () -> taskService.getAllTasksByHouseholdId("household1"));
  }

  @Description("GET /api/v1/task/household/{householdId} - Test get all tasks by household id with internal communication exception")
  @Test
  public void testGetAllTasksByHouseholdIdWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class))
        .thenThrow(new RestClientException("Internal error"));

    assertThrows(TaskInternalCommunicationException.class, () -> taskService.getAllTasksByHouseholdId("household1"));
  }

  @Description("GET /api/v1/task/household/{householdId} - Test get all tasks by household id success")
  @Test
  public void testGetAllTasksByHouseholdIdSuccess() {
    List<Task> tasks = Arrays.asList(task);

    when(restTemplate.getForObject("http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class))
        .thenReturn(householdResponse);
    when(taskRepository.findByHouseholdId(anyString())).thenReturn(tasks);

    List<TaskResponse> responses = taskService.getAllTasksByHouseholdId("household1");

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(taskResponse, responses.get(0));

    verify(restTemplate, times(1)).getForObject(
        "http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
        HouseholdResponse.class);
    verify(taskRepository, times(1)).findByHouseholdId(anyString());
  }

  @Description("PUT /api/v1/task/{id} - Test update task with invalid param")
  @Test
  public void testUpdateTaskWithInvalidParam() {
    assertThrows(TaskInvalidParamException.class,
        () -> taskService.updateTask(null, TaskEditMetadataRequest.builder().build()));
  }

  @Description("PUT /api/v1/task/{id} - Test update task with invalid body")
  @Test
  public void testUpdateTaskWithInvalidBody() {
    TaskEditMetadataRequest request = TaskEditMetadataRequest
        .builder()
        .build();

    assertThrows(TaskInvalidBodyException.class, () -> taskService.updateTask("1", request));
  }

  @Description("PUT /api/v1/task/{id} - Test update task with task not found")
  @Test
  public void testUpdateTaskWithTaskNotFound() {
    when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

    TaskEditMetadataRequest request = TaskEditMetadataRequest
        .builder()
        .title("Updated Task")
        .description("Updated description")
        .frequency("WEEKLY")
        .tag("CLEANING")
        .build();

    assertThrows(TaskNotFoundException.class,
        () -> taskService.updateTask("1", request));
  }

  @Description("PUT /api/v1/task/{id} - Test update task success")
  @Test
  public void testUpdateTaskSuccess() {
    TaskEditMetadataRequest request = TaskEditMetadataRequest
        .builder()
        .title("Updated Task")
        .description("Updated description")
        .frequency("ONCE_A_WEEK")
        .tag("KITCHEN")
        .build();

    when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    TaskResponse response = taskService.updateTask("1", request);

    assertNotNull(response);
    assertEquals("Updated Task", response.getTitle());
    assertEquals("Updated description", response.getDescription());
    assertEquals("ONCE_A_WEEK", response.getFrequency());
    assertEquals("KITCHEN", response.getTag());

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("PUT /api/v1/task/{id}/status - Test update task status with invalid param")
  @Test
  public void testUpdateTaskStatusWithInvalidParam() {
    assertThrows(TaskInvalidParamException.class,
        () -> taskService.updateTaskStatus(null, new TaskEditStatusRequest()));
  }

  @Description("PUT /api/v1/task/{id}/status - Test update task status with invalid body")
  @Test
  public void testUpdateTaskStatusWithInvalidBody() {
    TaskEditStatusRequest request = TaskEditStatusRequest
        .builder()
        .build();
    assertThrows(TaskInvalidBodyException.class, () -> taskService.updateTaskStatus("1", request));
  }

  @Description("PUT /api/v1/task/{id}/status - Test update task status with task not found")
  @Test
  public void testUpdateTaskStatusWithTaskNotFound() {
    when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

    TaskEditStatusRequest request = TaskEditStatusRequest
        .builder()
        .status("COMPLETED")
        .userId("user1")
        .build();

    assertThrows(TaskNotFoundException.class,
        () -> taskService.updateTaskStatus("1", request));
  }

  @Description("PUT /api/v1/task/{id}/status - Test update task status with forbidden action")
  @Test
  public void testUpdateTaskStatusWithForbiddenAction() {
    TaskEditStatusRequest request = TaskEditStatusRequest
        .builder()
        .status("COMPLETED")
        .userId("user2")
        .build();

    task.setUserId("user1");

    when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

    assertThrows(TaskUnforbiddenActionException.class, () -> taskService.updateTaskStatus("1", request));
  }

  @Description("PUT /api/v1/task/{id}/status - Test update task status success")
  @Test
  public void testUpdateTaskStatusSuccess() {
    TaskEditStatusRequest request = TaskEditStatusRequest
        .builder()
        .status("COMPLETED")
        .userId("user1")
        .build();

    task.setUserId("user1");

    when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

    when(taskRepository.save(any(Task.class))).thenReturn(task);

    TaskResponse response = taskService.updateTaskStatus("1", request);

    assertNotNull(response);
    assertEquals("COMPLETED", response.getStatus());

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("DELETE /api/v1/task/{id} - Test delete task with invalid param")
  @Test
  public void testDeleteTaskWithInvalidParam() {
    assertThrows(TaskInvalidParamException.class, () -> taskService.deleteTask(null));
  }

  @Description("DELETE /api/v1/task/{id} - Test delete task with task not found")
  @Test
  public void testDeleteTaskWithTaskNotFound() {
    when(taskRepository.existsById(anyString())).thenReturn(false);

    assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask("1"));
  }

  @Description("DELETE /api/v1/task/{id} - Test delete task success")
  @Test
  public void testDeleteTaskSuccess() {
    when(taskRepository.existsById(anyString())).thenReturn(true);

    doNothing().when(taskRepository).deleteById(anyString());

    taskService.deleteTask("1");

    verify(taskRepository, times(1)).deleteById("1");
  }

  @Description("PUT /api/v1/task/{id}/unassign - Test unassign task with invalid param")
  @Test
  public void testUnassignTaskWithInvalidParam() {
    assertThrows(TaskInvalidParamException.class, () -> taskService.unassignTask(null, "user1"));
    assertThrows(TaskInvalidParamException.class, () -> taskService.unassignTask("1", null));
  }

  @Description("PUT /api/v1/task/{id}/unassign - Test unassign task with task not found")
  @Test
  public void testUnassignTaskWithTaskNotFound() {
    when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskService.unassignTask("1", "user1"));
  }

  @Description("PUT /api/v1/task/{id}/unassign - Test unassign task with forbidden action")
  @Test
  public void testUnassignTaskWithForbiddenAction() {
    task.setUserId("user1");

    when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

    assertThrows(TaskUnforbiddenActionException.class, () -> taskService.unassignTask("1", "user2"));
  }

  @Description("PUT /api/v1/task/{id}/unassign - Test unassign task success")
  @Test
  public void testUnassignTaskSuccess() {
    task.setUserId("user1");

    when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    TaskResponse response = taskService.unassignTask("1", "user1");

    assertNotNull(response);
    assertNull(response.getUserId());
    assertEquals(Status.PENDING.name(), response.getStatus());

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("PUT /api/v1/task/{id}/assign - Test assign task with invalid param")
  @Test
  public void testAssignTaskWithInvalidParam() {
    assertThrows(TaskInvalidParamException.class, () -> taskService.assignTask(null, "user1"));
    assertThrows(TaskInvalidParamException.class, () -> taskService.assignTask("1", null));
  }

  @Description("PUT /api/v1/task/{id}/assign - Test assign task with task not found")
  @Test
  public void testAssignTaskWithTaskNotFound() {
    when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskService.assignTask("1", "user1"));
  }

  @Description("PUT /api/v1/task/{id}/assign - Test assign task with forbidden action")
  @Test
  public void testAssignTaskWithForbiddenAction() {
    task.setUserId("user1");

    when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));

    assertThrows(TaskUnforbiddenActionException.class, () -> taskService.assignTask("1", "user1"));
  }

  @Description("PUT /api/v1/task/{id}/assign - Test assign task success")
  @Test
  public void testAssignTaskSuccess() {
    when(taskRepository.findById(anyString())).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    TaskResponse response = taskService.assignTask("1", "user1");

    assertNotNull(response);
    assertEquals("user1", response.getUserId());

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(1)).save(any(Task.class));
  }
}