package com.choresync.userhousehold.service;

import java.util.List;

import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;

public interface UserhouseholdService {

  UserhouseholdResponse createUserhousehold(UserhouseholdRequest userhouseholdRequest);

  UserhouseholdResponse getUserhouseholdById(String id);

  List<UserhouseholdResponse> getUserhouseholdsByUserId(String userId);

  List<UserhouseholdResponse> getUserhouseholdsByHouseholdId(String householdId);

  void deleteUserhouseholdById(String id);
}
