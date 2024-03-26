package com.choresync.userhousehold.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.userhousehold.entity.Userhousehold;
import com.choresync.userhousehold.exception.UserhouseholdInternalCommunicationException;
import com.choresync.userhousehold.exception.UserhouseholdInvalidBodyException;
import com.choresync.userhousehold.exception.UserhouseholdInvalidParamException;
import com.choresync.userhousehold.exception.UserhouseholdNotFoundException;
import com.choresync.userhousehold.exception.UserhouseholdUserInHouseholdException;
import com.choresync.userhousehold.external.exception.HouseholdCreationException;
import com.choresync.userhousehold.external.exception.HouseholdNotFoundException;
import com.choresync.userhousehold.external.exception.UserNotFoundException;
import com.choresync.userhousehold.external.request.HouseholdRequest;
import com.choresync.userhousehold.external.response.HouseholdResponse;
import com.choresync.userhousehold.external.response.UserResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdResponse.Household;
import com.choresync.userhousehold.model.GetMembersResponse;
import com.choresync.userhousehold.repository.UserhouseholdRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserhouseholdServiceImpl implements UserhouseholdService {
  @Autowired
  private UserhouseholdRepository userhouseholdRepository;

  @Autowired
  private RestTemplate restTemplate;

  /*
   * Extracts the error message from a RestClientException
   * 
   * @param e
   * 
   * @return String
   */
  @Override
  public String extractErrorMessage(RestClientException e) {
    String rawMessage = e.getMessage();

    try {
      String jsonSubstring = rawMessage.substring(rawMessage.indexOf("{"), rawMessage.lastIndexOf("}") + 1);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(jsonSubstring);

      if (rootNode.has("message")) {
        return rootNode.get("message").asText();
      }
    } catch (JsonProcessingException ex) {
      System.out.println("Error parsing JSON from exception message: " + ex.getMessage());
    } catch (StringIndexOutOfBoundsException ex) {
      System.out.println("Error extracting JSON substring from exception message: " + ex.getMessage());
    }
    return rawMessage;
  }

  /*
   * Creates a new userhousehold
   * 
   * @param userhouseholdRequest
   * 
   * @return UserhouseholdResponse
   * 
   * @throws UserhouseholdInvalidBodyException
   * 
   * @throws UserNotFoundException
   * 
   * @throws UserhouseholdInternalCommunicationException
   * 
   * @throws HouseholdCreationException
   */
  @Override
  public UserhouseholdResponse createUserhousehold(UserhouseholdRequest userhouseholdRequest) {
    if (userhouseholdRequest.getName().isBlank() || userhouseholdRequest.getName() == null
        || userhouseholdRequest.getUserId().isBlank() || userhouseholdRequest.getUserId() == null) {
      throw new UserhouseholdInvalidBodyException("Invalid request body");
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + userhouseholdRequest.getUserId(),
          UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("User not found");
      }
    } catch (RestClientException e) {
      throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
    }

    HouseholdRequest householdResquest = HouseholdRequest
        .builder()
        .name(userhouseholdRequest.getName())
        .build();

    HouseholdResponse fetchedHousehold;

    try {
      fetchedHousehold = restTemplate.postForObject(
          "http://household-service/api/v1/household",
          householdResquest,
          HouseholdResponse.class);

      if (fetchedHousehold == null) {
        throw new HouseholdCreationException("Household not created");
      }
    } catch (RestClientException e) {
      throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
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

  /*
   * Gets a userhousehold by id
   * 
   * @param id
   * 
   * @return UserhouseholdResponse
   * 
   * @throws UserhouseholdInvalidParamException
   * 
   * @throws UserhouseholdNotFoundException
   * 
   * @throws UserhouseholdInternalCommunicationException
   * 
   * @throws HouseholdNotFoundException
   */
  @Override
  public UserhouseholdResponse getUserhouseholdById(String id) {
    if (id.isBlank() || id == null) {
      throw new UserhouseholdInvalidParamException("Invalid request param");
    }

    Userhousehold userhousehold = userhouseholdRepository.findById(id).orElseThrow(
        () -> new UserhouseholdNotFoundException("Userhousehold not found"));

    HouseholdResponse householdResponse;

    try {
      householdResponse = restTemplate
          .getForObject(
              "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
              HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Household not found");
      }
    } catch (RestClientException e) {
      throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
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

  /*
   * Gets userhouseholds by user id
   * 
   * @param userId
   * 
   * @return List<UserhouseholdResponse>
   * 
   * @throws UserhouseholdInvalidParamException
   * 
   * @throws UserNotFoundException
   * 
   * @throws UserhouseholdInternalCommunicationException
   * 
   * @throws HouseholdNotFoundException
   */
  @Override
  public List<UserhouseholdResponse> getUserhouseholdsByUserId(String userId) {
    if (userId.isBlank() || userId == null) {
      throw new UserhouseholdInvalidParamException("Invalid request param");
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + userId,
          UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("User not found");
      }
    } catch (RestClientException e) {
      throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
    }

    List<Userhousehold> userhouseholds = userhouseholdRepository.findAllByUserId(userId);

    List<UserhouseholdResponse> userhouseholdResponses = new ArrayList<>();

    for (Userhousehold userhousehold : userhouseholds) {
      HouseholdResponse householdResponse;

      try {
        householdResponse = restTemplate
            .getForObject(
                "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
                HouseholdResponse.class);

        if (householdResponse == null) {
          throw new HouseholdNotFoundException("Household not found");
        }
      } catch (RestClientException e) {
        throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
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

  /*
   * Gets userhouseholds by household id
   * 
   * @param householdId
   * 
   * @return List<GetMembersResponse>
   * 
   * @throws UserhouseholdInvalidParamException
   * 
   * @throws HouseholdNotFoundException
   * 
   * @throws UserhouseholdInternalCommunicationException
   */
  @Override
  public List<GetMembersResponse> getUserhouseholdsByHouseholdId(String householdId) {
    if (householdId.isBlank() || householdId == null) {
      throw new UserhouseholdInvalidParamException("Invalid request param");
    }

    try {
      HouseholdResponse householdResponse = restTemplate
          .getForObject(
              "http://household-service/api/v1/household/" + householdId,
              HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Household not found");
      }
    } catch (RestClientException e) {
      throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
    }

    List<Userhousehold> userhouseholds = userhouseholdRepository.findAllByHouseholdId(householdId);

    List<GetMembersResponse> userhouseholdResponses = new ArrayList<>();

    for (Userhousehold userhousehold : userhouseholds) {
      HouseholdResponse householdResponse;

      try {
        householdResponse = restTemplate.getForObject(
            "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
            HouseholdResponse.class);

        if (householdResponse == null) {
          throw new HouseholdNotFoundException("Household not found");
        }
      } catch (RestClientException e) {
        throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
      }

      UserResponse userResponse;

      try {
        userResponse = restTemplate.getForObject(
            "http://user-service/api/v1/user/" + userhousehold.getUserId(),
            UserResponse.class);

        if (userResponse == null) {
          throw new UserNotFoundException("User not found");
        }
      } catch (RestClientException e) {
        throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
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

  /*
   * Deletes a userhousehold by id
   * 
   * @param id
   * 
   * @throws UserhouseholdInvalidParamException
   * 
   * @throws UserhouseholdNotFoundException
   */
  @Override
  public void deleteUserhouseholdById(String id) {
    if (id.isBlank() || id == null) {
      throw new UserhouseholdInvalidParamException("Invalid request param");
    }

    if (!userhouseholdRepository.existsById(id)) {
      throw new UserhouseholdNotFoundException("Userhousehold not found");
    }

    userhouseholdRepository.deleteById(id);
  }

  /*
   * Joins a user to a household
   * 
   * @param userId
   * 
   * @param houseId
   * 
   * @return Household
   * 
   * @throws UserhouseholdInvalidParamException
   * 
   * @throws UserNotFoundException
   * 
   * @throws UserhouseholdInternalCommunicationException
   * 
   * @throws UserhouseholdUserInHouseholdException
   * 
   * @throws HouseholdNotFoundException
   */
  @Override
  public Household joinHouseHold(String userId, String houseId) {
    if (userId.isBlank() || userId == null || houseId.isBlank() || houseId == null) {
      throw new UserhouseholdInvalidParamException("Invalid request param");
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + userId,
          UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("User not found");
      }
    } catch (RestClientException e) {
      throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
    }

    HouseholdResponse householdResponse;

    try {
      householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + houseId,
          HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Household not found");
      }
    } catch (RestClientException e) {
      throw new UserhouseholdInternalCommunicationException(extractErrorMessage(e));
    }

    UserhouseholdResponse.Household household = UserhouseholdResponse.Household
        .builder()
        .id(householdResponse.getId())
        .name(householdResponse.getName())
        .createdAt(householdResponse.getCreatedAt())
        .updatedAt(householdResponse.getUpdatedAt())
        .build();

    List<Userhousehold> userhouseholds = userhouseholdRepository.findAllByUserId(userId);

    for (Userhousehold userhousehold : userhouseholds) {
      if (userhousehold.getHouseholdId().equals(houseId)) {
        throw new UserhouseholdUserInHouseholdException("User already in household");
      }
    }

    Userhousehold userHousehold = Userhousehold
        .builder()
        .userId(userId)
        .householdId(houseId)
        .build();

    userhouseholdRepository.save(userHousehold);

    return household;
  }
}