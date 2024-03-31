package com.choresync.household.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.household.entity.Household;
import com.choresync.household.exception.HouseholdInvalidBodyException;
import com.choresync.household.exception.HouseholdInvalidParamException;
import com.choresync.household.exception.HouseholdNotFoundException;
import com.choresync.household.model.HouseholdRequest;
import com.choresync.household.model.HouseholdResponse;
import com.choresync.household.repository.HouseholdRepository;

@Service
public class HouseholdServiceImpl implements HouseholdService {
  @Autowired
  private HouseholdRepository householdRepository;

  /*
   * This method creates a household
   * 
   * @param householdRequest
   * 
   * @return HouseholdResponse
   * 
   * @throws HouseholdInvalidBodyException
   */
  @Override
  public HouseholdResponse createHousehold(HouseholdRequest householdRequest) {
    if (householdRequest.getName() == null) {
      throw new HouseholdInvalidBodyException("Invalid request body");
    }

    Household household = Household
        .builder()
        .name(householdRequest.getName())
        .build();

    Household newHousehold = householdRepository.save(household);

    HouseholdResponse householdResponse = HouseholdResponse
        .builder()
        .id(newHousehold.getId())
        .name(newHousehold.getName())
        .createdAt(newHousehold.getCreatedAt())
        .updatedAt(newHousehold.getUpdatedAt())
        .build();

    return householdResponse;
  }

  /*
   * This method returns a household by id
   * 
   * @param id
   * 
   * @return HouseholdResponse
   * 
   * @throws HouseholdInvalidParamException
   * 
   * @throws HouseholdNotFoundException
   */
  @Override
  public HouseholdResponse getHouseholdById(String id) {
    if (id == null) {
      throw new HouseholdInvalidParamException("Invalid requets parameter");
    }

    Household household = householdRepository.findById(id)
        .orElseThrow(() -> new HouseholdNotFoundException("Household not found"));

    HouseholdResponse householdResponse = HouseholdResponse.builder()
        .id(household.getId())
        .name(household.getName())
        .createdAt(household.getCreatedAt())
        .updatedAt(household.getUpdatedAt())
        .build();

    return householdResponse;
  }

  /*
   * This method returns a list of all households
   * 
   * @return List<HouseholdResponse>
   */
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

  /*
   * This method deletes a household by id
   * 
   * @param id
   * 
   * @throws HouseholdInvalidParamException
   * 
   * @throws HouseholdNotFoundException
   */
  @Override
  public void deleteHousehold(String id) {
    if (id == null) {
      throw new HouseholdInvalidParamException("Invalid requets parameter");
    }

    if (!householdRepository.existsById(id)) {
      throw new HouseholdNotFoundException("Household not found");
    }

    householdRepository.deleteById(id);
  }
}
