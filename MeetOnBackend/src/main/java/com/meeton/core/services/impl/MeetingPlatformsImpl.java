package com.meeton.core.services.impl;

import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingPlatform;
import com.meeton.core.entities.Platform;
import com.meeton.core.repositories.MeetingPlatformsRepository;
import com.meeton.core.services.MeetingPlatformsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingPlatformsImpl implements MeetingPlatformsService {
    private final MeetingPlatformsRepository meetingPlatformsRepository;

    @Override
    public MeetingPlatform create(MeetingPlatform meetingPlatforms) {
        return meetingPlatformsRepository.save(meetingPlatforms);
    }

    @Override
    public MeetingPlatform update(Long id, MeetingPlatform meetingPlatforms) {
        meetingPlatforms.setId(id);
        return meetingPlatformsRepository.save(meetingPlatforms);
    }

    @Override
    public void delete(Long id) {
        meetingPlatformsRepository.deleteById(id);
    }

    @Override
    public void delete(MeetingPlatform meetingPlatform) {
        meetingPlatformsRepository.delete(meetingPlatform);
    }

    @Override
    public List<MeetingPlatform> getPlatformsByMeeting(Meeting meeting) {
        return meetingPlatformsRepository.findByMeeting(meeting);
    }

    @Override
    public List<MeetingPlatform> getMeetingByPlatform(Platform platform) {
        return meetingPlatformsRepository.findByPlatform(platform);
    }
}
