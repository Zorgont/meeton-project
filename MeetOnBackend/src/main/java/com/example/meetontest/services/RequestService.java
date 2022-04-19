package com.example.meetontest.services;

import com.example.meetontest.entities.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Optional;

public interface RequestService {
    Request create(Request request) throws JsonProcessingException;

    Optional<Request> getById(Long id);

    List<Request> getByUser(User user);

    List<Request> getByMeeting(Meeting meeting);

    void changeStatus(Request request, RequestStatus status) throws JsonProcessingException;

    Optional<Request> getByMeetingIdUserId(Long meetingId, Long userId);

    void removeById(Long id);

    int getApprovedRequestsAmount(long meetingId);

    List<Request> getByMeetingAndStatus(Meeting meeting, RequestStatus status);

    List<Request> getByUserAndRole(User user, MeetingRole role);
}