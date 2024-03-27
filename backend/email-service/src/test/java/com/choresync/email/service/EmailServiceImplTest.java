package com.choresync.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.email.exception.EmailInternalCommunicationException;
import com.choresync.email.exception.EmailInvalidBodyException;
import com.choresync.email.external.exception.HouseholdNotFoundException;
import com.choresync.email.external.response.HouseholdResponse;
import com.choresync.email.model.EmailRequest;
import com.choresync.email.model.EmailResponse;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

public class EmailServiceImplTest {

  @InjectMocks
  private EmailServiceImpl emailService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private JavaMailSender javaMailSender;

  private EmailRequest emailRequest;
  private EmailRequest invalidEmailRequest;
  private HouseholdResponse householdResponse;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    emailRequest = EmailRequest
        .builder()
        .householdId("1")
        .to("axel")
        .build();

    invalidEmailRequest = EmailRequest
        .builder()
        .build();

    householdResponse = HouseholdResponse
        .builder()
        .id("1")
        .name("Household")
        .build();
  }

  @Description("POST /api/v1/email/send - Test send email with invalid body")
  @Test
  public void testSendEmailWithInvalidBody() {
    assertThrows(EmailInvalidBodyException.class, () -> emailService.sendEmail(invalidEmailRequest));
  }

  @Description("POST /api/v1/email/send - Test send email with household not found")
  @Test
  public void testSendEmailWithHouseholdNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + emailRequest.getHouseholdId(),
        HouseholdResponse.class)).thenReturn(null);
    assertThrows(HouseholdNotFoundException.class, () -> emailService.sendEmail(emailRequest));
  }

  @Description("POST /api/v1/email/send - Test send email with internal communication exception")
  @Test
  public void testSendEmailWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + emailRequest.getHouseholdId(),
        HouseholdResponse.class)).thenThrow(new RestClientException("Internal error"));

    assertThrows(EmailInternalCommunicationException.class, () -> emailService.sendEmail(emailRequest));
  }

  @Description("POST /api/v1/email/send - Test send email success")
  @Test
  public void testSendEmailSuccess() throws Exception {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" +
        emailRequest.getHouseholdId(),
        HouseholdResponse.class)).thenReturn(householdResponse);

    MimeMessage mimeMessage = new MimeMessage((Session) null);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

    doNothing().when(javaMailSender).send(any(MimeMessage.class));

    EmailResponse response = emailService.sendEmail(emailRequest);

    assertNotNull(response);
    assertEquals("Email sent successfully", response.getMessage());
  }
}
