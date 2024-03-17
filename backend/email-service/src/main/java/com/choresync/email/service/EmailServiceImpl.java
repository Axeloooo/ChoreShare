package com.choresync.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.email.exception.HouseholdNotFoundException;
import com.choresync.email.external.response.HouseholdResponse;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public void sendEmail(String to, String householdId) {
    HouseholdResponse householdResponse;

    try {
      householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + householdId,
          HouseholdResponse.class);

    } catch (RestClientException e) {
      throw new HouseholdNotFoundException(
          "Could not find a household. " + e.getMessage());
    }

    if (householdResponse == null) {
      throw new HouseholdNotFoundException("Could not find a household");
    }

    String subject = "ChoreSync: Invitation to join a household!";
    String htmlMessage = String.format("<h1>Hi there!</h1>" +
        "<p>You have been invited to join a household on ChoreSync!</p>" +
        "<p>Click the link below to create an account:</p>" +
        "<a href='http://localhost:3000/register' style='background-color:#3498FF; color: white; padding: 10px 18px; text-align: center; text-decoration: none; display: inline-block; margin: 10px 0 10px 0;'>Join ChoreSync</a>"
        +
        "<p>Use the following code to join the household:</p>" +
        "<strong>%s</strong>",
        householdId);

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    try {
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlMessage, true);
      helper.setFrom("no-reply@choresync.com");
      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
