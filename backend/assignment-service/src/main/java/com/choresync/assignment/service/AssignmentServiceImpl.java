package com.choresync.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.assignment.entity.Assignment;
import com.choresync.assignment.model.AssignmentRequest;
import com.choresync.assignment.model.AssignmentResponse;
import com.choresync.assignment.repository.AssignmentRepository;

@Service
public class AssignmentServiceImpl implements AssignmentService {
  @Autowired
  private AssignmentRepository assignmentRepository;

  @Override
  public String createAssignment(AssignmentRequest assignmentRequest) {
    Assignment assignment = Assignment.builder()
        .assignmentDate(assignmentRequest.getAssignmentDate())
        .completionDate(assignmentRequest.getCompletionDate())
        .huseholdId(assignmentRequest.getHuseholdId())
        .taskId(assignmentRequest.getTaskId())
        .userId(assignmentRequest.getUserId())
        .build();

    assignmentRepository.save(assignment);

    return assignment.getId();
  }

  @Override
  public AssignmentResponse getAssignmentById(String id) {
    Assignment assignment = assignmentRepository.findById(id).orElse(null);

    AssignmentResponse assignmentResponse = AssignmentResponse.builder()
        .id(assignment.getId())
        .assignmentDate(assignment.getAssignmentDate())
        .completionDate(assignment.getCompletionDate())
        .huseholdId(assignment.getHuseholdId())
        .taskId(assignment.getTaskId())
        .userId(assignment.getUserId())
        .createdAt(assignment.getCreatedAt())
        .updatedAt(assignment.getUpdatedAt())
        .build();

    return assignmentResponse;
  }

}
