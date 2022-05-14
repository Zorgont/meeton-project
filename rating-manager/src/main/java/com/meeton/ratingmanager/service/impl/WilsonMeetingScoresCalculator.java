package com.meeton.ratingmanager.service.impl;

import com.meeton.ratingmanager.service.MeetingScoresCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WilsonMeetingScoresCalculator implements MeetingScoresCalculator {

    @Override
    public double calculate(double sum, double count, double min, double max) {
        log.info("sum: {}; count: {}; min: {}; max: {}", sum, count, min, max);
        double z = 1.64485;
        double width = max - min;
        double phat = (sum - count * min) / width / count;
        double rating = (phat + z * z / (2 * count) - z * Math.sqrt((phat * (1 - phat) + z * z / (4 * count)) / count)) / (1 + z * z / count);
        double result = rating * width + min;
        log.info("result: {}", result);
        return Double.isNaN(result) ? 0 : result;
    }
}