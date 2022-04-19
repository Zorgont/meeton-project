package com.example.meetontest.validators.impl;

import com.example.meetontest.dto.CommentDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.services.CommentService;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.impl.UserDetailsImpl;
import com.example.meetontest.validators.CommentValidator;
import com.example.meetontest.validators.DTOValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentValidatorImpl implements CommentValidator {
    private final CommentService commentService;
    private final MeetingService meetingService;
    private final DTOValidator dtoValidator;

    @Override
    public void validate(CommentDTO comment) throws IllegalAccessException, NoSuchFieldException {
        List<String> nullFieldsList = dtoValidator.validate(comment);
        Meeting meeting = meetingService.getMeetingById(comment.getMeeting_id());

        nullFieldsList.remove("id");
        nullFieldsList.remove("date");
        if(!nullFieldsList.isEmpty())
            throw new ValidatorException("Some fields are empty!");
        if(!((meeting.getStatus() == MeetingStatus.FINISHED) || (meeting.getStatus() == MeetingStatus.IN_PROGRESS)))
            throw new ValidatorException("You couldn't comment meeting which hasn't begun yet!");
    }
}
