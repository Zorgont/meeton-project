package com.meeton.ratingmanager.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserComponents {
    Long id;
    String username;
    String email;
    List<MeetingComponents> meetingComponents;
}
