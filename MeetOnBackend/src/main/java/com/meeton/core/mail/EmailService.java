package com.meeton.core.mail;

import com.meeton.core.entities.User;

import javax.mail.MessagingException;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendConfirmationMessage(User user, String confirmationToken) throws MessagingException;
}
