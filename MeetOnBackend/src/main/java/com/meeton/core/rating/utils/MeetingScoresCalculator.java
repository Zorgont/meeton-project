package com.meeton.core.rating.utils;

import com.meeton.core.entities.MeetingScore;

import java.util.List;

public interface MeetingScoresCalculator {
    double calculate(List<MeetingScore> scores, double min, double max);
}
