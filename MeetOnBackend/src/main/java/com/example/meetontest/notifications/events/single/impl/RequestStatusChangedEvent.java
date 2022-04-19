package com.example.meetontest.notifications.events.single.impl;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.RequestStatus;
import com.example.meetontest.entities.User;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.EventEntity;
import com.example.meetontest.notifications.entities.EventStatus;
import com.example.meetontest.notifications.events.single.AbstractSingleEvent;
import com.example.meetontest.notifications.services.EventStoringService;
import com.example.meetontest.notifications.services.NotificationService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RequestStatusChangedEvent implements AbstractSingleEvent<RequestDTO> {

    private final EventStoringService eventStoringService;
    private final NotificationService notificationService;
    private final EmailService mailService;
    private final UserService userService;
    private final MeetingService meetingService;

    @Override
    public void process(EventEntity event) throws JsonProcessingException {
        RequestDTO oldValue = new ObjectMapper().readValue(event.getBody(), new TypeReference<Map<String, RequestDTO>>() {}).get("old");
        RequestDTO newValue = new ObjectMapper().readValue(event.getBody(), new TypeReference<Map<String, RequestDTO>>() {}).get("new");
        requestChanged(newValue, RequestStatus.valueOf(oldValue.getStatus()), RequestStatus.valueOf(newValue.getStatus()));
        event.setStatus(EventStatus.HANDLED);
        eventStoringService.updateEvent(event);
    }

    private void requestChanged(RequestDTO request, RequestStatus oldStatus, RequestStatus newStatus) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("Your request on meeting ")
                .append(meetingService.getMeetingById(request.getMeeting_id()).getName())
                .append(" was changed from ")
                .append(oldStatus.toString().toLowerCase())
                .append(" to ")
                .append(newStatus.toString().toLowerCase());

        User user = userService.getUserById(request.getUser_id());
        mailService.sendSimpleMessage(user.getEmail(), "Request changing", stringBuilder.toString());
        notificationService.createNotification(new Notification(new Date(), stringBuilder.toString(), user));
    }
}
