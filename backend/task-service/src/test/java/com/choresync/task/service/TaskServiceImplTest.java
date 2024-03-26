package com.choresync.task.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

import com.choresync.task.entity.Frequency;
import com.choresync.task.entity.Status;
import com.choresync.task.entity.Tag;
import com.choresync.task.entity.Task;
import com.choresync.task.exception.TaskInvalidBodyException;
import com.choresync.task.exception.TaskNotFoundException;
import com.choresync.task.exception.TaskUnforbiddenActionException;
import com.choresync.task.model.TaskEditMetadataRequest;
import com.choresync.task.model.TaskEditStatusRequest;
import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;
import com.choresync.task.repository.TaskRepository;

public class TaskServiceImplTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskServiceImpl taskService;

  private Task task;
  private TaskRequest taskRequest;
  private TaskResponse taskResponse;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    taskRequest = TaskRequest.builder()
        .title("Test Task")
        .description("This is a test task")
        .status("PENDING")
        .frequency("EVERYDAY")
        .tag("GENERAL")
        .userId("user1")
        .build();

    task = Task.builder()
        .id("1")
        .title(taskRequest.getTitle())
        .description(taskRequest.getDescription())
        .status(Status.valueOf(taskRequest.getStatus()))
        .frequency(Frequency.valueOf(taskRequest.getFrequency()))
        .tag(Tag.valueOf(taskRequest.getTag()))
        .userId("user1")
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
        .createdAt(task.getCreatedAt())
        .updatedAt(task.getUpdatedAt())
        .build();
  }

  @Description("POST /api/v1/task - Test create task")
  @Test
  public void testCreateTask() {
    when(taskRepository.save(any(Task.class)))
        .thenReturn(task);

    TaskResponse response = taskService.createTask(taskRequest);

    assertNotNull(response);
    assertEquals(taskResponse, response);

    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("POST /api/v1/task - Test TaskCreationException when creating task")
  @Test
  public void testCreateTaskWithNullRequest() {
    assertThrows(TaskInvalidBodyException.class, () -> taskService.createTask(null));

    verify(taskRepository, times(0)).save(any(Task.class));
  }

  @Description("GET /api/v1/task/{id} - Test get task by id")
  @Test
  public void testGetTaskById() {
    when(taskRepository.findById("1"))
        .thenReturn(Optional.of(task));

    TaskResponse response = taskService.getTaskById("1");

    assertNotNull(response);
    assertEquals(taskResponse, response);

    verify(taskRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/task/{id} - Test TaskNotFoundException when getting task by id")
  @Test
  public void testGetTaskByIdNotFound() {
    assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById("1"));

    verify(taskRepository, times(1)).findById("1");
  }

  @Description("Get /api/v1/task/user/{userId} - Test get all tasks by user id")
  @Test
  public void testGetAllTasksByUserId() {
    when(taskRepository.findByUserId("user1"))
        .thenReturn(Arrays.asList(task));

    List<TaskResponse> result = taskService.getAllTasksByUserId("user1");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(taskResponse, result.get(0));

    verify(taskRepository, times(1)).findByUserId("user1");
  }

  @Description("Get /api/v1/task/user/{userId} - Test get all empty tasks by user id")
  @Test
  public void testGetAllTasksByUserIdEmpty() {
    when(taskRepository.findByUserId("user1"))
        .thenReturn(Arrays.asList());

    List<TaskResponse> result = taskService.getAllTasksByUserId("user1");

    assertNotNull(result);
    assertEquals(0, result.size());

    verify(taskRepository, times(1)).findByUserId("user1");
  }

  @Description("Get /api/v1/task/user/{userId} - Test TaskNotFoundException when getting all tasks by user id")
  @Test
  public void testGetAllTasksByUserIdNotFound() {
    assertThrows(TaskNotFoundException.class, () -> taskService.getAllTasksByUserId(null));

    verify(taskRepository, times(0)).findByUserId(null);
  }

  // @Description("Get /api/v1/task - Test get all tasks")
  // @Test
  // public void testGetAllTasks() {
  // when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

  // List<TaskResponse> result = taskService.getAllTasks();

  // assertNotNull(result);
  // assertEquals(1, result.size());
  // assertEquals(taskResponse, result.get(0));

  // verify(taskRepository, times(1)).findAll();
  // }

  // @Description("Get /api/v1/task - Test get all empty tasks")
  // @Test
  // public void testGetAllTasksEmpty() {
  // when(taskRepository.findAll()).thenReturn(Arrays.asList());

  // List<TaskResponse> result = taskService.getAllTasks();

  // assertNotNull(result);
  // assertEquals(0, result.size());

  // verify(taskRepository, times(1)).findAll();
  // }

  @Description("PUT /api/v1/task/{id} - Test update task")
  @Test
  void updateTask() {
    TaskEditMetadataRequest taskRequest = TaskEditMetadataRequest
        .builder()
        .title("Updated Task")
        .description("Updated Description")
        .frequency("ONCE_A_WEEK")
        .tag("KITCHEN")
        .build();

    when(taskRepository.findById("1"))
        .thenReturn(Optional.of(task));

    when(taskRepository.save(any(Task.class)))
        .thenReturn(task);

    TaskResponse updatedTaskResponse = taskService.updateTask("1", taskRequest);

    assertNotNull(updatedTaskResponse);
    assertEquals("Updated Task", updatedTaskResponse.getTitle());
    assertEquals("Updated Description", updatedTaskResponse.getDescription());
    assertEquals("ONCE_A_WEEK", updatedTaskResponse.getFrequency());
    assertEquals("KITCHEN", updatedTaskResponse.getTag());

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("PUT /api/v1/task/{id} - Test update task status")
  @Test
  void updateTaskStatus() {
    TaskEditStatusRequest taskRequest = TaskEditStatusRequest
        .builder()
        .status("COMPLETED")
        .userId("user1")
        .build();

    when(taskRepository.findById("1"))
        .thenReturn(Optional.of(task));

    when(taskRepository.save(any(Task.class)))
        .thenReturn(task);

    TaskResponse updatedTaskResponse = taskService.updateTaskStatus("1", taskRequest);

    assertNotNull(updatedTaskResponse);
    assertEquals("COMPLETED", updatedTaskResponse.getStatus());

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("DELETE /api/v1/task/{id} - Test delete task")
  @Test
  void deleteTask() {
    when(taskRepository.existsById("1"))
        .thenReturn(true);

    assertDoesNotThrow(() -> taskService.deleteTask("1"));

    verify(taskRepository, times(1)).deleteById("1");
  }

  @Description("PUT /api/v1/task/{id}/unassign/{uid} - Test unassign task")
  @Test
  void unassignTask() {
    when(taskRepository.findById("1"))
        .thenReturn(Optional.of(task));

    when(taskRepository.save(any(Task.class)))
        .thenReturn(task);

    TaskResponse unassignedTaskResponse = taskService.unassignTask("1", "user1");

    assertNotNull(unassignedTaskResponse);
    assertNull(unassignedTaskResponse.getUserId());

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("PUT /api/v1/task/{id}/assign/{uid} - Test assign task")
  @Test
  void assignTask() {
    Task task2 = Task.builder()
        .id("2")
        .title(taskRequest.getTitle())
        .description(taskRequest.getDescription())
        .status(Status.PENDING)
        .frequency(Frequency.EVERYDAY)
        .tag(Tag.GENERAL)
        .userId(null)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    when(taskRepository.findById("2"))
        .thenReturn(Optional.of(task2));
    when(taskRepository.save(any(Task.class)))
        .thenReturn(task2);

    TaskResponse assignedTaskResponse = taskService.assignTask("2", "user2");

    assertNotNull(assignedTaskResponse);
    assertEquals("user2", assignedTaskResponse.getUserId());

    verify(taskRepository, times(1)).findById("2");
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Description("PUT /api/v1/task/{id} - Test TaskCreationException when updating task")
  @Test
  void updateTask_invalidRequest_throwsException() {
    TaskEditMetadataRequest taskRequest = new TaskEditMetadataRequest(null, null, null, null);

    assertThrows(TaskInvalidBodyException.class, () -> {
      taskService.updateTask("1", taskRequest);
    });

    verify(taskRepository, times(0)).findById("1");
    verify(taskRepository, times(0)).save(any(Task.class));
  }

  @Description("PUT /api/v1/task/{id} - Test TaskCreationException when updating task")
  @Test
  void updateTaskStatus_invalidRequest_throwsException() {
    TaskEditStatusRequest taskRequest = new TaskEditStatusRequest(null, null);

    assertThrows(TaskInvalidBodyException.class, () -> {
      taskService.updateTaskStatus("1", taskRequest);
    });

  }

  @Description("DELETE /api/v1/task/{id} - Test TaskNotFoundException when deleting task")
  @Test
  void deleteTask_notFound_throwsException() {
    when(taskRepository.existsById("1")).thenReturn(false);

    assertThrows(TaskNotFoundException.class, () -> {
      taskService.deleteTask("1");
    });

    verify(taskRepository, times(1)).existsById("1");
    verify(taskRepository, times(0)).deleteById("1");
  }

  @Description("PUT /api/v1/task/{id}/unassign/{uid} - Test TaskNotFoundException when unassigning task")
  @Test
  void unassignTask_notFound_throwsException() {
    when(taskRepository.findById("1")).thenReturn(java.util.Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> {
      taskService.unassignTask("1", "123");
    });

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(0)).save(any(Task.class));
  }

  @Description("PUT /api/v1/task/{id}/unassign/{uid} - Test TaskUnforbiddenActionException when unassigning task")
  @Test
  void unassignTask_unauthorized_throwsException() {
    when(taskRepository.findById("1"))
        .thenReturn(Optional.of(task));

    assertThrows(TaskUnforbiddenActionException.class, () -> {
      taskService.unassignTask("1", "123");
    });

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(0)).save(any(Task.class));
  }

  @Description("PUT /api/v1/task/{id}/assign/{uid} - Test TaskNotFoundException when assigning task")
  @Test
  void assignTask_alreadyAssigned_throwsException() {

    when(taskRepository.findById("1"))
        .thenReturn(Optional.of(task));

    assertThrows(TaskUnforbiddenActionException.class, () -> {
      taskService.assignTask("1", "123");
    });

    verify(taskRepository, times(1)).findById("1");
    verify(taskRepository, times(0)).save(any(Task.class));
  }
}
