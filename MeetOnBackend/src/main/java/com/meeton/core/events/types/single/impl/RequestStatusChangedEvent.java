package com.meeton.core.events.types.single.impl;

import com.meeton.core.dto.NotificationDTO;
import com.meeton.core.dto.RequestDTO;
import com.meeton.core.dto.UserDTO;
import com.meeton.core.entities.EventEntity;
import com.meeton.core.entities.EventStatus;
import com.meeton.core.entities.RequestStatus;
import com.meeton.core.entities.User;
import com.meeton.core.events.EventStoringService;
import com.meeton.core.events.types.single.AbstractSingleEvent;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.NotificationManagerClient;
import com.meeton.core.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RequestStatusChangedEvent implements AbstractSingleEvent<RequestDTO> {

    private final EventStoringService eventStoringService;
    private final UserService userService;
    private final MeetingService meetingService;
    private final NotificationManagerClient notificationManagerClient;

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
        notificationManagerClient.sendNotification(
                NotificationDTO.builder()
                        .subject("Your request's status is changed")
                        .content(stringBuilder.toString())
                        .user(UserDTO.builder().id(user.getId()).email(user.getEmail()).username(user.getUsername()).build())
                        .build());
    }
}
