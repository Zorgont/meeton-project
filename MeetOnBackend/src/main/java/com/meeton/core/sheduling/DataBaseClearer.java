package com.meeton.core.sheduling;

import com.meeton.core.entities.ConfirmationToken;
import com.meeton.core.notifications.entities.EventEntity;
import com.meeton.core.notifications.entities.EventStatus;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.repositories.EventRepository;
import com.meeton.core.notifications.repositories.NotificationRepository;
import com.meeton.core.repositories.ConfirmationTokenRepository;
import com.meeton.core.notifications.entities.EventEntity;
import com.meeton.core.notifications.entities.EventStatus;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.repositories.EventRepository;
import com.meeton.core.notifications.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataBaseClearer {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EventRepository eventRepository;
    private final NotificationRepository notificationRepository;

    @Scheduled(fixedDelay = 60000)
    private void clearEntity(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);

        List<ConfirmationToken> tokensToRemove = confirmationTokenRepository.findAll().stream()
            .filter(token -> token.getCreatedDate().before(calendar.getTime())).collect(Collectors.toList());
        confirmationTokenRepository.deleteAll(tokensToRemove);

        List<EventEntity> eventsToRemove = eventRepository.findByStatus(EventStatus.HANDLED).stream()
            .filter(event -> event.getDate().before(calendar.getTime())).collect(Collectors.toList());
        eventRepository.deleteAll(eventsToRemove);

        calendar.add(Calendar.DATE, -6);
        List<Notification> notificationsToRemove = notificationRepository.findAll().stream().filter(notification -> notification.getDate().before(calendar.getTime())).collect(Collectors.toList());
        notificationRepository.deleteAll(notificationsToRemove);
    }
}
