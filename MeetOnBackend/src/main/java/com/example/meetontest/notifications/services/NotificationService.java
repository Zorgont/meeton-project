package com.example.meetontest.notifications.services;

import com.example.meetontest.entities.User;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationStatus;

import java.util.List;

public interface NotificationService {
    Notification getById(Long id);
    List<Notification> getByUser(User user);
    List<Notification> getByUserAndStatus(User user, NotificationStatus status);
    void changeNotificationsStatus(Notification notification, NotificationStatus status);
    void createNotification(Notification notification);
}
