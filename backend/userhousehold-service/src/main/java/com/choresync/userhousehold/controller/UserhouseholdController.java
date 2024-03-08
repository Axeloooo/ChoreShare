package com.choresync.userhousehold.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.service.UserhouseholdService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/userhousehold")
public class UserhouseholdController {
  @Autowired
  private UserhouseholdService userhouseholdService;

  @PostMapping
  public ResponseEntity<UserhouseholdResponse> createUserhousehold(
      @RequestBody UserhouseholdRequest userhouseholdRequest) {
    UserhouseholdResponse userhousehold = userhouseholdService.createUserhousehold(userhouseholdRequest);
    return new ResponseEntity<>(userhousehold, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserhouseholdResponse> getUserhouseholdsById(@PathVariable("id") String id) {
    UserhouseholdResponse userhouseholdResponse = userhouseholdService.getUserhouseholdById(id);
    return new ResponseEntity<>(userhouseholdResponse, HttpStatus.OK);
  }

  @GetMapping("/user/{uid}")
  public ResponseEntity<List<UserhouseholdResponse>> getUserhouseholdsByUserId(@PathVariable("uid") String userId) {
    List<UserhouseholdResponse> userhouseholdResponse = userhouseholdService.getUserhouseholdsByUserId(userId);
    return new ResponseEntity<>(userhouseholdResponse, HttpStatus.OK);
  }

  @GetMapping("/household/{hid}")
  public ResponseEntity<List<UserhouseholdResponse>> getUserhouseholdsByHouseholdId(
      @PathVariable("hid") String householdId) {
    List<UserhouseholdResponse> userhouseholdResponse = userhouseholdService
        .getUserhouseholdsByHouseholdId(householdId);
    return new ResponseEntity<>(userhouseholdResponse, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUserhouseholdById(@PathVariable("id") String id) {
    userhouseholdService.deleteUserhouseholdById(id);

    return new ResponseEntity<>("Userhousehold with id " + id + " has been deleted", HttpStatus.OK);
  }
}
