package com.choresync.userhousehold.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.choresync.userhousehold.entity.Userhousehold;
import com.choresync.userhousehold.external.request.HouseholdResquest;
import com.choresync.userhousehold.external.response.HouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.repository.UserhouseholdRepository;

@Service
public class UserhouseholdServiceImpl implements UserhouseholdService {
  @Autowired
  private UserhouseholdRepository userhouseholdRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public String createUserhousehold(UserhouseholdRequest userhouseholdRequest) {

    HouseholdResquest householdResquest = HouseholdResquest
        .builder()
        .name(userhouseholdRequest.getName())
        .build();

    String householdId = restTemplate
        .postForObject(
            "http://household-service/api/v1/household",
            householdResquest,
            String.class);

    Userhousehold userhousehold = Userhousehold
        .builder()
        .householdId(householdId)
        .userId(userhouseholdRequest.getUserId())
        .build();

    Userhousehold newUserhousehold = userhouseholdRepository.save(userhousehold);

    return newUserhousehold.getId();
  }

  @Override
  public UserhouseholdResponse getUserhouseholdById(String id) {
    Userhousehold userhousehold = userhouseholdRepository.findById(id).orElse(null);

    HouseholdResponse householdResponse = restTemplate
        .getForObject(
            "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
            HouseholdResponse.class);

    UserhouseholdResponse.Household household = UserhouseholdResponse.Household
        .builder()
        .id(householdResponse.getId())
        .name(householdResponse.getName())
        .createdAt(householdResponse.getCreatedAt())
        .updatedAt(householdResponse.getUpdatedAt())
        .build();

    UserhouseholdResponse userhouseholdResponse = UserhouseholdResponse
        .builder()
        .id(userhousehold.getId())
        .userId(userhousehold.getUserId())
        .household(household)
        .createdAt(userhousehold.getCreatedAt())
        .updatedAt(userhousehold.getUpdatedAt())
        .build();

    return userhouseholdResponse;
  }

  @Override
  public List<UserhouseholdResponse> getUserhouseholdsByUserId(String userId) {
    List<Userhousehold> userhouseholds = userhouseholdRepository.findAllByUserId(userId);

    List<UserhouseholdResponse> userhouseholdResponses = new ArrayList<>();

    for (Userhousehold userhousehold : userhouseholds) {
      HouseholdResponse householdResponse = restTemplate
          .getForObject(
              "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
              HouseholdResponse.class);

      UserhouseholdResponse.Household household = UserhouseholdResponse.Household
          .builder()
          .id(householdResponse.getId())
          .name(householdResponse.getName())
          .createdAt(householdResponse.getCreatedAt())
          .updatedAt(householdResponse.getUpdatedAt())
          .build();

      UserhouseholdResponse userhouseholdResponse = UserhouseholdResponse
          .builder()
          .id(userhousehold.getId())
          .userId(userhousehold.getUserId())
          .household(household)
          .createdAt(userhousehold.getCreatedAt())
          .updatedAt(userhousehold.getUpdatedAt())
          .build();

      userhouseholdResponses.add(userhouseholdResponse);
    }

    return userhouseholdResponses;
  }

  @Override
  public List<UserhouseholdResponse> getUserhouseholdsByHouseholdId(String householdId) {
    List<Userhousehold> userhouseholds = userhouseholdRepository.findAllByHouseholdId(householdId);

    List<UserhouseholdResponse> userhouseholdResponses = new ArrayList<>();

    for (Userhousehold userhousehold : userhouseholds) {
      HouseholdResponse householdResponse = restTemplate
          .getForObject(
              "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
              HouseholdResponse.class);

      UserhouseholdResponse.Household household = UserhouseholdResponse.Household
          .builder()
          .id(householdResponse.getId())
          .name(householdResponse.getName())
          .createdAt(householdResponse.getCreatedAt())
          .updatedAt(householdResponse.getUpdatedAt())
          .build();

      UserhouseholdResponse userhouseholdResponse = UserhouseholdResponse
          .builder()
          .id(userhousehold.getId())
          .userId(userhousehold.getUserId())
          .household(household)
          .createdAt(userhousehold.getCreatedAt())
          .updatedAt(userhousehold.getUpdatedAt())
          .build();

      userhouseholdResponses.add(userhouseholdResponse);
    }

    return userhouseholdResponses;
  }
}