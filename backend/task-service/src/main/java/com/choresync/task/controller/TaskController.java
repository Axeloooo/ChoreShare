package com.choresync.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;
import com.choresync.task.service.TaskService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
  @Autowired
  private TaskService taskService;

  @PostMapping
  public ResponseEntity<String> createTask(@RequestBody TaskRequest taskRequest) {
    String taskId = taskService.createTask(taskRequest);
    return new ResponseEntity<>(taskId, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") String id) {
    TaskResponse task = taskService.getTaskById(id);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

}
