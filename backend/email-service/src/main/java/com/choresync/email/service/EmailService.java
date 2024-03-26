package com.choresync.email.service;

import org.springframework.web.client.RestClientException;

import com.choresync.email.model.EmailRequest;
import com.choresync.email.model.EmailResponse;

public interface EmailService {
  String extractErrorMessage(RestClientException e);

  EmailResponse sendEmail(EmailRequest emailRequest);
}
