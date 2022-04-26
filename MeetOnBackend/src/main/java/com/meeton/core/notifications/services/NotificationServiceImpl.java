package com.meeton.core.notifications.services;

import com.meeton.core.entities.User;
import com.meeton.core.exceptions.ResourceNotFoundException;
import com.meeton.core.notifications.converters.NotificationConverter;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.entities.NotificationStatus;
import com.meeton.core.notifications.repositories.NotificationRepository;
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

    @Override
    public Notification getById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found!"));
    }

    @Override
    public List<Notification> getByUser(User user) {
        return notificationRepository.findByUser(user).stream().sorted((note1, note2) -> note1.getDate().before(note2.getDate()) ? 1 : -1).collect(Collectors.toList());
    }

    @Override
    public List<Notification> getByUserAndStatus(User user, NotificationStatus status) {
        return status != null ? notificationRepository.findByUserAndStatus(user, status).stream()
                .sorted((note1, note2) -> note1.getDate().before(note2.getDate()) ? 1 : -1).collect(Collectors.toList()) : getByUser(user);
    }

    @Override
    public void changeNotificationsStatus(Notification notification, NotificationStatus status) {
        notification.setStatus(status);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void createNotification(Notification notification) {

        notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(notification.getUser().getId().toString(),
                "/queue/notify",
                notificationConverter.convertBack(notification));
    }
}
