package com.meeton.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeetingComponents {
    long participantCount;
    long scoresCount;
    double scoresSum;
}
