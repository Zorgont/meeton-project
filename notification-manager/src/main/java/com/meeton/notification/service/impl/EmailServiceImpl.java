package com.meeton.notification.service.impl;

import com.meeton.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @SneakyThrows
    @Override
    @Async
    public void sendMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(text, text.contains("<"));
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("noreply@meeton.com");
        emailSender.send(mimeMessage);
    }
}
