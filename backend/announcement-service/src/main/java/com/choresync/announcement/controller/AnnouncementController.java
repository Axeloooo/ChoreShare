package com.choresync.announcement.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;
import com.choresync.announcement.service.AnnouncementService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/announcement")
public class AnnouncementController {

  @Autowired
  private AnnouncementService announcementService;

  @PostMapping
  public ResponseEntity<AnnouncementResponse> createAnnouncement(@RequestBody AnnouncementRequest announcementRequest) {
    AnnouncementResponse newAnnouncement = announcementService.createAnnouncement(announcementRequest);
    return new ResponseEntity<>(newAnnouncement, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AnnouncementResponse> editAnnouncement(@PathVariable String id,
      @RequestBody AnnouncementRequest announcementRequest) {
    AnnouncementResponse editedAnnouncement = announcementService.editAnnouncement(id, announcementRequest);
    return new ResponseEntity<>(editedAnnouncement, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AnnouncementResponse> getAnnouncementById(@PathVariable String id) {
    AnnouncementResponse announcement = announcementService.getAnnouncementById(id);
    return new ResponseEntity<>(announcement, HttpStatus.OK);
  }

  @GetMapping("/household/{hid}")
  public ResponseEntity<List<AnnouncementResponse>> getAllAnnouncementsByHousehold(
      @PathVariable("hid") String householdId) {
    List<AnnouncementResponse> announcements = announcementService.getAllAnnouncementsByHouseholdId(householdId);
    return new ResponseEntity<>(announcements, HttpStatus.OK);
  }

  @GetMapping("/user/{uid}")
  public ResponseEntity<List<AnnouncementResponse>> getMethodName(@PathVariable("uid") String userId) {
    List<AnnouncementResponse> announcements = announcementService.getAllAnnouncementsByUserId(userId);
    return new ResponseEntity<>(announcements, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteAnnouncement(@PathVariable String id) {
    announcementService.deleteAnnouncement(id);
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("message", "Announcement deleted successfully");
    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }
}
