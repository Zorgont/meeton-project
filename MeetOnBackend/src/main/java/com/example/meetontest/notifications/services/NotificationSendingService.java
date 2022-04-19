package com.example.meetontest.notifications.services;

import com.example.meetontest.notifications.entities.EventEntity;
import com.example.meetontest.notifications.events.multiple.AbstractMultipleEvent;
import com.example.meetontest.notifications.events.single.AbstractSingleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationSendingService {
    private final EventStoringService eventStoringService;

    @Autowired
    @Qualifier("notificationSingleEventMap")
    Map<String, AbstractSingleEvent> notificationSingleEventMap;

    @Autowired
    @Qualifier("notificationMultipleEventMap")
    Map<String, AbstractMultipleEvent> notificationMultipleEventMap;

    @Scheduled(fixedDelay = 10000)
    public void checkEvents() {
        eventStoringService.getUnhandledEventsByTypes(notificationSingleEventMap.keySet()).forEach(notificationEvent -> {
            try {
                notificationSingleEventMap.get(notificationEvent.getType()).process(notificationEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // once per 1 min.
    @Scheduled(fixedDelay = 60000)
    public void checkUnsentRequestCreatedEvents() {
        eventStoringService.getUnhandledEventsByTypes(notificationMultipleEventMap.keySet()).stream()
            .collect(Collectors.groupingBy(EventEntity::getType)).forEach((eventType, events) -> {
                AbstractMultipleEvent event = notificationMultipleEventMap.get(eventType);
                event.preprocess(events).forEach((meetingId, eventList) -> event.process(meetingId, (List<EventEntity>) eventList));
            });
    }
}
