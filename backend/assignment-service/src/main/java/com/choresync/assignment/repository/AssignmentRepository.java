package com.choresync.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.assignment.entity.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {

}
