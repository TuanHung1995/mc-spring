package com.mc.infrastructure.iam.mail;

import com.mc.domain.core.port.out.MailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("iamMailSender")
@Slf4j
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void send(String to, String subject, String body) {
        sendEmail(new String[]{to}, subject, body);
    }

    @Override
    @Async
    public void send(String[] to, String subject, String body) {
        sendEmail(to, subject, body);
    }

    private void sendEmail(String[] to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

//            helper.setFrom("noreply@mondayclone.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML

            javaMailSender.send(message);
            log.info("Email sent successfully to {} recipients", to.length);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
        }
    }

}