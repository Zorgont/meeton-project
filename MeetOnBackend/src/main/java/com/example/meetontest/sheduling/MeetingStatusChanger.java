package com.example.meetontest.sheduling;

import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.services.MeetingPlatformsService;
import com.example.meetontest.services.MeetingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MeetingStatusChanger {

    private static final Logger log = LoggerFactory.getLogger(MeetingStatusChanger.class);
    private final MeetingService meetingService;
    private final MeetingPlatformsService meetingPlatformsService;

    @Scheduled(fixedDelay = 60000)
    public void changeStatus() {
        meetingService.getMeetingsByTags(null).forEach(meeting -> {
            if (new Date().after(meeting.getDate()) && (meeting.getStatus() == MeetingStatus.PLANNING)) {
                meeting.setStatus(MeetingStatus.IN_PROGRESS);
                log.info("Changed status of meeting " + meeting.getName() + " to " + meeting.getStatus());
                try {
                    meetingService.updateMeeting(meeting.getId(), meeting, meetingPlatformsService.getPlatformsByMeeting(meeting).stream().collect(Collectors.toSet()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (new Date().after(meeting.getEndDate()) && (meeting.getStatus() == MeetingStatus.IN_PROGRESS)) {
                meeting.setStatus(MeetingStatus.FINISHED);
                log.info("Changed status of meeting " + meeting.getName() + " to " + meeting.getStatus());
                try {
                    meetingService.updateMeeting(meeting.getId(), meeting, meetingPlatformsService.getPlatformsByMeeting(meeting).stream().collect(Collectors.toSet()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}