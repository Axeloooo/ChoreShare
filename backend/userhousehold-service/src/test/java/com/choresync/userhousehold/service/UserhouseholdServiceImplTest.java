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
import com.choresync.userhousehold.model.GetMembersResponse;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.model.UserhouseholdResponse.Household;
import com.choresync.userhousehold.repository.UserhouseholdRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

class UserhouseholdServiceImplTest {

	@Mock
	private UserhouseholdRepository userhouseholdRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private UserhouseholdServiceImpl userhouseholdService;

	private UserhouseholdRequest userhouseholdRequest;
	private HouseholdResponse fetchedHousehold;
	private Userhousehold userhousehold;
	private HouseholdRequest householdRequest;
	private HouseholdResponse householdResponse;
	private UserResponse userResponse;

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

		userResponse = UserResponse
				.builder()
				.id("user1")
				.firstName("John")
				.lastName("Doe")
				.username("johndoe")
				.email("axel@gmail.com")
				.phone("1234567890")
				.missedChores(0)
				.streak(0)
				.createdAt(new Date())
				.updatedAt(new Date())
				.build();
	}

	@Description("POST /api/v1/userhousehold - Test create userhousehold with invalid body")
	@Test
	public void testCreateUserhouseholdWithInvalidBody() {
		UserhouseholdRequest invalidRequest = new UserhouseholdRequest();
		assertThrows(UserhouseholdInvalidBodyException.class,
				() -> userhouseholdService.createUserhousehold(invalidRequest));
	}

	@Description("POST /api/v1/userhousehold - Test create userhousehold with user not found")
	@Test
	public void testCreateUserhouseholdWithUserNotFound() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userhouseholdRequest.getUserId(),
						UserResponse.class))
				.thenReturn(null);

		assertThrows(UserNotFoundException.class, () -> userhouseholdService.createUserhousehold(userhouseholdRequest));
	}

	@Description("POST /api/v1/userhousehold - Test create userhousehold with internal communication exception for user")
	@Test
	public void testCreateUserhouseholdWithInternalCommunicationExceptionForUser() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userhouseholdRequest.getUserId(),
						UserResponse.class))
				.thenThrow(new RestClientException("Internal error"));

		assertThrows(UserhouseholdInternalCommunicationException.class,
				() -> userhouseholdService.createUserhousehold(userhouseholdRequest));
	}

	@Description("POST /api/v1/userhousehold - Test create userhousehold with household creation exception")
	@Test
	public void testCreateUserhouseholdWithHouseholdCreationException() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userhouseholdRequest.getUserId(),
						UserResponse.class))
				.thenReturn(userResponse);
		when(restTemplate
				.postForObject(
						"http://household-service/api/v1/household", householdRequest,
						HouseholdResponse.class))
				.thenReturn(null);

		assertThrows(HouseholdCreationException.class,
				() -> userhouseholdService.createUserhousehold(userhouseholdRequest));
	}

	@Description("POST /api/v1/userhousehold - Test create userhousehold with internal communication exception for household")
	@Test
	public void testCreateUserhouseholdWithInternalCommunicationExceptionForHousehold() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userhouseholdRequest.getUserId(),
						UserResponse.class))
				.thenReturn(userResponse);
		when(restTemplate
				.postForObject(
						"http://household-service/api/v1/household", householdRequest,
						HouseholdResponse.class))
				.thenThrow(new RestClientException("Internal error"));

		assertThrows(UserhouseholdInternalCommunicationException.class,
				() -> userhouseholdService.createUserhousehold(userhouseholdRequest));
	}

	@Description("POST /api/v1/userhousehold - Test create userhousehold success")
	@Test
	public void testCreateUserhouseholdSuccess() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userhouseholdRequest.getUserId(),
						UserResponse.class))
				.thenReturn(userResponse);
		when(restTemplate
				.postForObject(
						"http://household-service/api/v1/household", householdRequest,
						HouseholdResponse.class))
				.thenReturn(fetchedHousehold);
		when(userhouseholdRepository.save(any(Userhousehold.class))).thenReturn(userhousehold);

		UserhouseholdResponse response = userhouseholdService.createUserhousehold(userhouseholdRequest);

		assertNotNull(response);
		assertEquals(userhousehold.getId(), response.getId());
		assertEquals(userhousehold.getUserId(), response.getUserId());
		assertEquals(fetchedHousehold.getId(), response.getHousehold().getId());
		assertEquals(fetchedHousehold.getName(), response.getHousehold().getName());
		assertEquals(fetchedHousehold.getCreatedAt(), response.getHousehold().getCreatedAt());
		assertEquals(fetchedHousehold.getUpdatedAt(), response.getHousehold().getUpdatedAt());

		verify(userhouseholdRepository, times(1)).save(any(Userhousehold.class));
		verify(restTemplate, times(1)).getForObject(
				"http://user-service/api/v1/user/" + userhouseholdRequest.getUserId(),
				UserResponse.class);
		verify(restTemplate, times(1)).postForObject("http://household-service/api/v1/household", householdRequest,
				HouseholdResponse.class);
	}

	@Description("GET /api/v1/userhousehold/{id} - Test get userhousehold by id with invalid param")
	@Test
	public void testGetUserhouseholdByIdWithInvalidParam() {
		assertThrows(UserhouseholdInvalidParamException.class, () -> userhouseholdService.getUserhouseholdById(null));
	}

	@Description("GET /api/v1/userhousehold/{id} - Test get userhousehold by id with userhousehold not found")
	@Test
	public void testGetUserhouseholdByIdWithUserhouseholdNotFound() {
		when(userhouseholdRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(UserhouseholdNotFoundException.class, () -> userhouseholdService.getUserhouseholdById("1"));
	}

	@Description("GET /api/v1/userhousehold/{id} - Test get userhousehold by id with household not found")
	@Test
	public void testGetUserhouseholdByIdWithHouseholdNotFound() {
		when(userhouseholdRepository.findById(anyString())).thenReturn(Optional.of(userhousehold));
		when(restTemplate
				.getForObject(
						"http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
						HouseholdResponse.class))
				.thenReturn(null);

		assertThrows(HouseholdNotFoundException.class, () -> userhouseholdService.getUserhouseholdById("1"));
	}

	@Description("GET /api/v1/userhousehold/{id} - Test get userhousehold by id with internal communication exception")
	@Test
	public void testGetUserhouseholdByIdWithInternalCommunicationException() {
		when(userhouseholdRepository.findById(anyString())).thenReturn(Optional.of(userhousehold));
		when(restTemplate
				.getForObject(
						"http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
						HouseholdResponse.class))
				.thenThrow(new RestClientException("Internal error"));

		assertThrows(UserhouseholdInternalCommunicationException.class,
				() -> userhouseholdService.getUserhouseholdById("1"));
	}

	@Description("GET /api/v1/userhousehold/{id} - Test get userhousehold by id success")
	@Test
	public void testGetUserhouseholdByIdSuccess() {
		when(userhouseholdRepository.findById(anyString())).thenReturn(Optional.of(userhousehold));
		when(restTemplate.getForObject("http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
				HouseholdResponse.class)).thenReturn(fetchedHousehold);

		UserhouseholdResponse response = userhouseholdService.getUserhouseholdById("1");

		assertNotNull(response);
		assertEquals(userhousehold.getId(), response.getId());
		assertEquals(userhousehold.getUserId(), response.getUserId());
		assertEquals(fetchedHousehold.getId(), response.getHousehold().getId());
		assertEquals(fetchedHousehold.getName(), response.getHousehold().getName());
		assertEquals(fetchedHousehold.getCreatedAt(), response.getHousehold().getCreatedAt());
		assertEquals(fetchedHousehold.getUpdatedAt(), response.getHousehold().getUpdatedAt());

		verify(userhouseholdRepository, times(1)).findById(anyString());
		verify(restTemplate, times(1)).getForObject(
				"http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
				HouseholdResponse.class);
	}

	@Description("GET /api/v1/userhousehold/user/{userId} - Test get userhouseholds by user id with invalid param")
	@Test
	public void testGetUserhouseholdsByUserIdWithInvalidParam() {
		assertThrows(UserhouseholdInvalidParamException.class, () -> userhouseholdService.getUserhouseholdsByUserId(null));
	}

	@Description("GET /api/v1/userhousehold/user/{userId} - Test get userhouseholds by user id with user not found")
	@Test
	public void testGetUserhouseholdsByUserIdWithUserNotFound() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
				.thenReturn(null);

		assertThrows(UserNotFoundException.class,
				() -> userhouseholdService.getUserhouseholdsByUserId(userResponse.getId()));
	}

	@Description("GET /api/v1/userhousehold/user/{userId} - Test get userhouseholds by user id with internal communication exception")
	@Test
	public void testGetUserhouseholdsByUserIdWithInternalCommunicationException() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
				.thenThrow(new RestClientException("Internal error"));

		assertThrows(UserhouseholdInternalCommunicationException.class,
				() -> userhouseholdService.getUserhouseholdsByUserId(userResponse.getId()));
	}

	@Description("GET /api/v1/userhousehold/user/{userId} - Test get userhouseholds by user id success")
	@Test
	public void testGetUserhouseholdsByUserIdSuccess() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
				.thenReturn(userResponse);
		when(userhouseholdRepository.findAllByUserId(userResponse.getId()))
				.thenReturn(Collections.singletonList(userhousehold));
		when(restTemplate
				.getForObject(
						"http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
						HouseholdResponse.class))
				.thenReturn(fetchedHousehold);

		List<UserhouseholdResponse> response = userhouseholdService.getUserhouseholdsByUserId(userResponse.getId());

		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertEquals(1, response.size());
		assertEquals(userhousehold.getId(), response.get(0).getId());
		assertEquals(userhousehold.getUserId(), response.get(0).getUserId());
		assertEquals(fetchedHousehold.getId(), response.get(0).getHousehold().getId());
		assertEquals(fetchedHousehold.getName(), response.get(0).getHousehold().getName());
		assertEquals(fetchedHousehold.getCreatedAt(), response.get(0).getHousehold().getCreatedAt());
		assertEquals(fetchedHousehold.getUpdatedAt(), response.get(0).getHousehold().getUpdatedAt());

		verify(restTemplate, times(1)).getForObject("http://user-service/api/v1/user/" + userResponse.getId(),
				UserResponse.class);
		verify(userhouseholdRepository, times(1)).findAllByUserId(userResponse.getId());
		verify(restTemplate, times(1)).getForObject(
				"http://household-service/api/v1/household/" + userhousehold.getHouseholdId(),
				HouseholdResponse.class);
	}

	@Description("GET /api/v1/userhousehold/household/{householdId} - Test get userhouseholds by household id with invalid param")
	@Test
	public void testGetUserhouseholdsByHouseholdIdWithInvalidParam() {
		assertThrows(UserhouseholdInvalidParamException.class,
				() -> userhouseholdService.getUserhouseholdsByHouseholdId(null));
	}

	@Description("GET /api/v1/userhousehold/household/{householdId} - Test get userhouseholds by household id with household not found")
	@Test
	public void testGetUserhouseholdsByHouseholdIdWithHouseholdNotFound() {
		when(restTemplate
				.getForObject(
						"http://household-service/api/v1/household/" + householdResponse.getId(),
						HouseholdResponse.class))
				.thenReturn(null);

		assertThrows(HouseholdNotFoundException.class,
				() -> userhouseholdService.getUserhouseholdsByHouseholdId(householdResponse.getId()));
	}

	@Description("GET /api/v1/userhousehold/household/{householdId} - Test get userhouseholds by household id with internal communication exception")
	@Test
	public void testGetUserhouseholdsByHouseholdIdWithInternalCommunicationException() {
		when(restTemplate
				.getForObject(
						"http://household-service/api/v1/household/" + householdResponse.getId(), HouseholdResponse.class))
				.thenThrow(new RestClientException("Internal error"));

		assertThrows(UserhouseholdInternalCommunicationException.class,
				() -> userhouseholdService.getUserhouseholdsByHouseholdId(householdResponse.getId()));
	}

	@Description("GET /api/v1/userhousehold/household/{householdId} - Test get userhouseholds by household id success")
	@Test
	public void testGetUserhouseholdsByHouseholdIdSuccess() {
		when(restTemplate
				.getForObject(
						"http://household-service/api/v1/household/" + householdResponse.getId(), HouseholdResponse.class))
				.thenReturn(fetchedHousehold);
		when(userhouseholdRepository.findAllByHouseholdId(householdResponse.getId()))
				.thenReturn(Collections.singletonList(userhousehold));
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userhousehold.getUserId(), UserResponse.class))
				.thenReturn(userResponse);

		List<GetMembersResponse> response = userhouseholdService.getUserhouseholdsByHouseholdId(householdResponse.getId());

		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertEquals(1, response.size());
		assertEquals(userResponse.getFirstName(), response.get(0).getUser().getFirstName());
		assertEquals(userResponse.getLastName(), response.get(0).getUser().getLastName());
		assertEquals(userResponse.getUsername(), response.get(0).getUser().getUsername());
		assertEquals(fetchedHousehold.getId(), response.get(0).getHousehold().getId());
		assertEquals(fetchedHousehold.getName(), response.get(0).getHousehold().getName());
		assertEquals(fetchedHousehold.getCreatedAt(), response.get(0).getHousehold().getCreatedAt());
		assertEquals(fetchedHousehold.getUpdatedAt(), response.get(0).getHousehold().getUpdatedAt());

		verify(userhouseholdRepository, times(1)).findAllByHouseholdId(householdResponse.getId());
	}

	@Description("DELETE /api/v1/userhousehold/{id} - Test delete userhousehold by id with invalid param")
	@Test
	public void testDeleteUserhouseholdByIdWithInvalidParam() {
		assertThrows(UserhouseholdInvalidParamException.class, () -> userhouseholdService.deleteUserhouseholdById(null));
	}

	@Description("DELETE /api/v1/userhousehold/{id} - Test delete userhousehold by id with userhousehold not found")
	@Test
	public void testDeleteUserhouseholdByIdWithUserhouseholdNotFound() {
		when(userhouseholdRepository.existsById(anyString())).thenReturn(false);

		assertThrows(UserhouseholdNotFoundException.class, () -> userhouseholdService.deleteUserhouseholdById("1"));
	}

	@Description("DELETE /api/v1/userhousehold/{id} - Test delete userhousehold by id success")
	@Test
	public void testDeleteUserhouseholdByIdSuccess() {
		when(userhouseholdRepository.existsById(anyString())).thenReturn(true);
		doNothing().when(userhouseholdRepository).deleteById(anyString());
		userhouseholdService.deleteUserhouseholdById("1");

		verify(userhouseholdRepository, times(1)).deleteById("1");
	}

	@Description("POST /api/v1/userhousehold/join - Test join household with invalid param")
	@Test
	public void testJoinHouseHoldWithInvalidParam() {
		assertThrows(UserhouseholdInvalidParamException.class, () -> userhouseholdService.joinHouseHold(null, null));
	}

	@Description("POST /api/v1/userhousehold/join - Test join household with user not found")
	@Test
	public void testJoinHouseHoldWithUserNotFound() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
				.thenReturn(null);

		assertThrows(UserNotFoundException.class,
				() -> userhouseholdService.joinHouseHold(userResponse.getId(), householdResponse.getId()));
	}

	@Description("POST /api/v1/userhousehold/join - Test join household with household not found")
	@Test
	public void testJoinHouseHoldWithHouseholdNotFound() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
				.thenReturn(userResponse);
		when(restTemplate
				.getForObject(
						"http://household-service/api/v1/household/" + householdResponse.getId(),
						HouseholdResponse.class))
				.thenReturn(null);

		assertThrows(HouseholdNotFoundException.class,
				() -> userhouseholdService.joinHouseHold(userResponse.getId(), householdResponse.getId()));
	}

	@Description("POST /api/v1/userhousehold/join - Test join household with user already in household")
	@Test
	public void testJoinHouseHoldWithUserAlreadyInHousehold() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
				.thenReturn(userResponse);
		when(restTemplate
				.getForObject(
						"http://household-service/api/v1/household/" + householdResponse.getId(),
						HouseholdResponse.class))
				.thenReturn(fetchedHousehold);
		when(userhouseholdRepository.findAllByUserId(userResponse.getId()))
				.thenReturn(Collections.singletonList(userhousehold));

		assertThrows(UserhouseholdUserInHouseholdException.class,
				() -> userhouseholdService.joinHouseHold(userResponse.getId(), householdResponse.getId()));
	}

	@Description("POST /api/v1/userhousehold/join - Test join household success")
	@Test
	public void testJoinHouseHoldSuccess() {
		when(restTemplate
				.getForObject(
						"http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
				.thenReturn(userResponse);
		when(restTemplate.getForObject("http://household-service/api/v1/household/" + householdResponse.getId(),
				HouseholdResponse.class))
				.thenReturn(fetchedHousehold);
		when(userhouseholdRepository.findAllByUserId(userResponse.getId())).thenReturn(Collections.emptyList());
		when(userhouseholdRepository.save(any(Userhousehold.class))).thenReturn(userhousehold);

		Household response = userhouseholdService.joinHouseHold(userResponse.getId(), householdResponse.getId());

		assertNotNull(response);
		assertEquals(fetchedHousehold.getId(), response.getId());
		assertEquals(fetchedHousehold.getName(), response.getName());
		assertEquals(fetchedHousehold.getCreatedAt(), response.getCreatedAt());
		assertEquals(fetchedHousehold.getUpdatedAt(), response.getUpdatedAt());
	}
}
