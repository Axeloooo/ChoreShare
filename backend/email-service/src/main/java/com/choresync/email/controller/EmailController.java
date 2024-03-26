package com.choresync.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.choresync.email.model.EmailRequest;
import com.choresync.email.model.EmailResponse;
import com.choresync.email.service.EmailService;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
  @Autowired
  private EmailService emailService;

  @PostMapping("/send")
  public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest) {
    EmailResponse emailResponse = emailService.sendEmail(emailRequest);
    return new ResponseEntity<>(emailResponse, HttpStatus.OK);
  }
}