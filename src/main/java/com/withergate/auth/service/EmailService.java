package com.withergate.auth.service;

import javax.mail.internet.InternetAddress;

import com.withergate.auth.AuthProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * EmailService class.
 * </p>
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    private final AuthProperties properties;

    /**
     * <p>
     * Prepare Mime message and send it using java mail sender.
     * </p>
     */
    @Async
    public void prepareAndSend(String to, String subject, String message, String link) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(new InternetAddress(properties.getEmailFrom(), properties.getEmailFromName()));
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);

            String content = mailContentBuilder.build(message, link);
            messageHelper.setText(content, true);
        };

        log.debug("Sending email...");
        mailSender.send(messagePreparator);
    }

}
