package com.choresync.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.choresync.assignment.entity.Assignment;
import com.choresync.assignment.external.request.TaskRequest;
import com.choresync.assignment.external.response.TaskResponse;
import com.choresync.assignment.model.AssignmentRequest;
import com.choresync.assignment.model.AssignmentResponse;
import com.choresync.assignment.repository.AssignmentRepository;

@Service
public class AssignmentServiceImpl implements AssignmentService {
  @Autowired
  private AssignmentRepository assignmentRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public String createAssignment(AssignmentRequest assignmentRequest) {

    TaskRequest taskRequest = TaskRequest
        .builder()
        .name(assignmentRequest.getName())
        .description(assignmentRequest.getDescription())
        .dueDate(assignmentRequest.getDueDate())
        .build();

    String taskId = restTemplate
        .postForObject(
            "http://task-service/api/v1/task",
            taskRequest,
            String.class);

    Assignment assignment = Assignment
        .builder()
        .householdId(assignmentRequest.getHouseholdId())
        .taskId(taskId)
        .userId(assignmentRequest.getUserId())
        .build();

    Assignment newAssignment = assignmentRepository.save(assignment);

    return newAssignment.getId();
  }

  @Override
  public AssignmentResponse getAssignmentById(String id) {
    Assignment assignment = assignmentRepository.findById(id).orElse(null);

    TaskResponse taskResponse = restTemplate
        .getForObject(
            "http://task-service/api/v1/task/" + assignment.getTaskId(),
            TaskResponse.class);

    AssignmentResponse.Task task = AssignmentResponse.Task
        .builder()
        .id(taskResponse.getId())
        .name(taskResponse.getName())
        .description(taskResponse.getDescription())
        .dueDate(taskResponse.getDueDate())
        .createdAt(taskResponse.getCreatedAt())
        .updatedAt(taskResponse.getUpdatedAt())
        .build();

    AssignmentResponse assignmentResponse = AssignmentResponse
        .builder()
        .id(assignment.getId())
        .userId(assignment.getUserId())
        .householdId(assignment.getHouseholdId())
        .task(task)
        .createdAt(assignment.getCreatedAt())
        .updatedAt(assignment.getUpdatedAt())
        .build();

    return assignmentResponse;
  }

}
