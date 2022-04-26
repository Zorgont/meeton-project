package com.meeton.core.services;

import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingPlatform;
import com.meeton.core.entities.Platform;

import java.util.List;

public interface MeetingPlatformsService {
    MeetingPlatform create(MeetingPlatform meetingPlatforms);

    MeetingPlatform update(Long id, MeetingPlatform meetingPlatforms);

    void delete(Long id);

    void delete(MeetingPlatform meetingPlatform);

    List<MeetingPlatform> getPlatformsByMeeting(Meeting meeting);

    List<MeetingPlatform> getMeetingByPlatform(Platform platform);
}
