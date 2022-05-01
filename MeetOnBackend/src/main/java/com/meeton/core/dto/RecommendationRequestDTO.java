package com.meeton.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationRequestDTO {
    List<MeetingDTO> meetings;
    List<String> tags;
    int maxNumber;
}
