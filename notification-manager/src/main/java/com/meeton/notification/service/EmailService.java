package com.meeton.notification.service;

public interface EmailService {
    void sendMessage(String to, String subject, String text);
}
