package com.meeton.notification.service.impl;

import com.meeton.notification.converter.NotificationConverter;
import com.meeton.notification.model.entity.Notification;
import com.meeton.notification.model.entity.NotificationStatus;
import com.meeton.notification.repository.NotificationRepository;
import com.meeton.notification.service.EmailService;
import com.meeton.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationConverter notificationConverter;
    private final EmailService emailService;

    @Override
    public Notification getById(String id) {
        return notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found!"));
    }

    @Override
    public List<Notification> getByUser(Long userId) {
        return notificationRepository.findByUserId(userId).stream().sorted((note1, note2) -> note1.getDate().before(note2.getDate()) ? 1 : -1).collect(Collectors.toList());
    }

    @Override
    public List<Notification> getByUserAndStatus(Long userId, NotificationStatus status) {
        return status != null ? notificationRepository.findByUserIdAndStatus(userId, status).stream()
                .sorted((note1, note2) -> note1.getDate().before(note2.getDate()) ? 1 : -1).collect(Collectors.toList()) : getByUser(userId);
    }

    @Override
    public void changeNotificationsStatus(String id, NotificationStatus status) {
        Notification notification = notificationRepository.findById(id).get();
        notification.setStatus(status);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void createNotification(Notification notification) {
        notification = notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(notification.getUserId().toString(),
                "/queue/notify",
                notificationConverter.convertBack(notification));

        emailService.sendMessage(notification.getUserEmail(), notification.getSubject(), notification.getContent());
    }
}
