package com.example.meetontest.sheduling;

import com.example.meetontest.entities.ConfirmationToken;
import com.example.meetontest.notifications.entities.EventEntity;
import com.example.meetontest.notifications.entities.EventStatus;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.repositories.EventRepository;
import com.example.meetontest.notifications.repositories.NotificationRepository;
import com.example.meetontest.repositories.ConfirmationTokenRepository;
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
