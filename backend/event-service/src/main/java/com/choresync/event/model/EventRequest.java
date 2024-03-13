package com.choresync.event.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

  private String householdId;

  private String title;

  private String userId;

  private String username;

  private Date startTime;

  private Date endTime;
}
