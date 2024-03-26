package com.choresync.task.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.choresync.task.model.TaskEditMetadataRequest;
import com.choresync.task.model.TaskEditStatusRequest;
import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;
import com.choresync.task.service.TaskService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
  @Autowired
  private TaskService taskService;

  @PostMapping
  public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
    TaskResponse task = taskService.createTask(taskRequest);
    return new ResponseEntity<>(task, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") String id) {
    TaskResponse task = taskService.getTaskById(id);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

  @GetMapping("/user/{uid}")
  public ResponseEntity<List<TaskResponse>> getAllTasksByUserId(@PathVariable("uid") String userId) {
    List<TaskResponse> tasks = taskService.getAllTasksByUserId(userId);
    return new ResponseEntity<>(tasks, HttpStatus.OK);
  }

  @GetMapping("/household/{hid}")
  public ResponseEntity<List<TaskResponse>> getAllTasksByHouseholdId(@PathVariable("hid") String householdId) {
    List<TaskResponse> tasks = taskService.getAllTasksByHouseholdId(householdId);
    return new ResponseEntity<>(tasks, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable("id") String id,
      @RequestBody TaskEditMetadataRequest taskRequest) {
    TaskResponse task = taskService.updateTask(id, taskRequest);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

  @PutMapping("/{id}/assign/{uid}")
  public ResponseEntity<TaskResponse> assignTask(@PathVariable("id") String id,
      @PathVariable("uid") String userId) {
    TaskResponse task = taskService.assignTask(id, userId);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

  @PutMapping("/{id}/unassign/{uid}")
  public ResponseEntity<TaskResponse> unassignTask(@PathVariable("id") String id,
      @PathVariable("uid") String userId) {
    TaskResponse task = taskService.unassignTask(id, userId);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable("id") String id,
      @RequestBody TaskEditStatusRequest taskRequest) {
    TaskResponse task = taskService.updateTaskStatus(id, taskRequest);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteTask(@PathVariable("id") String id) {
    taskService.deleteTask(id);
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("message", "Task deleted successfully");
    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }
}
