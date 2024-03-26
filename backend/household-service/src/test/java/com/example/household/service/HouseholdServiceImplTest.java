package com.example.household.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;

import com.choresync.household.entity.Household;
import com.choresync.household.exception.HouseholdInvalidBodyException;
import com.choresync.household.exception.HouseholdNotFoundException;
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
        .createdAt(new Date())
        .updatedAt(new Date())
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

  @Description("POST /api/v1/household - Test create household")
  @Test
  public void testCreateHousehold() {
    when(householdRepository.save(any(Household.class)))
        .thenReturn(household);

    HouseholdResponse result = householdService.createHousehold(householdRequest);

    assertNotNull(result);
    assertEquals(householdResponse, result);

    verify(householdRepository, times(1)).save(any(Household.class));
  }

  @Description("POST /api/v1/household - Test HouseholdCreationException when household request is null")
  @Test
  public void testCreateHouseholdWithNullRequest() {
    assertThrows(HouseholdInvalidBodyException.class, () -> householdService.createHousehold(null));

    verify(householdRepository, times(0)).save(any(Household.class));
  }

  @Description("GET /api/v1/household/{id} - Test get household by id")
  @Test
  public void testGetHouseholdById() {
    when(householdRepository.findById("1"))
        .thenReturn(Optional.ofNullable(household));

    HouseholdResponse result = householdService.getHouseholdById("1");

    assertNotNull(result);
    assertEquals(householdResponse, result);

    verify(householdRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/household/{id} - Test HouseholdNotFoundException when household is not found")
  @Test
  public void testGetHouseholdByIdNotFound() {
    assertThrows(HouseholdNotFoundException.class, () -> householdService.getHouseholdById("1"));

    verify(householdRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/household - Test get all households")
  @Test
  public void testGetAllHouseholds() {
    when(householdRepository.findAll())
        .thenReturn(Arrays.asList(household));

    List<HouseholdResponse> result = householdService.getAllHouseholds();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(householdResponse, result.get(0));
  }

  @Description("GET /api/v1/household - Test get all empty households")
  @Test
  public void testGetAllHouseholdsEmpty() {
    when(householdRepository.findAll())
        .thenReturn(Arrays.asList());

    List<HouseholdResponse> result = householdService.getAllHouseholds();

    assertNotNull(result);
    assertEquals(0, result.size());
  }
}
