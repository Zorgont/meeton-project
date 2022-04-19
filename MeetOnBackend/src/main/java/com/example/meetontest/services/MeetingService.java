package com.example.meetontest.services;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingPlatform;
import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ValidatorException;
import com.fasterxml.jackson.core.JsonProcessingException;

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