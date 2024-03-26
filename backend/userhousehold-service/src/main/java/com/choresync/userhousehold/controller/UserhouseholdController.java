package com.choresync.userhousehold.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.userhousehold.model.GetMembersResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdResponse.Household;
import com.choresync.userhousehold.service.UserhouseholdService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  public ResponseEntity<List<GetMembersResponse>> getUserhouseholdsByHouseholdId(
      @PathVariable("hid") String householdId) {
    List<GetMembersResponse> userhouseholdResponse = userhouseholdService
        .getUserhouseholdsByHouseholdId(householdId);
    return new ResponseEntity<>(userhouseholdResponse, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> deleteUserhouseholdById(@PathVariable("id") String id) {
    userhouseholdService.deleteUserhouseholdById(id);
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("message", "Userhousehold successfully deleted");
    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }

  @PostMapping("/join/{hid}/user/{uid}")
  public ResponseEntity<Household> joinHouseHold(@PathVariable("uid") String userId,
      @PathVariable("hid") String houseId) {
    Household userhouseholdResponse = userhouseholdService.joinHouseHold(userId, houseId);
    return new ResponseEntity<>(userhouseholdResponse, HttpStatus.OK);
  }
}
