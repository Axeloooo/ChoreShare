package com.choresync.household.service;

import java.util.ArrayList;
import java.util.List;

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

    Household newHousehold = householdRepository.save(household);

    return newHousehold.getId();
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

  @Override
  public List<HouseholdResponse> getAllHouseholds() {
    List<Household> households = householdRepository.findAll();

    List<HouseholdResponse> householdResponses = new ArrayList<>();

    for (Household household : households) {
      HouseholdResponse householdResponse = HouseholdResponse.builder()
          .id(household.getId())
          .name(household.getName())
          .createdAt(household.getCreatedAt())
          .updatedAt(household.getUpdatedAt())
          .build();

      householdResponses.add(householdResponse);
    }

    return householdResponses;
  }
}
