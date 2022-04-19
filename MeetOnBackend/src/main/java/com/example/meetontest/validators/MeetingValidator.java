package com.example.meetontest.validators;

import com.example.meetontest.dto.MeetingDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.User;

import java.util.List;

public interface MeetingValidator{
    void validate(MeetingDTO meetingRequest) throws IllegalAccessException;
    List<String> getNullFieldsList(MeetingDTO meetingRequest) throws IllegalAccessException;
    List<String> getFieldsList() throws IllegalAccessException;
    void validate(Meeting meeting);
}
