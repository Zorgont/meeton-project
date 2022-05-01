package com.meeton.recommendation.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationRequestDTO {
    List<MeetingDTO> meetings;
    List<String> tags;
    int maxNumber;
}
