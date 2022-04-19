package com.example.meetontest.services.impl;

import com.example.meetontest.converters.RequestConverter;
import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.*;
import com.example.meetontest.notifications.events.multiple.impl.RequestCreatedEvent;
import com.example.meetontest.notifications.events.single.impl.RequestStatusChangedEvent;
import com.example.meetontest.notifications.services.EventStoringService;
import com.example.meetontest.repositories.RequestRepository;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final MeetingService meetingService;
    private final UserService userService;
    private final EventStoringService eventStoringService;
    private final RequestConverter requestConverter;
    private final RequestStatusChangedEvent requestStatusChangedEvent;
    private final RequestCreatedEvent requestCreatedEvent;

    @Override
    public Request create(Request request) throws JsonProcessingException {
        if (request.getRole() == MeetingRole.PARTICIPANT) {
            if (request.getMeeting().getIsPrivate())
                request.setStatus(RequestStatus.PENDING);
            else if (!request.getMeeting().getIsPrivate())
                request.setStatus(RequestStatus.APPROVED);
        }

        requestRepository.save(request);
        if (request.getRole() != MeetingRole.MANAGER)
            eventStoringService.saveEvent(requestCreatedEvent.toEntity(this, new Date(), null, requestConverter.convertBack(request)));
        return request;
    }

    @Override
    public Optional<Request> getById(Long id) {
        return requestRepository.findById(id);
    }

    @Override
    public List<Request> getByUser(User user) {
        return requestRepository.findByUser(user);
    }

    @Override
    public List<Request> getByMeeting(Meeting meeting) {
        return requestRepository.findByMeeting(meeting);
    }

    @Override
    @Transactional
    public void changeStatus(Request request, RequestStatus status) throws JsonProcessingException {
        RequestDTO oldRequest = requestConverter.convertBack(request);
        request.setStatus(status);
        requestRepository.save(request);
        eventStoringService.saveEvent(requestStatusChangedEvent.toEntity(this, new Date(), oldRequest, requestConverter.convertBack(request)));
    }

    @Override
    public Optional<Request> getByMeetingIdUserId(Long meetingId, Long userId) {
        return requestRepository.findByMeetingAndUser(meetingService.getMeetingById(meetingId), userService.getUserById(userId));
    }

    @Override
    public void removeById(Long id) {
        requestRepository.deleteById(id);
    }

    @Override
    public int getApprovedRequestsAmount(long meetingId) {
        return requestRepository.countByMeetingAndStatus(meetingService.getMeetingById(meetingId), RequestStatus.APPROVED);
    }

    @Override
    public List<Request> getByMeetingAndStatus(Meeting meeting, RequestStatus status) {
        return requestRepository.findByMeetingAndStatus(meeting, status);
    }

    @Override
    public List<Request> getByUserAndRole(User user, MeetingRole role) {
        return requestRepository.findByUserAndRole(user, role);
    }
}