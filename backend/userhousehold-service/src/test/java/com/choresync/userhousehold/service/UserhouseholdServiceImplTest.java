package com.choresync.userhousehold.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.userhousehold.entity.Userhousehold;
import com.choresync.userhousehold.exception.HouseholdCreationException;
import com.choresync.userhousehold.exception.HouseholdNotFoundException;
import com.choresync.userhousehold.exception.UserhouseholdNotFoundException;
import com.choresync.userhousehold.external.request.HouseholdRequest;
import com.choresync.userhousehold.external.response.HouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.repository.UserhouseholdRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

class UserhouseholdServiceImplTest {

    @Mock
    private UserhouseholdRepository userhouseholdRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserhouseholdServiceImpl userhouseholdService;

    UserhouseholdRequest userhouseholdRequest;
    HouseholdResponse fetchedHousehold;
    Userhousehold userhousehold;
    HouseholdRequest householdRequest;
    HouseholdResponse householdResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userhouseholdRequest = UserhouseholdRequest
                .builder()
                .name("Test Household")
                .userId("1")
                .build();

        fetchedHousehold = HouseholdResponse
                .builder()
                .id("1")
                .name("Test Household")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        userhousehold = Userhousehold
                .builder()
                .id("1")
                .userId("1")
                .householdId("1")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        householdRequest = HouseholdRequest
                .builder()
                .name("Test Household")
                .build();

        householdResponse = HouseholdResponse
                .builder()
                .id("1")
                .name("Test Household")
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    @Description("POST /api/v1/household - Test createUserhousehold")
    @Test
    void createUserhousehold_Success() {
        when(restTemplate.postForObject(
                "http://household-service/api/v1/household",
                householdRequest,
                HouseholdResponse.class))
                .thenReturn(fetchedHousehold);

        when(userhouseholdRepository.save(any()))
                .thenReturn(userhousehold);

        UserhouseholdResponse result = userhouseholdService.createUserhousehold(userhouseholdRequest);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("1", result.getUserId());
        assertEquals("Test Household", result.getHousehold().getName());

        verify(restTemplate, times(1)).postForObject(
                "http://household-service/api/v1/household",
                householdRequest,
                HouseholdResponse.class);
        verify(userhouseholdRepository, times(1)).save(any());
    }

    @Description("POST /api/v1/household - Test HouseholdCreationException")
    @Test
    void createUserhousehold_Failure_RestClientException() {
        when(restTemplate.postForObject(
                "http://household-service/api/v1/household",
                householdRequest,
                HouseholdResponse.class)).thenThrow(new RestClientException("Failed"));

        assertThrows(HouseholdCreationException.class,
                () -> userhouseholdService.createUserhousehold(userhouseholdRequest));
    }

    @Description("GET /api/v1/userhousehold/{id} - Test getUserhouseholdById")
    @Test
    void getUserhouseholdById_Success() {
        when(userhouseholdRepository.findById(userhousehold.getId()))
                .thenReturn(Optional.of(userhousehold));

        when(restTemplate.getForObject(
                "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
                HouseholdResponse.class))
                .thenReturn(householdResponse);

        UserhouseholdResponse result = userhouseholdService.getUserhouseholdById(userhousehold.getId());

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Test Household", result.getHousehold().getName());

        verify(userhouseholdRepository, times(1)).findById(userhousehold.getId());
        verify(restTemplate, times(1)).getForObject(
                "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
                HouseholdResponse.class);
    }

    @Description("GET /api/v1/userhousehold/{id} - Test UserhouseholdNotFoundException")
    @Test
    void getUserhouseholdById_Failure_UserhouseholdNotFound() {
        when(userhouseholdRepository.findById(userhousehold.getId()))
                .thenReturn(Optional.empty());

        assertThrows(UserhouseholdNotFoundException.class,
                () -> userhouseholdService.getUserhouseholdById(userhousehold.getId()));

        verify(userhouseholdRepository, times(1)).findById(userhousehold.getId());
        verify(restTemplate, times(0)).getForObject(
                "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
                HouseholdResponse.class);
    }

    @Description("GET /api/v1/userhousehold/{id} - Test HouseholdNotFoundException")
    @Test
    void getUserhouseholdById_Failure_RestClientException() {
        when(userhouseholdRepository.findById(userhousehold.getId()))
                .thenReturn(Optional.of(userhousehold));

        when(restTemplate.getForObject(
                "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
                HouseholdResponse.class))
                .thenThrow(new RestClientException("Failed"));

        assertThrows(HouseholdNotFoundException.class,
                () -> userhouseholdService.getUserhouseholdById(userhousehold.getId()));

        verify(userhouseholdRepository, times(1)).findById(userhousehold.getId());
        verify(restTemplate, times(1)).getForObject(
                "http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
                HouseholdResponse.class);
    }

    @Description("DELETE /api/v1/userhousehold/{id} - Test deleteUserhouseholdById")
    @Test
    void deleteUserhouseholdById_Success() {
        when(userhouseholdRepository.findById(userhousehold.getId()))
                .thenReturn(Optional.of(userhousehold));

        userhouseholdService.deleteUserhouseholdById(userhousehold.getId());

        verify(userhouseholdRepository, times(1)).findById(userhousehold.getId());
        verify(userhouseholdRepository, times(1)).delete(userhousehold);
    }

    @Description("DELETE /api/v1/userhousehold/{id} - Test UserhouseholdNotFoundException")
    @Test
    void deleteUserhouseholdById_Failure_UserhouseholdNotFound() {
        when(userhouseholdRepository.findById(userhousehold.getId()))
                .thenReturn(Optional.empty());

        assertThrows(UserhouseholdNotFoundException.class,
                () -> userhouseholdService.deleteUserhouseholdById(userhousehold.getId()));

        verify(userhouseholdRepository, times(1)).findById(userhousehold.getId());
        verify(userhouseholdRepository, times(0)).delete(userhousehold);
    }
}
