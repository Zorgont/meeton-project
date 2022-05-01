package com.meeton.recommendation.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class MeetingDTO {
    private Long meetingId;
    private List<String> tags;
}
