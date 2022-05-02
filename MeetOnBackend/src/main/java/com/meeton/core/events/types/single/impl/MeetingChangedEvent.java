package com.meeton.core.events.types.single.impl;

import com.meeton.core.converters.MeetingConverter;
import com.meeton.core.dto.MeetingDTO;
import com.meeton.core.dto.NotificationDTO;
import com.meeton.core.dto.UserDTO;
import com.meeton.core.entities.*;
import com.meeton.core.events.EventStoringService;
import com.meeton.core.events.types.single.AbstractSingleEvent;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeton.core.services.client.ServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MeetingChangedEvent implements AbstractSingleEvent<MeetingDTO> {
    private final MeetingService meetingService;
    private final MeetingConverter meetingConverter;
    private final RequestService requestService;
    private final EventStoringService eventStoringService;
    private final ServiceClient<Void, NotificationDTO> notificationManagerKafkaClient;

    @Override
    public void process(EventEntity event) throws JsonProcessingException, ParseException {
        MeetingDTO oldValue = new ObjectMapper().readValue(event.getBody(), new TypeReference<Map<String, MeetingDTO>>() {}).get("old");
        MeetingDTO newValue = new ObjectMapper().readValue(event.getBody(), new TypeReference<Map<String, MeetingDTO>>() {}).get("new");

        Meeting oldMeeting = meetingConverter.convert(oldValue);
        Meeting newMeeting = meetingConverter.convert(newValue);
        newMeeting.setId(newValue.getMeetingId());
        if (!oldMeeting.getStatus().equals(newMeeting.getStatus()))
            statusChanged(newMeeting);
        else infoChanged(oldMeeting, newMeeting);

        event.setStatus(EventStatus.HANDLED);
        eventStoringService.updateEvent(event);
    }

    private void statusChanged(Meeting meeting) {

        if (meeting.getStatus() == MeetingStatus.CANCELED) {
            requestService.getByMeeting(meeting).forEach(request -> {
                sendNotification("Meeting " + meeting.getName() + " cancelled", request.getUser());
            });
        } else if (meeting.getStatus() == MeetingStatus.IN_PROGRESS) {
            requestService.getByMeeting(meeting).forEach(request -> {
                sendNotification("Meeting " + meeting.getName() + " began", request.getUser());
            });
        } else if (meeting.getStatus() == MeetingStatus.FINISHED) {
            requestService.getByMeeting(meeting).forEach(request -> {
                sendNotification("Meeting " + meeting.getName() + " finished", request.getUser());
            });
        }
    }

    private void sendNotification(String content, User user) {
        sendNotification(content, content, user);
    }

    private void sendNotification(String content, String subject, User user) {
        NotificationDTO dto = NotificationDTO.builder()
                .subject(content)
                .content(content)
                .user(UserDTO.builder().id(user.getId()).email(user.getEmail()).username(user.getUsername()).build())
                .build();
        notificationManagerKafkaClient.execute(dto);
    }

    private void infoChanged(Meeting oldMeeting, Meeting newMeeting) {
        StringBuilder stringBuilder = new StringBuilder("Information of this meeting was changed:" + '\n');
        if (!oldMeeting.getName().equals(newMeeting.getName()))
            stringBuilder.append("Name from ").append(oldMeeting.getName()).append(" to ").append(newMeeting.getName()).append('\n');
        if (!oldMeeting.getDate().equals(newMeeting.getDate()))
            stringBuilder.append("Date from ").append(oldMeeting.getDate()).append(" to ").append(newMeeting.getDate()).append('\n');
        if (!oldMeeting.getEndDate().equals(newMeeting.getEndDate()))
            stringBuilder.append("End date from ").append(oldMeeting.getEndDate()).append(" to ").append(newMeeting.getEndDate()).append('\n');
        if (!(oldMeeting.getParticipantAmount() == (newMeeting.getParticipantAmount())))
            stringBuilder.append("Participant Amount from ").append(oldMeeting.getParticipantAmount()).append(" to ").append(newMeeting.getParticipantAmount()).append('\n');
        if (!(oldMeeting.getIsPrivate() == (newMeeting.getIsPrivate())))
            stringBuilder.append("Private from ").append(oldMeeting.getIsPrivate()).append(" to ").append(newMeeting.getIsPrivate()).append('\n');
        if (!(oldMeeting.getIsParticipantAmountRestricted() == (newMeeting.getIsParticipantAmountRestricted())))
            stringBuilder.append("Participant Amount Restricted from ").append(oldMeeting.getIsParticipantAmountRestricted()).append(" to ").append(newMeeting.getIsParticipantAmountRestricted()).append('\n');
        if (!oldMeeting.getAbout().equals(newMeeting.getAbout()))
            stringBuilder.append("About from ").append(oldMeeting.getAbout()).append(" to ").append(newMeeting.getAbout()).append('\n');
        if (!oldMeeting.getDetails().equals(newMeeting.getDetails()))
            stringBuilder.append("Details from ").append(oldMeeting.getDetails()).append(" to ").append(newMeeting.getDetails()).append('\n');

        List<Request> requestList = requestService.getByMeeting(meetingService.getMeetingById(newMeeting.getId()));
        requestList.forEach(request -> {
            sendNotification("Meeting " + newMeeting.getName() + " changed", stringBuilder.toString(), request.getUser());
        });
    }
}
