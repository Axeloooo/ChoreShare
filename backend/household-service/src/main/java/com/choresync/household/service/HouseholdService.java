package com.choresync.household.service;

import java.util.List;

import com.choresync.household.model.HouseholdRequest;
import com.choresync.household.model.HouseholdResponse;

public interface HouseholdService {

  String createHousehold(HouseholdRequest householdRequest);

  HouseholdResponse getHouseholdById(String id);

  List<HouseholdResponse> getAllHouseholds();

}
