package com.example.meetontest.services;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingPlatform;
import com.example.meetontest.entities.Platform;

import java.util.List;

public interface MeetingPlatformsService {
    MeetingPlatform create(MeetingPlatform meetingPlatforms);

    MeetingPlatform update(Long id, MeetingPlatform meetingPlatforms);

    void delete(Long id);

    void delete(MeetingPlatform meetingPlatform);

    List<MeetingPlatform> getPlatformsByMeeting(Meeting meeting);

    List<MeetingPlatform> getMeetingByPlatform(Platform platform);
}
