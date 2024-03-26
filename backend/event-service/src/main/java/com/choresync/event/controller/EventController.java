package com.choresync.event.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.choresync.event.model.EventRequest;
import com.choresync.event.model.EventResponse;
import com.choresync.event.service.EventService;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {
  @Autowired
  private EventService eventService;

  @PostMapping
  public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
    EventResponse eventResponse = eventService.createEvent(eventRequest);
    return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResponse> getEventById(@PathVariable("id") String id) {
    EventResponse eventResponse = eventService.getEventById(id);
    return new ResponseEntity<>(eventResponse, HttpStatus.OK);
  }

  @GetMapping("/household/{hid}")
  public ResponseEntity<List<EventResponse>> getAllEventsByHouseholdId(@PathVariable("hid") String householdId) {
    List<EventResponse> events = eventService.getAllEventsByHouseholdId(householdId);
    return new ResponseEntity<>(events, HttpStatus.OK);
  }

  @GetMapping("/user/{uid}")
  public ResponseEntity<List<EventResponse>> getAllEventsByUserId(@PathVariable("uid") String userId) {
    List<EventResponse> events = eventService.getAllEventsByUserId(userId);
    return new ResponseEntity<>(events, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteEvent(@PathVariable("id") String id) {
    eventService.deleteEvent(id);
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("message", "Announcement deleted successfully");
    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }
}