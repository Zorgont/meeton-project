package com.meeton.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeton.notification.converter.NotificationConverter;
import com.meeton.notification.model.dto.EmailConfirmationDTO;
import com.meeton.notification.model.dto.NotificationDTO;
import com.meeton.notification.service.EmailService;
import com.meeton.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ObjectMapper mapper;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final NotificationConverter notificationConverter;

    @KafkaListener(topics = "email", groupId = "notifications")
    public void consumeEmailMessage(String message) throws IOException {
        log.info(String.format("#### -> Consumed message -> %s", message));
        EmailConfirmationDTO emailConfirmation = mapper.readValue(message, EmailConfirmationDTO.class);
        emailService.sendMessage(
                emailConfirmation.getEmail(),
                emailConfirmation.getSubject(),
                emailConfirmation.getMessage());
    }

    @KafkaListener(topics = "notifications", groupId = "notifications")
    public void consumeNotificationMessage(String message) throws IOException {
        log.info(String.format("#### -> Consumed message -> %s", message));
        NotificationDTO notification = mapper.readValue(message, NotificationDTO.class);
        notificationService.createNotification(notificationConverter.convert(notification));
    }
}
