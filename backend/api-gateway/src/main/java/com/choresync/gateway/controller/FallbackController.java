package com.choresync.gateway.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class FallbackController {

  @GetMapping("/userhousehold-service-fallback")
  public String userhouseholdServiceFallback() {
    return "Userhousehold service is down. Please try again later";
  }

  @GetMapping("/task-service-fallback")
  public String taskServiceFallback() {
    return "Task service is down. Please try again later";
  }

  @GetMapping("/household-service-fallback")
  public String householdServiceFallback() {
    return "Household service is down. Please try again later";
  }

  @GetMapping("/assignment-service-fallback")
  public String assignmentServiceFallback() {
    return "Assignment service is down. Please try again later";
  }

  @GetMapping("/event-service-fallback")
  public String eventServiceFallback() {
    return "Event service is down. Please try again later";
  }

  @GetMapping("/auth-service-fallback")
  public String authServiceFallback() {
    return "Auth service is down. Please try again later";
  }

  @GetMapping("/announcement-service-fallback")
  public String announcementServiceFallback() {
    return "Announcement service is down. Please try again later";
  }

  @GetMapping("/user-service-fallback")
  public String userServiceFallback() {
    return "User service is down. Please try again later";
  }
}
