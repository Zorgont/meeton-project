package com.meeton.notification.service;

import com.meeton.notification.model.entity.Notification;
import com.meeton.notification.model.entity.NotificationStatus;

import java.util.List;

public interface NotificationService {
    Notification getById(String id);
    List<Notification> getByUser(Long userId);
    List<Notification> getByUserAndStatus(Long userId, NotificationStatus status);
    void changeNotificationsStatus(String id, NotificationStatus status);
    void createNotification(Notification notification);
}
