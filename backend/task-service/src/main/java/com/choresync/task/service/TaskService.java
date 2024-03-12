package com.choresync.task.service;

import java.util.List;

import com.choresync.task.model.TaskEditMetadataRequest;
import com.choresync.task.model.TaskEditStatusRequest;
import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;

public interface TaskService {

  TaskResponse createTask(TaskRequest taskRequest);

  TaskResponse getTaskById(String id);

  List<TaskResponse> getAllTasksByUserId(String userId);

  List<TaskResponse> getAllTasksByHousehold(String householdId);

  TaskResponse updateTask(String id, TaskEditMetadataRequest taskRequest);

  TaskResponse updateTaskStatus(String id, TaskEditStatusRequest taskRequest);

  void deleteTask(String id);

  TaskResponse unassignTask(String id, String userId);

  TaskResponse assignTask(String id, String userId);
}
