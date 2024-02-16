package com.choresync.household.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.household.entity.Household;
import com.choresync.household.model.HouseholdRequest;
import com.choresync.household.model.HouseholdResponse;
import com.choresync.household.repository.HouseholdRepository;

@Service
public class HouseholdServiceImpl implements HouseholdService {
  @Autowired
  private HouseholdRepository householdRepository;

  @Override
  public String createHousehold(HouseholdRequest householdRequest) {
    Household household = Household.builder()
        .name(householdRequest.getName())
        .build();

    householdRepository.save(household);

    return household.getId();
  }

  @Override
  public HouseholdResponse getHouseholdById(String id) {
    Household household = householdRepository.findById(id).orElse(null);

    HouseholdResponse householdResponse = HouseholdResponse.builder()
        .id(household.getId())
        .name(household.getName())
        .createdAt(household.getCreatedAt())
        .updatedAt(household.getUpdatedAt())
        .build();

    return householdResponse;
  }

}
