package com.example.meetontest.notifications.repositories;

import com.example.meetontest.entities.User;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndStatus(User user, NotificationStatus status);

}
