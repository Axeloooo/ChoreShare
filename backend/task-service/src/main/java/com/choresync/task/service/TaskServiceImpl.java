package com.choresync.task.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.task.entity.Status;
import com.choresync.task.entity.Task;
import com.choresync.task.exception.TaskCreationException;
import com.choresync.task.exception.TaskNotFoundException;
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
        .status(taskRequest.getStatus() != null ? taskRequest.getStatus() : Status.PENDING)
        .frequency(taskRequest.getFrequency())
        .tag(taskRequest.getTag())
        .userId(taskRequest.getUserId())
        .build();

    Task newTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(newTask.getId())
        .title(newTask.getTitle())
        .description(newTask.getDescription())
        .status(newTask.getStatus())
        .frequency(newTask.getFrequency())
        .tag(newTask.getTag())
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
        .status(task.getStatus())
        .frequency(task.getFrequency())
        .tag(task.getTag())
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
          .status(task.getStatus())
          .frequency(task.getFrequency())
          .tag(task.getTag())
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
          .status(task.getStatus())
          .frequency(task.getFrequency())
          .tag(task.getTag())
          .userId(task.getUserId())
          .createdAt(task.getCreatedAt())
          .updatedAt(task.getUpdatedAt())
          .build();

      taskResponses.add(taskResponse);
    }

    return taskResponses;
  }
}