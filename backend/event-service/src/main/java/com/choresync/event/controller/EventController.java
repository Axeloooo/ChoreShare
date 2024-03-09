package com.choresync.event.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    EventResponse event = eventService.createEvent(eventRequest);
    return new ResponseEntity<>(event, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResponse> getEventById(@PathVariable("id") String id) {
    EventResponse event = eventService.getEventById(id);
    return new ResponseEntity<>(event, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<EventResponse>> getAllEvents() {
    List<EventResponse> events = eventService.getAllEvents();
    return new ResponseEntity<>(events, HttpStatus.OK);
  }
}