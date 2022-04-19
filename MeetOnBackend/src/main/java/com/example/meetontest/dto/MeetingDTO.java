package com.example.meetontest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDTO {
    private Long meetingId;
    private String name;
    private String date;
    private String endDate;
    private String about;
    private String details;
    private Long managerId;
    private String managerUsername;
    private Boolean isPrivate;
    private Boolean isParticipantAmountRestricted;
    private int participantAmount;
    private String status;
    private List<String> tags;
    private List<MeetingPlatformsDTO> meetingPlatforms;
}