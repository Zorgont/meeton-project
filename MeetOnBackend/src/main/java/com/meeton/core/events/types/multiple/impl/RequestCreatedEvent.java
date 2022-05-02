package com.meeton.core.events.types.multiple.impl;

import com.meeton.core.dto.NotificationDTO;
import com.meeton.core.dto.RequestDTO;
import com.meeton.core.dto.UserDTO;
import com.meeton.core.entities.EventEntity;
import com.meeton.core.entities.EventStatus;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.User;
import com.meeton.core.events.EventStoringService;
import com.meeton.core.events.types.multiple.AbstractMultipleEvent;
import com.meeton.core.services.MeetingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeton.core.services.client.ServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestCreatedEvent implements AbstractMultipleEvent<Long, RequestDTO> {
    private final MeetingService meetingService;
    private final EventStoringService eventStoringService;
    private final ServiceClient<Void, NotificationDTO> notificationManagerKafkaClient;

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
        events.forEach(event -> {
            event.setStatus(EventStatus.HANDLED);
            eventStoringService.updateEvent(event);
        });

        Meeting meeting = meetingService.getMeetingById(meetingId);
        User user = meetingService.getManager(meeting);

        NotificationDTO dto = NotificationDTO.builder()
                .subject("New requests for meeting " + meeting.getName())
                .content("You have new " + events.size() + " requests on meeting " + meeting.getName())
                .user(UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .build();

        notificationManagerKafkaClient.execute(dto);
    }
}
