package com.choresync.userhousehold.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import com.choresync.userhousehold.entity.Userhousehold;
import com.choresync.userhousehold.exception.HouseholdCreationException;
import com.choresync.userhousehold.exception.HouseholdNotFoundException;
import com.choresync.userhousehold.exception.UserNotFoundException;
import com.choresync.userhousehold.exception.UserhouseholdCreationException;
import com.choresync.userhousehold.exception.UserhouseholdNotFoundException;
import com.choresync.userhousehold.external.request.HouseholdRequest;
import com.choresync.userhousehold.external.response.HouseholdResponse;
import com.choresync.userhousehold.external.response.UserResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdResponse.Household;
import com.choresync.userhousehold.model.GetMembersResponse;
import com.choresync.userhousehold.model.GetMembersResponse.User;
import com.choresync.userhousehold.repository.UserhouseholdRepository;

@Service
public class UserhouseholdServiceImpl implements UserhouseholdService {
  @Autowired
  private UserhouseholdRepository userhouseholdRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public UserhouseholdResponse createUserhousehold(UserhouseholdRequest userhouseholdRequest) {
    if (userhouseholdRequest.getName() == null || userhouseholdRequest.getUserId() == null) {
      throw new UserhouseholdCreationException("Invalid request body");
    }

    HouseholdRequest householdResquest = HouseholdRequest
        .builder()
        .name(userhouseholdRequest.getName())
        .build();

    HouseholdResponse fetchedHousehold;

    try {
      fetchedHousehold = restTemplate
          .postForObject(
              "http://household-service/api/v1/household",
              householdResquest,
              HouseholdResponse.class);
    } catch (RestClientException e) {
      throw new HouseholdCreationException("Failed to create household. " + e.getMessage());
    }

    Userhousehold userhousehold = Userhousehold
        .builder()
        .userId(userhouseholdRequest.getUserId())
        .householdId(fetchedHousehold.getId())
        .build();

    Userhousehold newUserhousehold = userhouseholdRepository.save(userhousehold);

    UserhouseholdResponse.Household household = UserhouseholdResponse.Household
        .builder()
        .id(fetchedHousehold.getId())
        .name(fetchedHousehold.getName())
        .createdAt(fetchedHousehold.getCreatedAt())
        .updatedAt(fetchedHousehold.getUpdatedAt())
        .build();

    UserhouseholdResponse userhouseholdResponse = UserhouseholdResponse
        .builder()
        .id(newUserhousehold.getId())
        .userId(newUserhousehold.getUserId())
        .household(household)
        .createdAt(newUserhousehold.getCreatedAt())
        .updatedAt(newUserhousehold.getUpdatedAt())
        .build();

    return userhouseholdResponse;
  }

  @Override
  public UserhouseholdResponse getUserhouseholdById(String id) {
    Userhousehold userhousehold = userhouseholdRepository.findById(id).orElseThrow(
        () -> new UserhouseholdNotFoundException("Userhousehold not found"));

    HouseholdResponse householdResponse;

    try {
      householdResponse = restTemplate
          .getForObject(
              "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
              HouseholdResponse.class);
    } catch (RestClientException e) {
      throw new HouseholdNotFoundException("Household not found. " + e.getMessage());
    }

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
      HouseholdResponse householdResponse;

      try {
        householdResponse = restTemplate
            .getForObject(
                "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
                HouseholdResponse.class);
      } catch (RestClientException e) {
        throw new HouseholdNotFoundException("Household not found. " + e.getMessage());
      }

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
  public List<GetMembersResponse> getUserhouseholdsByHouseholdId(String householdId) {
    List<Userhousehold> userhouseholds = userhouseholdRepository.findAllByHouseholdId(householdId);

    List<GetMembersResponse> userhouseholdResponses = new ArrayList<>();

    for (Userhousehold userhousehold : userhouseholds) {
      HouseholdResponse householdResponse;

      try {
        householdResponse = restTemplate
            .getForObject(
                "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
                HouseholdResponse.class);
      } catch (RestClientException e) {
        throw new HouseholdNotFoundException("Household not found. " + e.getMessage());
      }

      UserResponse userResponse;

      try {
        userResponse = restTemplate
            .getForObject(
                "http://user-service/api/v1/user/" + userhousehold.getUserId(),
                UserResponse.class);
      } catch (RestClientException e) {
        throw new UserNotFoundException("User not found. " + e.getMessage());
      }

      GetMembersResponse.Household household = GetMembersResponse.Household
          .builder()
          .id(householdResponse.getId())
          .name(householdResponse.getName())
          .createdAt(householdResponse.getCreatedAt())
          .updatedAt(householdResponse.getUpdatedAt())
          .build();

      GetMembersResponse.User user = GetMembersResponse.User
          .builder()
          .id(userResponse.getId())
          .firstName(userResponse.getFirstName())
          .lastName(userResponse.getLastName())
          .username(userResponse.getUsername())
          .createdAt(userhousehold.getCreatedAt())
          .updatedAt(userhousehold.getUpdatedAt())
          .build();

      GetMembersResponse userhouseholdResponse = GetMembersResponse
          .builder()
          .user(user)
          .household(household)
          .createdAt(userhousehold.getCreatedAt())
          .updatedAt(userhousehold.getUpdatedAt())
          .build();

      userhouseholdResponses.add(userhouseholdResponse);
    }

    return userhouseholdResponses;
  }

  @Override
  public void deleteUserhouseholdById(String id) {
    Userhousehold userhousehold = userhouseholdRepository.findById(id).orElseThrow(
        () -> new UserhouseholdNotFoundException("Userhousehold not found"));

    userhouseholdRepository.delete(userhousehold);
  }

  @Override
  public Household joinHouseHold(String userId, String houseId) {

    HouseholdResponse householdResponse;

    // Throws of houseId does not exist
    try {
      householdResponse = restTemplate
          .getForObject(
              "http://household-service/api/v1/household/" + houseId,
              HouseholdResponse.class);
    } catch (RestClientException e) {
      throw new HouseholdNotFoundException("Household not found. " + e.getMessage());
    }

    UserhouseholdResponse.Household household = UserhouseholdResponse.Household
          .builder()
          .id(householdResponse.getId())
          .name(householdResponse.getName())
          .createdAt(householdResponse.getCreatedAt())
          .updatedAt(householdResponse.getUpdatedAt())
          .build();

    // Make Sure that the user exists
    try {
      restTemplate
          .getForObject(
              "http://user-service/api/v1/user/" + userId,
              UserResponse.class);
    } catch (Exception e) {
      throw new HouseholdNotFoundException("User not found. " + e.getMessage());
    }

    // Check If the user already exists in the house
    List<Userhousehold> userhouseholds = userhouseholdRepository.findAllByUserId(userId);

    for (Userhousehold userhousehold : userhouseholds) {
      if (userhousehold.getHouseholdId().equals(houseId)) {
        throw new HouseholdNotFoundException("User Already In house! ");

      }
    }

    Userhousehold userHousehold = new Userhousehold();
        userHousehold.setUserId(userId);
        userHousehold.setHouseholdId(houseId);
        userHousehold.setCreatedAt(new Date()); // Set the current timestamp for createdAt
        userHousehold.setUpdatedAt(new Date()); // Set the current timestamp for updatedAt

        userhouseholdRepository.save(userHousehold);

    return household;

  }

 
}