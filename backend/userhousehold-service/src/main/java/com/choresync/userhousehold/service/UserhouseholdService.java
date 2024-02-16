package com.choresync.userhousehold.service;

import com.choresync.userhousehold.model.UserhouseholdRequest;
import com.choresync.userhousehold.model.UserhouseholdResponse;

public interface UserhouseholdService {

  String createUserhousehold(UserhouseholdRequest userhouseholdRequest);

  UserhouseholdResponse getUserhouseholdById(String id);

}
