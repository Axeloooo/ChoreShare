package com.choresync.task.service;

import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;

public interface TaskService {

  String createTask(TaskRequest taskRequest);

  TaskResponse getTaskById(String id);

}
