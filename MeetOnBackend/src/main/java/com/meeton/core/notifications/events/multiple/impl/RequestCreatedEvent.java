package com.meeton.core.notifications.events.multiple.impl;

import com.meeton.core.dto.RequestDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.mail.EmailService;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.entities.EventEntity;
import com.meeton.core.notifications.entities.EventStatus;
import com.meeton.core.notifications.events.multiple.AbstractMultipleEvent;
import com.meeton.core.notifications.services.EventStoringService;
import com.meeton.core.notifications.services.NotificationService;
import com.meeton.core.services.MeetingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeton.core.notifications.entities.EventEntity;
import com.meeton.core.notifications.entities.EventStatus;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.services.EventStoringService;
import com.meeton.core.notifications.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestCreatedEvent implements AbstractMultipleEvent<Long, RequestDTO> {
    private final MeetingService meetingService;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final EventStoringService eventStoringService;

    @Override
    public Map<Long, List<EventEntity>> preprocess(List<EventEntity> events) {
        return events.stream().collect(Collectors.groupingBy(event -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, RequestDTO> map = objectMapper.readValue(event.getBody(), new TypeReference<Map<String, RequestDTO>>() {
                });

                return meetingService.getMeetingById(map.get("new").getMeeting_id()).getId();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }));
    }

    @Override
    public void process(Long meetingId, List<EventEntity> events) {
        Meeting meeting = meetingService.getMeetingById(meetingId);
        emailService.sendSimpleMessage(meetingService.getManager(meeting).getEmail(), "New requests for meeting " + meeting.getName(), "You have new " + events.size() + " requests on meeting " + meeting.getName());
        events.forEach(event -> {
            event.setStatus(EventStatus.HANDLED);
            eventStoringService.updateEvent(event);
        });
        notificationService.createNotification(new Notification(new Date(), events.size() + " new requests on meeting " + meeting.getName(), meetingService.getManager(meeting)));
    }
}
