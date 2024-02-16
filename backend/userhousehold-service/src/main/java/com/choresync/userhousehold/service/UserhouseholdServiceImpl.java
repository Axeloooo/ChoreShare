package com.choresync.userhousehold.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.userhousehold.entity.Userhousehold;
import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;
import com.choresync.userhousehold.repository.UserhouseholdRepository;

@Service
public class UserhouseholdServiceImpl implements UserhouseholdService {
  @Autowired
  private UserhouseholdRepository userhouseholdRepository;

  @Override
  public String createUserhousehold(UserhouseholdRequest userhouseholdRequest) {
    Userhousehold userhousehold = Userhousehold.builder()
        .householdId(userhouseholdRequest.getHouseholdId())
        .userId(userhouseholdRequest.getUserId())
        .build();

    userhouseholdRepository.save(userhousehold);

    return userhousehold.getId();
  }

  @Override
  public UserhouseholdResponse getUserhouseholdById(String id) {
    Userhousehold userhousehold = userhouseholdRepository.findById(id).orElse(null);

    UserhouseholdResponse userhouseholdResponse = UserhouseholdResponse.builder().id(userhousehold.getId())
        .householdId(userhousehold.getHouseholdId())
        .userId(userhousehold.getUserId())
        .createdAt(userhousehold.getCreatedAt())
        .updatedAt(userhousehold.getUpdatedAt())
        .build();

    return userhouseholdResponse;
  }

}
