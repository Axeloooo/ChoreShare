package com.example.household.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
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
import com.choresync.household.exception.HouseholdInvalidParamException;
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

  @Description("POST /api/v1/household - Test create household with invalid body")
  @Test
  public void testCreateHouseholdWithInvalidBody() {
    HouseholdRequest invalidRequest = new HouseholdRequest();
    assertThrows(HouseholdInvalidBodyException.class, () -> householdService.createHousehold(invalidRequest));
  }

  @Description("POST /api/v1/household - Test create household success")
  @Test
  public void testCreateHouseholdSuccess() {
    when(householdRepository.save(any(Household.class))).thenReturn(household);

    HouseholdResponse response = householdService.createHousehold(householdRequest);

    assertNotNull(response);
    assertEquals(householdResponse, response);

    verify(householdRepository, times(1)).save(any(Household.class));
  }

  @Description("GET /api/v1/household/{id} - Test get household by id with invalid param")
  @Test
  public void testGetHouseholdByIdWithInvalidParam() {
    assertThrows(HouseholdInvalidParamException.class, () -> householdService.getHouseholdById(null));
  }

  @Description("GET /api/v1/household/{id} - Test get household by id with household not found")
  @Test
  public void testGetHouseholdByIdWithHouseholdNotFound() {
    when(householdRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(HouseholdNotFoundException.class, () -> householdService.getHouseholdById("1"));
  }

  @Description("GET /api/v1/household/{id} - Test get household by id success")
  @Test
  public void testGetHouseholdByIdSuccess() {
    when(householdRepository.findById(anyString())).thenReturn(Optional.of(household));

    HouseholdResponse response = householdService.getHouseholdById("1");

    assertNotNull(response);
    assertEquals(householdResponse, response);

    verify(householdRepository, times(1)).findById(anyString());

  }

  @Description("GET /api/v1/household - Test get all households when no households exist")
  @Test
  public void testGetAllHouseholdsWhenNoHouseholdsExist() {
    when(householdRepository.findAll()).thenReturn(Collections.emptyList());

    List<HouseholdResponse> response = householdService.getAllHouseholds();

    assertTrue(response.isEmpty());
  }

  @Description("GET /api/v1/household - Test get all households when households exist")
  @Test
  public void testGetAllHouseholdsWhenHouseholdsExist() {
    List<Household> households = Collections.singletonList(household);
    when(householdRepository.findAll()).thenReturn(households);

    List<HouseholdResponse> response = householdService.getAllHouseholds();
    assertNotNull(response);
    assertFalse(response.isEmpty());
    assertEquals(1, response.size());
    assertEquals(householdResponse, response.get(0));

    verify(householdRepository, times(1)).findAll();
  }

  @Description("DELETE /api/v1/household/{id} - Test delete household by id with invalid param")
  @Test
  public void testDeleteHouseholdByIdWithInvalidParam() {
    assertThrows(HouseholdInvalidParamException.class, () -> householdService.deleteHousehold(null));
  }

  @Description("DELETE /api/v1/household/{id} - Test delete household by id with household not found")
  @Test
  public void testDeleteHouseholdByIdWithHouseholdNotFound() {
    when(householdRepository.existsById(anyString())).thenReturn(false);

    assertThrows(HouseholdNotFoundException.class, () -> householdService.deleteHousehold("1"));
  }

  @Description("DELETE /api/v1/household/{id} - Test delete household by id success")
  @Test
  public void testDeleteHouseholdByIdSuccess() {
    when(householdRepository.existsById(anyString())).thenReturn(true);
    doNothing().when(householdRepository).deleteById(anyString());
    householdService.deleteHousehold("1");

    verify(householdRepository, times(1)).deleteById("1");
  }
}
