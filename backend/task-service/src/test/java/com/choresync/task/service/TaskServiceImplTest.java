package com.choresync.task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.choresync.task.entity.Frequency;
import com.choresync.task.entity.Status;
import com.choresync.task.entity.Tag;
import com.choresync.task.entity.Task;
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

  @Test
  public void testCreateTask() {
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    String taskId = taskService.createTask(taskRequest);

    assertNotNull(taskId);
    assertEquals(task.getId(), taskId);
  }

  @Test
  public void testGetTaskById() {
    when(taskRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(task));

    TaskResponse result = taskService.getTaskById("1");

    assertNotNull(result);
    assertEquals(taskResponse, result);
  }

  @Test
  public void testGetAllTasksByUserId() {
    when(taskRepository.findByUserId("user1")).thenReturn(Arrays.asList(task));

    List<TaskResponse> result = taskService.getAllTasksByUserId("user1");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(taskResponse, result.get(0));
  }

  @Test
  public void testGetAllTasks() {
    when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

    List<TaskResponse> result = taskService.getAllTasks();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(taskResponse, result.get(0));
  }
}
