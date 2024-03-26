package com.choresync.userhousehold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.userhousehold.entity.Userhousehold;

@Repository
public interface UserhouseholdRepository extends JpaRepository<Userhousehold, String> {
  List<Userhousehold> findAllByUserId(String userId);

  List<Userhousehold> findAllByHouseholdId(String householdId);
}
