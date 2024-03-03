package com.choresync.task.model;

import com.choresync.task.entity.Frequency;
import com.choresync.task.entity.Tag;
import com.choresync.task.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
  private String title;

  private String description;

  private Status status;

  private Frequency frequency;

  private Tag tag;

  private String userId;
}