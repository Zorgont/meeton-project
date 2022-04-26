package com.meeton.core.notifications.repositories;

import com.meeton.core.entities.User;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.entities.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndStatus(User user, NotificationStatus status);

}
