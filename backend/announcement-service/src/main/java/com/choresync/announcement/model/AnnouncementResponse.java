package com.choresync.announcement.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementResponse {
  private String id;

  private String householdId;

  private String message;

  private String userId;

  private String author;

  private Date createdAt;

  private Date updatedAt;
}
