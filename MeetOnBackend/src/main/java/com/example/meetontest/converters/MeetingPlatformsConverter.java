package com.example.meetontest.converters;

import com.example.meetontest.dto.MeetingPlatformsDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingPlatform;
import com.example.meetontest.entities.Platform;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MeetingPlatformsConverter implements Converter<MeetingPlatform, MeetingPlatformsDTO> {
    private final PlatformService platformService;
    private final MeetingService meetingService;

    @Override
    public MeetingPlatform convert(MeetingPlatformsDTO entity) throws ParseException, ValidatorException {
        Meeting meeting = (entity.getMeetingId() != null && meetingService.existsById(entity.getMeetingId())) ? meetingService.getMeetingById(entity.getMeetingId()) : null;
        Optional<Platform> tryPlatform = platformService.getById(entity.getPlatformId());
        Platform platform = tryPlatform.isPresent() ? tryPlatform.get() : null;

        if (entity.getAddress() == null || entity.getAddress().isEmpty())
            throw new ValidatorException("Address cannot be empty!");

        return new MeetingPlatform(meeting, platform, entity.getAddress());
    }

    @Override
    public MeetingPlatformsDTO convertBack(MeetingPlatform entity) {
        return new MeetingPlatformsDTO(entity.getId(), entity.getMeeting().getId(), entity.getPlatform().getId(), entity.getAddress());
    }
}
