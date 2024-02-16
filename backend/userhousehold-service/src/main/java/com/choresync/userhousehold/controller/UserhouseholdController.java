package com.choresync.userhousehold.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.service.UserhouseholdService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/userhousehold")
public class UserhouseholdController {
  @Autowired
  private UserhouseholdService userhouseholdService;

  @PostMapping
  public ResponseEntity<String> createUserhousehold(@RequestBody UserhouseholdRequest userhouseholdRequest) {
    String id = userhouseholdService.createUserhousehold(userhouseholdRequest);
    return new ResponseEntity<>(id, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserhouseholdResponse> getUserhouseholdById(@PathVariable("id") String id) {
    UserhouseholdResponse userhouseholdResponse = userhouseholdService.getUserhouseholdById(id);
    return new ResponseEntity<>(userhouseholdResponse, HttpStatus.OK);
  }

}
