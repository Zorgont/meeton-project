package com.meeton.notification.sheduling;

import com.meeton.notification.model.entity.Notification;
import com.meeton.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataBaseClearer {
    private final NotificationRepository notificationRepository;

    @Scheduled(fixedDelay = 60000)
    private void clearEntity(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);

        calendar.add(Calendar.DATE, -6);
        List<Notification> notificationsToRemove = notificationRepository.findAll().stream().filter(notification -> notification.getDate().before(calendar.getTime())).collect(Collectors.toList());
        notificationRepository.deleteAll(notificationsToRemove);
    }
}
