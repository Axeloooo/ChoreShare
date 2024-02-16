package com.choresync.assignment.service;

import com.choresync.assignment.model.AssignmentRequest;
import com.choresync.assignment.model.AssignmentResponse;

public interface AssignmentService {

  String createAssignment(AssignmentRequest assignmentRequest);

  AssignmentResponse getAssignmentById(String id);

}
