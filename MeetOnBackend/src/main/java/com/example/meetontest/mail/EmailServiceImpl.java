package com.example.meetontest.mail;


import com.example.meetontest.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.AssertFalse;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Override
    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@meeton.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    @Async
    public void sendConfirmationMessage(User user, String confirmationToken) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Complete Registration!");
        helper.setFrom("noreply@meeton.com");
        message.setText("<p>To confirm your account, please click here</p><br><a href='https://meeton-web.herokuapp.com/confirm?token=" + confirmationToken + "'>Click here!</a>","UTF-8", "html");
        emailSender.send(message);
    }
}
