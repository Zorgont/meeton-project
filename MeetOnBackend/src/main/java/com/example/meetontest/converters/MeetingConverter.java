package com.example.meetontest.converters;

import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.dto.MeetingPlatformsDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.entities.Tag;
import com.example.meetontest.entities.TagGroup;
import com.example.meetontest.services.MeetingPlatformsService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MeetingConverter implements Converter<Meeting, MeetingDTO> {
    private final TagService tagService;
    private final MeetingPlatformsService meetingPlatformsService;
    private final MeetingPlatformsConverter meetingPlatformsConverter;

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @Autowired
    private MeetingService meetingService;

    @Override
    public Meeting convert(MeetingDTO entity) throws ParseException {
        Meeting meeting = new Meeting();
        meeting.setName(entity.getName());
        meeting.setAbout(entity.getAbout());
        meeting.setDate(df.parse(entity.getDate()));
        meeting.setEndDate(df.parse(entity.getEndDate()));
        meeting.setIsParticipantAmountRestricted(entity.getIsParticipantAmountRestricted());
        meeting.setParticipantAmount(entity.getParticipantAmount());
        meeting.setIsPrivate(entity.getIsPrivate());
        meeting.setDetails(entity.getDetails());
        meeting.setTags(tagService.getTags(entity.getTags()));

        try {
            meeting.setStatus(MeetingStatus.valueOf(entity.getStatus().toUpperCase()));
        } catch (Exception e) {
            meeting.setStatus(MeetingStatus.PLANNING);
        }
        return meeting;
    }

    @Override
    public MeetingDTO convertBack(Meeting entity) {
        return new MeetingDTO(entity.getId(),
                entity.getName(),
                df.format(entity.getDate()),
                df.format(entity.getEndDate()),
                entity.getAbout(),
                entity.getDetails(),
                meetingService.getManager(entity).getId(),
                meetingService.getManager(entity).getUsername(),
                entity.getIsPrivate(),
                entity.getIsParticipantAmountRestricted(),
                entity.getParticipantAmount(),
                entity.getStatus().toString(),
                entity.getTags().stream().map(Tag::getName).sorted().collect(Collectors.toList()),
                meetingPlatformsService.getPlatformsByMeeting(entity).stream().map(meetingPlatformsConverter::convertBack).sorted(Comparator.comparing(MeetingPlatformsDTO::getPlatformId)).collect(Collectors.toList()));
    }
}
