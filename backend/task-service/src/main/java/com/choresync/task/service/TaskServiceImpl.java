package com.choresync.task.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.task.entity.Frequency;
import com.choresync.task.entity.Status;
import com.choresync.task.entity.Tag;
import com.choresync.task.entity.Task;
import com.choresync.task.exception.TaskCreationException;
import com.choresync.task.exception.TaskNotFoundException;
import com.choresync.task.exception.TaskUnforbiddenActionException;
import com.choresync.task.model.TaskEditMetadataRequest;
import com.choresync.task.model.TaskEditStatusRequest;
import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;
import com.choresync.task.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
  @Autowired
  private TaskRepository taskRepository;

  @Override
  public TaskResponse createTask(TaskRequest taskRequest) {
    if (taskRequest == null) {
      throw new TaskCreationException("Missing fields in request body");
    }

    Task task = Task.builder()
        .title(taskRequest.getTitle())
        .description(taskRequest.getDescription())
        .status(Status.valueOf(taskRequest.getStatus()))
        .frequency(Frequency.valueOf(taskRequest.getFrequency()))
        .tag(Tag.valueOf(taskRequest.getTag()))
        .userId(taskRequest.getUserId())
        .build();

    Task newTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(newTask.getId())
        .title(newTask.getTitle())
        .description(newTask.getDescription())
        .status(newTask.getStatus().name())
        .frequency(newTask.getFrequency().name())
        .tag(newTask.getTag().name())
        .userId(newTask.getUserId())
        .createdAt(newTask.getCreatedAt())
        .updatedAt(newTask.getUpdatedAt())
        .build();

    return taskResponse;
  }

  @Override
  public TaskResponse getTaskById(String id) {
    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    TaskResponse taskResponse = TaskResponse.builder()
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

    return taskResponse;
  }

  @Override
  public List<TaskResponse> getAllTasksByUserId(String userId) {
    if (userId == null) {
      throw new TaskNotFoundException("Task not found");
    }

    List<Task> tasks = taskRepository.findByUserId(userId);

    List<TaskResponse> taskResponses = new ArrayList<>();

    for (Task task : tasks) {
      TaskResponse taskResponse = TaskResponse.builder()
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

      taskResponses.add(taskResponse);
    }

    return taskResponses;
  }

  @Override
  public List<TaskResponse> getAllTasks() {
    List<Task> tasks = taskRepository.findAll();

    List<TaskResponse> taskResponses = new ArrayList<>();

    for (Task task : tasks) {
      TaskResponse taskResponse = TaskResponse.builder()
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

      taskResponses.add(taskResponse);
    }

    return taskResponses;
  }

  @Override
  public TaskResponse updateTask(String id, TaskEditMetadataRequest taskRequest) {
    if (taskRequest.getTitle() == null && taskRequest.getDescription() == null
        && taskRequest.getFrequency() == null && taskRequest.getTag() == null) {
      throw new TaskCreationException("Invalid request body");
    }

    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    task.setTitle(taskRequest.getTitle());
    task.setDescription(taskRequest.getDescription());
    task.setFrequency(Frequency.valueOf(taskRequest.getFrequency()));
    task.setTag(Tag.valueOf(taskRequest.getTag()));

    Task updatedTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(updatedTask.getId())
        .title(updatedTask.getTitle())
        .description(updatedTask.getDescription())
        .status(updatedTask.getStatus().name())
        .frequency(updatedTask.getFrequency().name())
        .tag(updatedTask.getTag().name())
        .userId(updatedTask.getUserId())
        .createdAt(updatedTask.getCreatedAt())
        .updatedAt(updatedTask.getUpdatedAt())
        .build();

    return taskResponse;
  }

  @Override
  public TaskResponse updateTaskStatus(String id, TaskEditStatusRequest taskRequest) {
    if (taskRequest.getStatus() == null && taskRequest.getUserId() == null) {
      throw new TaskCreationException("Invalid request body");
    }

    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    if (!task.getUserId().equals(taskRequest.getUserId())) {
      throw new TaskUnforbiddenActionException("User not allowed to perform this action");
    }

    task.setStatus(Status.valueOf(taskRequest.getStatus()));

    Task updatedTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(updatedTask.getId())
        .title(updatedTask.getTitle())
        .description(updatedTask.getDescription())
        .status(updatedTask.getStatus().name())
        .frequency(updatedTask.getFrequency().name())
        .tag(updatedTask.getTag().name())
        .userId(updatedTask.getUserId())
        .createdAt(updatedTask.getCreatedAt())
        .updatedAt(updatedTask.getUpdatedAt())
        .build();

    return taskResponse;
  }

  @Override
  public void deleteTask(String id) {
    if (taskRepository.existsById(id)) {
      taskRepository.deleteById(id);
    } else {
      throw new TaskNotFoundException("Task not found");
    }
  }

  @Override
  public TaskResponse unassignTask(String id, String userId) {
    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    if (!task.getUserId().equals(userId)) {
      throw new TaskUnforbiddenActionException("User not allowed to perform this action");
    }

    task.setUserId(null);

    Task updatedTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(updatedTask.getId())
        .title(updatedTask.getTitle())
        .description(updatedTask.getDescription())
        .status(updatedTask.getStatus().name())
        .frequency(updatedTask.getFrequency().name())
        .tag(updatedTask.getTag().name())
        .userId(updatedTask.getUserId())
        .createdAt(updatedTask.getCreatedAt())
        .updatedAt(updatedTask.getUpdatedAt())
        .build();

    return taskResponse;
  }

  @Override
  public TaskResponse assignTask(String id, String userId) {
    if (userId == null) {
      throw new TaskCreationException("Invalid request body");
    }

    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    if (task.getUserId() != null) {
      throw new TaskUnforbiddenActionException("Task already assigned");
    }

    task.setUserId(userId);

    Task updatedTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(updatedTask.getId())
        .title(updatedTask.getTitle())
        .description(updatedTask.getDescription())
        .status(updatedTask.getStatus().name())
        .frequency(updatedTask.getFrequency().name())
        .tag(updatedTask.getTag().name())
        .userId(updatedTask.getUserId())
        .createdAt(updatedTask.getCreatedAt())
        .updatedAt(updatedTask.getUpdatedAt())
        .build();

    return taskResponse;
  }
}