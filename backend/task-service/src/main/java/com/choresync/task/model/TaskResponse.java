package com.choresync.task.model;

import java.util.Date;

import com.choresync.task.entity.Frequency;
import com.choresync.task.entity.Status;
import com.choresync.task.entity.Tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
  private String id;

  private String title;

  private String description;

  private Status status;

  private Frequency frequency;

  private Tag tag;

  private String userId;

  private Date createdAt;

  private Date updatedAt;
}
