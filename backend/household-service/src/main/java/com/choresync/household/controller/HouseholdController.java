package com.choresync.household.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.household.model.HouseholdRequest;
import com.choresync.household.model.HouseholdResponse;
import com.choresync.household.service.HouseholdService;

import java.util.List;

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
  public ResponseEntity<HouseholdResponse> createHousehold(@RequestBody HouseholdRequest householdRequest) {
    HouseholdResponse household = householdService.createHousehold(householdRequest);
    return new ResponseEntity<>(household, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<HouseholdResponse> getHouseholdById(@PathVariable("id") String id) {
    HouseholdResponse householdResponse = householdService.getHouseholdById(id);
    return new ResponseEntity<>(householdResponse, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<HouseholdResponse>> getAllHouseholds() {
    List<HouseholdResponse> householdResponses = householdService.getAllHouseholds();
    return new ResponseEntity<>(householdResponses, HttpStatus.OK);
  }

}
