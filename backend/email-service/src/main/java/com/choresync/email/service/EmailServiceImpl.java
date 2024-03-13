package com.choresync.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public void sendEmail(String to) {
    String subject = "ChoreSync: Invitation to join a household!";
    String htmlMessage = "<h1>Hi there!</h1>" +
        "<p>You have been invited to join a household on ChoreSync!</p>" +
        "<p>Click the link below to create an account:</p>" +
        "<a href='http://localhost:3000/register' style='background-color:#3498FF; color: white; padding: 10px 18px; text-align: center; text-decoration: none; display: inline-block; margin: 10px 0 10px 0;'>Join ChoreSync</a>"
        +
        "<p>Use the following code to join the household:</p>" +
        "<strong>hwd1qejhfpoq23456</strong>";

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
