package com.meeton.core.services;

import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingPlatform;
import com.meeton.core.entities.User;
import com.meeton.core.exceptions.ValidatorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.meeton.core.exceptions.ValidatorException;

import java.util.List;
import java.util.Set;

public interface MeetingService {
    List<Meeting> getMeetingsByTags(List<String> tags);

    Meeting createMeeting(Meeting meetingRequest, User manager, Set<MeetingPlatform> meetingPlatforms) throws ValidatorException, JsonProcessingException;

    Meeting getMeetingById(Long id);

    boolean existsById(Long id);

    User getManager(Meeting meeting);

    boolean deleteMeeting(Long id);

    Meeting updateMeeting(Long id, Meeting meetingRequest, Set<MeetingPlatform> meetingPlatforms) throws ValidatorException, JsonProcessingException;

    List<Meeting> getMeetingsByManager(User manager);
}