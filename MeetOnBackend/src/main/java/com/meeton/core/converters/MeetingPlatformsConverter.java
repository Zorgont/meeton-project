package com.meeton.core.converters;

import com.meeton.core.dto.MeetingPlatformsDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingPlatform;
import com.meeton.core.entities.Platform;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.PlatformService;
import com.meeton.core.exceptions.ValidatorException;
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
