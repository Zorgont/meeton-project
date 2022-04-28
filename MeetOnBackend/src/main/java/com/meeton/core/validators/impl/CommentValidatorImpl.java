package com.meeton.core.validators.impl;

import com.meeton.core.dto.CommentDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingStatus;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.services.MeetingService;
import com.meeton.core.validators.CommentValidator;
import com.meeton.core.validators.DTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentValidatorImpl implements CommentValidator {
    private final MeetingService meetingService;
    private final DTOValidator dtoValidator;

    @Override
    public void validate(CommentDTO comment) throws IllegalAccessException {
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
