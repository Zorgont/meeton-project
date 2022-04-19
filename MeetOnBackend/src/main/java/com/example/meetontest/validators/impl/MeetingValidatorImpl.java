package com.example.meetontest.validators.impl;

import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingStatus;
import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.validators.DTOValidator;
import com.example.meetontest.validators.MeetingValidator;
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
