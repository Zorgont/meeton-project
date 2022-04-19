package com.example.meetontest.notifications;

import com.example.meetontest.notifications.events.multiple.AbstractMultipleEvent;
import com.example.meetontest.notifications.events.single.impl.MeetingChangedEvent;
import com.example.meetontest.notifications.events.multiple.impl.RequestCreatedEvent;
import com.example.meetontest.notifications.events.single.impl.RequestStatusChangedEvent;
import com.example.meetontest.notifications.events.single.AbstractSingleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class EventsConfig {
    private final MeetingChangedEvent meetingChangedEvent;
    private final RequestCreatedEvent requestCreatedEvent;
    private final RequestStatusChangedEvent requestStatusChangedEvent;

    @Bean
    public HashMap<String, AbstractSingleEvent> notificationSingleEventMap() {
        HashMap<String, AbstractSingleEvent> result = new HashMap<>();

        result.put("MeetingChangedEvent", meetingChangedEvent);
        result.put("RequestStatusChangedEvent", requestStatusChangedEvent);
        return result;
    }

    @Bean
    public HashMap<String, AbstractMultipleEvent> notificationMultipleEventMap() {
        HashMap<String, AbstractMultipleEvent> result = new HashMap<>();
        result.put("RequestCreatedEvent", requestCreatedEvent);
        return result;
    }
}
