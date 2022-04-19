package com.example.meetontest.rating.utils;

import com.example.meetontest.entities.MeetingScore;

import java.util.List;

public interface MeetingScoresCalculator {
    double calculate(List<MeetingScore> scores, double min, double max);
}
