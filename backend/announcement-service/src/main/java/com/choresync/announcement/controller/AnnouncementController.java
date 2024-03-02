package com.choresync.announcement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.choresync.announcement.exception.AnnouncementNotFoundException;
import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;
import com.choresync.announcement.service.AnnouncementService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/announcement")
public class AnnouncementController {

  @Autowired
  private AnnouncementService announcementService;

  // Create announcement
  // url: /api/v1/announcement, method: POST
  // return type: ResponseEntity<AnnouncementResponse>
  // NOTE: add check to see if user wanting to create announcement exists
  @PostMapping
  public ResponseEntity<AnnouncementResponse> createAnnouncement(@RequestBody AnnouncementRequest announcementRequest) {
    AnnouncementResponse newAnnouncement = announcementService.createAnnouncement(announcementRequest);
    return new ResponseEntity<>(newAnnouncement, HttpStatus.CREATED);
  }

  // Update announcement
  // url: /api/v1/announcement/{id}, request parameter: String (new message),
  // method: PUT
  // return type: ResponseEntity<AnnouncementResponse>
  // NOTE: add check to see if user wanting to create announcement exists
  @PutMapping("/{id}")
  public ResponseEntity<AnnouncementResponse> editAnnouncement(@PathVariable String id,
      @RequestBody AnnouncementRequest announcementRequest) {
    AnnouncementResponse editedAnnouncement = announcementService.editAnnouncement(id, announcementRequest);
    return new ResponseEntity<>(editedAnnouncement, HttpStatus.OK);
  }

  // Get announcement by id
  // url: /api/v1/announcement/{id}, method: GET
  // return type: ResponseEntity<AnnouncementResponse>
  @GetMapping("/{id}")
  public ResponseEntity<AnnouncementResponse> getAnnouncementById(@PathVariable String id) {
    AnnouncementResponse announcement = announcementService.getAnnouncementById(id);
    return new ResponseEntity<>(announcement, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteAnnouncement(@PathVariable String id) {
    announcementService.deleteAnnouncement(id);
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("message", "Announcement deleted successfully");
    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }

  // Exception Handler for AnnouncementNotFoundException
  @ExceptionHandler(AnnouncementNotFoundException.class)
  public ResponseEntity<Object> handleAnnouncementNotFoundException(AnnouncementNotFoundException ex) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("error", ex.getMessage());
    return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
  }

}
