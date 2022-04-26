package com.meeton.core.notifications.services;

import com.meeton.core.entities.User;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.entities.NotificationStatus;

import java.util.List;

public interface NotificationService {
    Notification getById(Long id);
    List<Notification> getByUser(User user);
    List<Notification> getByUserAndStatus(User user, NotificationStatus status);
    void changeNotificationsStatus(Notification notification, NotificationStatus status);
    void createNotification(Notification notification);
}
