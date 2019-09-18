package com.withergate.auth.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import javax.mail.MessagingException;

import com.withergate.auth.AuthProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class EmailServiceTest {

  @Mock
  private JavaMailSender mailSender;

  @Mock
  private MailContentBuilder contentBuilder;

  private EmailService emailService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    AuthProperties properties = new AuthProperties();
    properties.setEmailFrom("test@tets.com");
    properties.setEmailFromName("Test");

    emailService = new EmailService(mailSender, contentBuilder, properties);
  }

  @Test
  public void testGivenEmailServiceWhenSendingEmailThenVerifyEmailSentThroughMailSender() {
    emailService.prepareAndSend("john@example.com", "test", "Hello!", "http://www.example.com");

    verify(mailSender).send(any(MimeMessagePreparator.class));
  }

}
