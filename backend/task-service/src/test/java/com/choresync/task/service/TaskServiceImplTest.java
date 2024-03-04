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
import com.choresync.task.exception.TaskCreationException;
import com.choresync.task.exception.TaskNotFoundException;
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

    task = Task.builder()
        .id("1")
        .title("Test Task")
        .description("This is a test task")
        .status(Status.PENDING)
        .frequency(Frequency.EVERYDAY)
        .tag(Tag.GENERAL)
        .userId("user1")
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    taskRequest = TaskRequest.builder()
        .title("Test Task")
        .description("This is a test task")
        .status(Status.PENDING)
        .frequency(Frequency.EVERYDAY)
        .tag(Tag.GENERAL)
        .userId("user1")
        .build();

    taskResponse = TaskResponse.builder()
        .id("1")
        .title("Test Task")
        .description("This is a test task")
        .status(Status.PENDING)
        .frequency(Frequency.EVERYDAY)
        .tag(Tag.GENERAL)
        .userId("user1")
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
    assertThrows(TaskCreationException.class, () -> taskService.createTask(null));

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

  @Description("Get /api/v1/task - Test get all tasks")
  @Test
  public void testGetAllTasks() {
    when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

    List<TaskResponse> result = taskService.getAllTasks();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(taskResponse, result.get(0));

    verify(taskRepository, times(1)).findAll();
  }

  @Description("Get /api/v1/task - Test get all empty tasks")
  @Test
  public void testGetAllTasksEmpty() {
    when(taskRepository.findAll()).thenReturn(Arrays.asList());

    List<TaskResponse> result = taskService.getAllTasks();

    assertNotNull(result);
    assertEquals(0, result.size());

    verify(taskRepository, times(1)).findAll();
  }
}
