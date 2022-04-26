package com.meeton.core.validators.impl;

import com.meeton.core.dto.ScoreDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingStatus;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RequestService;
import com.meeton.core.services.ScoreService;
import com.meeton.core.services.UserService;
import com.meeton.core.services.impl.UserDetailsImpl;
import com.meeton.core.validators.DTOValidator;
import com.meeton.core.validators.ScoreValidator;
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
