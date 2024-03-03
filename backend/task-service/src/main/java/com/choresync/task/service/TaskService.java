package com.choresync.task.service;

import java.util.List;

import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;

public interface TaskService {

  String createTask(TaskRequest taskRequest);

  TaskResponse getTaskById(String id);

  List<TaskResponse> getAllTasksByUserId(String userId);

  List<TaskResponse> getAllTasks();

}
