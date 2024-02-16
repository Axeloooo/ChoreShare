package com.choresync.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        .name(taskRequest.getName())
        .description(taskRequest.getDescription())
        .status("PENDING")
        .dueDate(taskRequest.getDueDate())
        .build();

    taskRepository.save(task);

    return task.getId();
  }

  @Override
  public TaskResponse getTaskById(String id) {
    Task task = taskRepository.findById(id).orElse(null);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(task.getId())
        .name(task.getName())
        .description(task.getDescription())
        .status(task.getStatus())
        .dueDate(task.getDueDate())
        .createdAt(task.getCreatedAt())
        .updatedAt(task.getUpdatedAt())
        .build();

    return taskResponse;
  }

}
