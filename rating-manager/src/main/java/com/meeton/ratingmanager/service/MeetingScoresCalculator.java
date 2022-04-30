package com.meeton.ratingmanager.service;

public interface MeetingScoresCalculator {
    double calculate(double sum, double count, double min, double max);
}
