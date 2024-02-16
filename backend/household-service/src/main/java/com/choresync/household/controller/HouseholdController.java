package com.choresync.household.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.household.model.HouseholdRequest;
import com.choresync.household.model.HouseholdResponse;
import com.choresync.household.service.HouseholdService;

import brave.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/household")
public class HouseholdController {
  @Autowired
  private HouseholdService householdService;

  @PostMapping
  public ResponseEntity<String> createHousehold(@RequestBody HouseholdRequest householdRequest) {
    String id = householdService.createHousehold(householdRequest);
    return new ResponseEntity<>(id, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<HouseholdResponse> getHouseholdById(@PathVariable("id") String id) {
    HouseholdResponse householdResponse = householdService.getHouseholdById(id);
    return new ResponseEntity<>(householdResponse, HttpStatus.OK);
  }

}
