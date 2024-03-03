package com.example.household.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.choresync.household.entity.Household;
import com.choresync.household.model.HouseholdRequest;
import com.choresync.household.model.HouseholdResponse;
import com.choresync.household.repository.HouseholdRepository;
import com.choresync.household.service.HouseholdServiceImpl;

public class HouseholdServiceImplTest {

  @Mock
  private HouseholdRepository householdRepository;

  @InjectMocks
  private HouseholdServiceImpl householdService;

  private Household household;
  private HouseholdRequest householdRequest;
  private HouseholdResponse householdResponse;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    household = Household.builder()
        .id("1")
        .name("Doe Family")
        .build();

    householdRequest = HouseholdRequest.builder()
        .name("Doe Family")
        .build();

    householdResponse = HouseholdResponse.builder()
        .id("1")
        .name("Doe Family")
        .createdAt(household.getCreatedAt())
        .updatedAt(household.getUpdatedAt())
        .build();
  }

  @Test
  public void testCreateHousehold() {
    when(householdRepository.save(any(Household.class))).thenReturn(household);

    String householdId = householdService.createHousehold(householdRequest);

    assertNotNull(householdId);
    assertEquals(household.getId(), householdId);
  }

  @Test
  public void testGetHouseholdById() {
    when(householdRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(household));

    HouseholdResponse result = householdService.getHouseholdById("1");

    assertNotNull(result);
    assertEquals(householdResponse, result);
  }

  @Test
  public void testGetAllHouseholds() {
    when(householdRepository.findAll()).thenReturn(Arrays.asList(household));

    List<HouseholdResponse> result = householdService.getAllHouseholds();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(householdResponse, result.get(0));
  }
}
