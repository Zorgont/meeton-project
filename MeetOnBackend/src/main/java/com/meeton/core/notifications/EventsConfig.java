package com.meeton.core.notifications;

import com.meeton.core.notifications.events.multiple.AbstractMultipleEvent;
import com.meeton.core.notifications.events.single.impl.MeetingChangedEvent;
import com.meeton.core.notifications.events.multiple.impl.RequestCreatedEvent;
import com.meeton.core.notifications.events.single.impl.RequestStatusChangedEvent;
import com.meeton.core.notifications.events.single.AbstractSingleEvent;
import com.meeton.core.notifications.events.multiple.AbstractMultipleEvent;
import com.meeton.core.notifications.events.multiple.impl.RequestCreatedEvent;
import com.meeton.core.notifications.events.single.AbstractSingleEvent;
import com.meeton.core.notifications.events.single.impl.MeetingChangedEvent;
import com.meeton.core.notifications.events.single.impl.RequestStatusChangedEvent;
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
