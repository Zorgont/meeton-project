package com.meeton.core.validators.impl;

import com.meeton.core.dto.MeetingDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingStatus;
import com.meeton.core.entities.User;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.validators.DTOValidator;
import com.meeton.core.validators.MeetingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Component
@RequiredArgsConstructor
public class MeetingValidatorImpl implements MeetingValidator {
    private final DTOValidator dtoValidator;
    @Override
    public List<String> getNullFieldsList(MeetingDTO meetingRequest) throws IllegalAccessException {
        List<String> nullFieldsList = dtoValidator.validate(meetingRequest);
        if(meetingRequest.getIsParticipantAmountRestricted() && meetingRequest.getParticipantAmount() == 0)
            nullFieldsList.add("participant_amount");
        nullFieldsList.remove("status");
        nullFieldsList.remove("meetingId");
        return nullFieldsList;
    }
    public void validate(MeetingDTO meetingRequest) throws IllegalAccessException{
        List<String> nullFieldsList = dtoValidator.validate(meetingRequest);
        if(meetingRequest.getIsParticipantAmountRestricted() && meetingRequest.getParticipantAmount() == 0)
            nullFieldsList.add("participant_amount");
        nullFieldsList.remove("status");
        nullFieldsList.remove("meetingId");
        if(!nullFieldsList.isEmpty())
            throw new ValidatorException("Some fields are empty!");
    }

    @Override
    public List<String> getFieldsList() throws IllegalAccessException {
            List<String> fieldsList = dtoValidator.getFieldsList(new MeetingDTO());
        fieldsList.remove("status");
        fieldsList.remove("meetingId");
        return fieldsList;
    }
    @Override
    public void validate(Meeting meeting) throws ValidatorException {
        if (meeting.getStatus().equals(MeetingStatus.PLANNING)) {
            // Валидация даты - проверка на то, что указанная дата в будущем:
            // todo: Добавить проверку after, чтобы нельзя было запланировать собрание через 100 лет:
            if (meeting.getDate().before(new Date()))
                throw new ValidatorException("Incorrect date!");

            if (meeting.getDate().after(meeting.getEndDate()))
                throw new ValidatorException("Date of the meeting cannot be before end date!");

            // Валидация тегов - они существуют в БД и их количество не ноль:
            if (meeting.getTags().isEmpty())
                throw new ValidatorException("Incorrect tags!");
        }
    }
}
