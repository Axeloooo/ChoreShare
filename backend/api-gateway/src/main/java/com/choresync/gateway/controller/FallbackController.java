package com.choresync.gateway.controller;

import org.springframework.web.bind.annotation.RestController;

import com.choresync.gateway.Model.FallbackResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class FallbackController {
	@GetMapping("/userhousehold-service-fallback")
	public ResponseEntity<FallbackResponse> userhouseholdServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("Userhousehold service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/task-service-fallback")
	public ResponseEntity<FallbackResponse> taskServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("Task service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/household-service-fallback")
	public ResponseEntity<FallbackResponse> householdServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("Household service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/assignment-service-fallback")
	public ResponseEntity<FallbackResponse> assignmentServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("Assignment service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/event-service-fallback")
	public ResponseEntity<FallbackResponse> eventServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("Event service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/auth-service-fallback")
	public ResponseEntity<FallbackResponse> authServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("Auth service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/announcement-service-fallback")
	public ResponseEntity<FallbackResponse> announcementServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("Announcement service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/user-service-fallback")
	public ResponseEntity<FallbackResponse> userServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("User service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}

	@GetMapping("/email-service-fallback")
	public ResponseEntity<FallbackResponse> emailServiceFallback() {
		return new ResponseEntity<>(
				FallbackResponse
						.builder()
						.message("Email service is down. Please try again later")
						.build(),
				HttpStatus.SERVICE_UNAVAILABLE);
	}
}
