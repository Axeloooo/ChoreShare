package com.choresync.email.service;

public interface EmailService {
  void sendEmail(String to, String householdId);
}
