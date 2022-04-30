package com.meeton.ratingmanager.service.impl;

import com.meeton.ratingmanager.service.MeetingScoresCalculator;
import org.springframework.stereotype.Service;

@Service
public class WilsonMeetingScoresCalculator implements MeetingScoresCalculator {

    @Override
    public double calculate(double sum, double count, double min, double max) {
        double z = 1.64485;
        double width = max - min;
        double phat = (sum - count * min) / width / count;
        double rating = (phat + z * z / (2 * count) - z * Math.sqrt((phat * (1 - phat) + z * z / (4 * count)) / count)) / (1 + z * z / count);
        return rating * width + min;
    }
}