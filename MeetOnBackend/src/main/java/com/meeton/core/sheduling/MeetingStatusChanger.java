package com.meeton.core.sheduling;

import com.meeton.core.entities.MeetingStatus;
import com.meeton.core.services.MeetingPlatformsService;
import com.meeton.core.services.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingStatusChanger {
    private final MeetingService meetingService;
    private final MeetingPlatformsService meetingPlatformsService;

    @Scheduled(fixedDelay = 60000)
    public void changeStatus() {
        meetingService.getMeetingsByTags(null).forEach(meeting -> {
            boolean changed = false;
            if (new Date().after(meeting.getDate()) && (meeting.getStatus() == MeetingStatus.PLANNING)) {
                meeting.setStatus(MeetingStatus.IN_PROGRESS);
                changed = true;
            }
            if (new Date().after(meeting.getEndDate()) && (meeting.getStatus() == MeetingStatus.IN_PROGRESS)) {
                meeting.setStatus(MeetingStatus.FINISHED);
                changed = true;
            }

            if (changed) {
                log.info("Changed status of meeting " + meeting.getName() + " to " + meeting.getStatus());
                try {
                    meetingService.updateMeeting(meeting.getId(), meeting, new HashSet<>(meetingPlatformsService.getPlatformsByMeeting(meeting)));
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}