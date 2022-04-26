package com.meeton.core.validators.impl;


import com.meeton.core.dto.RequestDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingRole;
import com.meeton.core.entities.MeetingStatus;
import com.meeton.core.entities.RequestStatus;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RequestService;
import com.meeton.core.services.impl.UserDetailsImpl;
import com.meeton.core.validators.DTOValidator;
import com.meeton.core.validators.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestValidatorImpl implements RequestValidator {
    private final RequestService requestService;
    private final MeetingService meetingService;
    private final DTOValidator dtoValidator;
    @Override
    public void validate(RequestDTO request) throws IllegalAccessException, NoSuchFieldException {
        List<String> nullFieldsList = dtoValidator.validate(request);
        Meeting meeting = meetingService.getMeetingById(request.getMeeting_id());
        if (!meeting.getIsPrivate())
            nullFieldsList.remove("about");
        if(nullFieldsList.contains("user_id") || nullFieldsList.contains("about") || nullFieldsList.contains("meeting_id"))
            throw new ValidatorException("Some fields are empty!");
        if(request.getUser_id().equals(meetingService.getManager(meeting).getId()))
            throw new ValidatorException("Creator couldn't enroll twice!");
        if(meeting.getStatus() == MeetingStatus.FINISHED)
            throw new ValidatorException("You couldn't enroll to finished meeting!");
        if(meeting.getIsParticipantAmountRestricted() && requestService.getApprovedRequestsAmount(meeting.getId()) >= meeting.getParticipantAmount())
            throw new ValidatorException("No available places!");
        if(requestService.getByMeetingIdUserId(request.getMeeting_id(), request.getUser_id()).isPresent())
            throw new ValidatorException("Request already exists!");
    }
}
