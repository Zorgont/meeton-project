package com.meeton.core.validators;

import com.meeton.core.dto.MeetingDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.User;

import java.util.List;

public interface MeetingValidator{
    void validate(MeetingDTO meetingRequest) throws IllegalAccessException;
    List<String> getNullFieldsList(MeetingDTO meetingRequest) throws IllegalAccessException;
    List<String> getFieldsList() throws IllegalAccessException;
    void validate(Meeting meeting);
}
