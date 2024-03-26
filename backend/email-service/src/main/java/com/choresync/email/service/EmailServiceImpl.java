package com.choresync.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.email.exception.EmailInternalCommunicationException;
import com.choresync.email.exception.EmailInvalidBodyException;
import com.choresync.email.exception.EmailSendException;
import com.choresync.email.external.exception.HouseholdNotFoundException;
import com.choresync.email.external.response.HouseholdResponse;
import com.choresync.email.model.EmailRequest;
import com.choresync.email.model.EmailResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private RestTemplate restTemplate;

  /*
   * Extracts the error message from a RestClientException
   * 
   * @param e
   * 
   * @return String
   */
  @Override
  public String extractErrorMessage(RestClientException e) {
    String rawMessage = e.getMessage();

    try {
      String jsonSubstring = rawMessage.substring(rawMessage.indexOf("{"), rawMessage.lastIndexOf("}") + 1);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(jsonSubstring);

      if (rootNode.has("message")) {
        return rootNode.get("message").asText();
      }
    } catch (JsonProcessingException ex) {
      System.out.println("Error parsing JSON from exception message: " + ex.getMessage());
    } catch (StringIndexOutOfBoundsException ex) {
      System.out.println("Error extracting JSON substring from exception message: " + ex.getMessage());
    }
    return rawMessage;
  }

  /*
   * Sends an email to the specified recipient
   * 
   * @param emailRequest
   * 
   * @return EmailResponse
   * 
   * @throws EmailInvalidBodyException
   * 
   * @throws HouseholdNotFoundException
   * 
   * @throws EmailInternalCommunicationException
   * 
   * @throws EmailSendException
   */
  @Override
  public EmailResponse sendEmail(EmailRequest emailRequest) {
    if (emailRequest.getTo().isBlank() || emailRequest.getTo() == null || emailRequest.getHouseholdId().isBlank()
        || emailRequest.getHouseholdId() == null) {
      throw new EmailInvalidBodyException("Invalid request body");
    }

    try {
      HouseholdResponse householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + emailRequest.getHouseholdId(),
          HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Could not find a household");
      }
    } catch (RestClientException e) {
      throw new EmailInternalCommunicationException(extractErrorMessage(e));
    }

    String subject = "ChoreSync: Invitation to join a household!";
    String htmlMessage = String.format("<h1>Hi there!</h1>" +
        "<p>You have been invited to join a household on ChoreSync!</p>" +
        "<p>Click the link below to create an account:</p>" +
        "<a href='http://localhost:3000/register' style='background-color:#3498FF; color: white; padding: 10px 18px; text-align: center; text-decoration: none; display: inline-block; margin: 10px 0 10px 0;'>Join ChoreSync</a>"
        +
        "<p>Use the following code to join the household:</p>" +
        "<strong>%s</strong>",
        emailRequest.getHouseholdId());

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    try {
      helper.setTo(emailRequest.getTo());
      helper.setSubject(subject);
      helper.setText(htmlMessage, true);
      helper.setFrom("no-reply@choresync.com");
      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      throw new EmailSendException("Failed to send email");
    }
    return EmailResponse
        .builder()
        .message("Email sent successfully")
        .build();
  }
}
