package com.choresync.userhousehold.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.choresync.userhousehold.entity.Userhousehold;
import com.choresync.userhousehold.external.request.HouseholdResquest;
import com.choresync.userhousehold.external.response.HouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.repository.UserhouseholdRepository;

public class UserhouseholdServiceImplTest {

  @Mock
  private UserhouseholdRepository userhouseholdRepository;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private UserhouseholdServiceImpl userhouseholdService;

  private HouseholdResquest householdRequest;
  private Userhousehold userhousehold;
  private UserhouseholdRequest userhouseholdRequest;
  private UserhouseholdResponse userhouseholdResponse;
  private HouseholdResponse householdResponse;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    householdRequest = HouseholdResquest
        .builder()
        .name("Doe Family")
        .build();

    householdResponse = HouseholdResponse
        .builder()
        .id("1")
        .name("Doe Family")
        .build();

    userhousehold = Userhousehold
        .builder()
        .id("1")
        .householdId("1")
        .userId("user1")
        .build();

    userhouseholdRequest = UserhouseholdRequest
        .builder()
        .name("Doe Family")
        .userId("user1")
        .build();

    userhouseholdResponse = UserhouseholdResponse
        .builder()
        .id("1")
        .userId("user1")
        .household(UserhouseholdResponse.Household.builder()
            .id("1")
            .name("Doe Family")
            .build())
        .build();
  }

  @Test
  public void testCreateUserhousehold() {
    when(restTemplate.postForObject(
        "http://household-service/api/v1/household",
        householdRequest,
        String.class))
        .thenReturn("1");

    when(userhouseholdRepository.save(any(Userhousehold.class)))
        .thenReturn(userhousehold);

    String userhouseholdId = userhouseholdService.createUserhousehold(userhouseholdRequest);

    assertNotNull(userhouseholdId);
    assertEquals(userhousehold.getId(), userhouseholdId);
  }

  @Test
  public void testGetUserhouseholdById() {
    when(userhouseholdRepository.findById("1"))
        .thenReturn(Optional.ofNullable(userhousehold));

    when(restTemplate.getForObject(
        "http://household-service/api/v1/household/1",
        HouseholdResponse.class))
        .thenReturn(householdResponse);

    UserhouseholdResponse result = userhouseholdService.getUserhouseholdById("1");

    assertNotNull(result);
    assertEquals(userhouseholdResponse, result);
  }

  @Test
  public void testGetUserhouseholdsByUserId() {
    when(userhouseholdRepository.findAllByUserId("user1"))
        .thenReturn(Arrays.asList(userhousehold));

    when(restTemplate.getForObject(
        "http://household-service/api/v1/household/1",
        HouseholdResponse.class))
        .thenReturn(householdResponse);

    List<UserhouseholdResponse> result = userhouseholdService.getUserhouseholdsByUserId("user1");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(userhouseholdResponse, result.get(0));
  }

  @Test
  public void testGetUserhouseholdsByHouseholdId() {
    when(userhouseholdRepository.findAllByHouseholdId("1"))
        .thenReturn(Arrays.asList(userhousehold));

    when(restTemplate.getForObject(
        "http://household-service/api/v1/household/1",
        HouseholdResponse.class))
        .thenReturn(householdResponse);

    List<UserhouseholdResponse> result = userhouseholdService.getUserhouseholdsByHouseholdId("1");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(userhouseholdResponse, result.get(0));
  }
}
