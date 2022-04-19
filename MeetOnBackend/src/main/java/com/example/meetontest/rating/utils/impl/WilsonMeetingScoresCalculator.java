package com.example.meetontest.rating.utils.impl;

import com.example.meetontest.entities.MeetingScore;
import com.example.meetontest.rating.utils.MeetingScoresCalculator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WilsonMeetingScoresCalculator implements MeetingScoresCalculator {

    @Override
    public double calculate(List<MeetingScore> scores, double min, double max) {
        double z = 1.64485;
        double width = max - min;
        double count = scores.size();
        double sum = scores.stream().mapToDouble(MeetingScore::getScore).sum();
        double phat = (sum - count * min) / width / count;
        double rating = (phat + z * z / (2 * count) - z * Math.sqrt((phat * (1 - phat) + z * z / (4 * count)) / count)) / (1 + z * z / count);
        return rating * width + min;
    }
}