package com.choresync.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.task.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
  List<Task> findByUserId(String userId);
}
