package com.example.meetontest.validators.impl;

import com.example.meetontest.dto.ScoreDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RequestService;
import com.example.meetontest.services.ScoreService;
import com.example.meetontest.services.UserService;
import com.example.meetontest.services.impl.UserDetailsImpl;
import com.example.meetontest.validators.DTOValidator;
import com.example.meetontest.validators.ScoreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScoreValidatorImpl implements ScoreValidator {
    private final MeetingService meetingService;
    private final DTOValidator dtoValidator;
    @Override
    public void validate(ScoreDTO score) throws IllegalAccessException, NoSuchFieldException {
        List<String> nullFieldsList = dtoValidator.validate(score);
        nullFieldsList.remove("id");
        nullFieldsList.remove("date");
        Meeting meeting = meetingService.getMeetingById(score.getMeeting_id());
        if(!nullFieldsList.isEmpty())
            throw new ValidatorException("Some fields are empty!");
        if(meetingService.getManager(meeting).getId().equals(score.getUser_id()))
            throw new ValidatorException("Creator can't estimate his meeting!");
        if(!((meeting.getStatus() == MeetingStatus.IN_PROGRESS) || (meeting.getStatus() == MeetingStatus.FINISHED)))
            throw new ValidatorException("You can't estimate meeting which hasn't begun yet");
        if (score.getScore() < 1 || score.getScore() > 5)
            throw new ValidatorException("Score must be between 1 and 5");
    }
}
