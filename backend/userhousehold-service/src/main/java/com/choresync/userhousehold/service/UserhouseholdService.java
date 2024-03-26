package com.choresync.userhousehold.service;

import java.util.List;

import org.springframework.web.client.RestClientException;

import com.choresync.userhousehold.model.GetMembersResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdResponse.Household;

public interface UserhouseholdService {
  String extractErrorMessage(RestClientException e);

  UserhouseholdResponse createUserhousehold(UserhouseholdRequest userhouseholdRequest);

  UserhouseholdResponse getUserhouseholdById(String id);

  List<UserhouseholdResponse> getUserhouseholdsByUserId(String userId);

  List<GetMembersResponse> getUserhouseholdsByHouseholdId(String householdId);

  Household joinHouseHold(String userId, String houseId);

  void deleteUserhouseholdById(String id);
}
