package com.choresync.task.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.task.entity.Status;
import com.choresync.task.entity.Task;
import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;
import com.choresync.task.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
  @Autowired
  private TaskRepository taskRepository;

  @Override
  public String createTask(TaskRequest taskRequest) {
    Task task = Task.builder()
        .title(taskRequest.getTitle())
        .description(taskRequest.getDescription())
        .status(taskRequest.getStatus() != null ? taskRequest.getStatus() : Status.PENDING)
        .frequency(taskRequest.getFrequency())
        .tag(taskRequest.getTag())
        .userId(taskRequest.getUserId())
        .build();

    Task newTask = taskRepository.save(task);

    return newTask.getId();
  }

  @Override
  public TaskResponse getTaskById(String id) {
    Task task = taskRepository.findById(id).orElse(null);

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