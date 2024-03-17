package com.choresync.userhousehold.service;

import java.util.List;

import com.choresync.userhousehold.external.response.UserResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdResponse.Household;

public interface UserhouseholdService {

  UserhouseholdResponse createUserhousehold(UserhouseholdRequest userhouseholdRequest);

  UserhouseholdResponse getUserhouseholdById(String id);

  List<UserhouseholdResponse> getUserhouseholdsByUserId(String userId);

  List<UserhouseholdResponse> getUserhouseholdsByHouseholdId(String householdId);

  Household joinHouseHold(String userId, String houseId);

  void deleteUserhouseholdById(String id);
}
