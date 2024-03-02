package com.choresync.announcement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.announcement.entity.Announcement;
import com.choresync.announcement.exception.AnnouncementNotFoundException;
import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;
import com.choresync.announcement.repository.AnnouncementRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

  @Autowired
  private AnnouncementRepository announcementRepository;

  @Override
  public AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest) {
    Announcement announcement = Announcement.builder()
        .message(announcementRequest.getMessage())
        .userId(announcementRequest.getUserId())
        .build();

    announcementRepository.save(announcement);

    AnnouncementResponse announcementResponse = AnnouncementResponse.builder()
        .id(announcement.getId())
        .message(announcement.getMessage())
        .userId(announcement.getUserId())
        .createdAt(announcement.getCreatedAt())
        .updatedAt(announcement.getUpdatedAt())
        .build();

    return announcementResponse;
  }

  @Override
  public AnnouncementResponse getAnnouncementById(String id) {
    Announcement announcement = announcementRepository.findById(id).orElse(null);

    if (announcement == null) {
      throw new AnnouncementNotFoundException("Announcement not found");
    }

    AnnouncementResponse announcementResponse = AnnouncementResponse.builder()
        .id(announcement.getId())
        .message(announcement.getMessage())
        .userId(announcement.getUserId())
        .createdAt(announcement.getCreatedAt())
        .updatedAt(announcement.getUpdatedAt())
        .build();

    return announcementResponse;
  }

  @Override
  public AnnouncementResponse editAnnouncement(String id, AnnouncementRequest announcementRequest) {
    Announcement announcement = announcementRepository.findById(id).orElse(null);

    if (announcement == null) {
      throw new AnnouncementNotFoundException("Announcement not found");
    }

    announcement.setMessage(announcementRequest.getMessage());
    announcement.setUserId(announcementRequest.getUserId());
    announcementRepository.save(announcement);

    AnnouncementResponse announcementResponse = AnnouncementResponse.builder()
        .id(announcement.getId())
        .message(announcement.getMessage())
        .userId(announcement.getUserId())
        .createdAt(announcement.getCreatedAt())
        .updatedAt(announcement.getUpdatedAt())
        .build();

    return announcementResponse;
  }

  @Override
  public void deleteAnnouncement(String id) {
    if (!announcementRepository.existsById(id)) {
      throw new AnnouncementNotFoundException("Announcement not found");
    }
    announcementRepository.deleteById(id);
  }
}
