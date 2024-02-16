package com.choresync.assignment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.assignment.model.AssignmentRequest;
import com.choresync.assignment.model.AssignmentResponse;
import com.choresync.assignment.service.AssignmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/assignment")
public class AssignmentController {
  @Autowired
  private AssignmentService assignmentService;

  @PostMapping
  public ResponseEntity<String> createAssignment(@RequestBody AssignmentRequest assignmentRequest) {
    String id = assignmentService.createAssignment(assignmentRequest);
    return new ResponseEntity<>(id, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AssignmentResponse> getAssignmentById(@PathVariable("id") String id) {
    AssignmentResponse assignmentResponse = assignmentService.getAssignmentById(id);
    return new ResponseEntity<>(assignmentResponse, HttpStatus.OK);
  }

}
