package com.choresync.userhousehold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.choresync.userhousehold.entity.Userhousehold;

@Repository
public interface UserhouseholdRepository extends JpaRepository<Userhousehold, String> {
  List<Userhousehold> findAllByUserId(String userId);

  List<Userhousehold> findAllByHouseholdId(String householdId);


  // In future possibly optimize joinHouseHold with this query
  //@Query("SELECT COUNT(u) FROM userhousehold u WHERE u.user_id = ?1 AND u.household_id = ?2")
  //Long countByUserIdAndHouseholdId(String userId, String householdId);
}
