package com.example.meetontest.notifications.events.single.impl;

import com.example.meetontest.converters.MeetingConverter;
import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.entities.Request;
import com.example.meetontest.mail.EmailService;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.EventEntity;
import com.example.meetontest.notifications.entities.EventStatus;
import com.example.meetontest.notifications.events.single.AbstractSingleEvent;
import com.example.meetontest.notifications.services.EventStoringService;
import com.example.meetontest.notifications.services.NotificationService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MeetingChangedEvent implements AbstractSingleEvent<MeetingDTO> {
    private final MeetingService meetingService;
    private final MeetingConverter meetingConverter;
    private final RequestService requestService;
    private final EmailService mailService;
    private final NotificationService notificationService;
    private final EventStoringService eventStoringService;

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
                mailService.sendSimpleMessage(request.getUser().getEmail(), "Meeting " + meeting.getName() + " cancelled", meeting.toString());
                notificationService.createNotification(new Notification(new Date(), "Meeting " + meeting.getName() + " cancelled", request.getUser()));
            });
        } else if (meeting.getStatus() == MeetingStatus.IN_PROGRESS) {
            requestService.getByMeeting(meeting).forEach(request -> {
                mailService.sendSimpleMessage(request.getUser().getEmail(), "Meeting " + meeting.getName() + " began", meeting.toString());
                notificationService.createNotification(new Notification(new Date(), "Meeting " + meeting.getName() + " began", request.getUser()));
            });
        } else if (meeting.getStatus() == MeetingStatus.FINISHED) {
            requestService.getByMeeting(meeting).forEach(request -> {
                mailService.sendSimpleMessage(request.getUser().getEmail(), "Meeting " + meeting.getName() + " finished", meeting.toString());
                notificationService.createNotification(new Notification(new Date(), "Meeting " + meeting.getName() + " finished", request.getUser()));
            });
        }
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
            mailService.sendSimpleMessage(request.getUser().getEmail(), "Meeting " + newMeeting.getName() + " changed", stringBuilder.toString());
            notificationService.createNotification(new Notification(new Date(), "Meeting " + newMeeting.getName() + " changed", request.getUser()));
        });
    }
}
