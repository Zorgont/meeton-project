package com.example.meetontest.rating.component.impl;

import com.example.meetontest.entities.RatingWeightType;
import com.example.meetontest.entities.User;
import com.example.meetontest.rating.component.UserRatingComponent;
import com.example.meetontest.rating.utils.MeetingScoresCalculator;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.RatingWeightService;
import com.example.meetontest.services.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingScoresUserRatingComponent implements UserRatingComponent {
    private final ScoreService scoreService;
    private final MeetingService meetingService;
    private final MeetingScoresCalculator meetingScoresCalculator;
    private final RatingWeightService weightService;
    @Override
    public double getUserRating(User user) {
        final double minScoreValue = 0;
        final double maxScoreValue = 5;

        try {
            double result = meetingService.getMeetingsByManager(user).stream().map(scoreService::getScoresByMeeting)
                    .mapToDouble(scores -> meetingScoresCalculator.calculate(scores, minScoreValue, maxScoreValue)).sum() * weightService.getValueByType(RatingWeightType.MEETING_SCORE_COEFFICIENT);
            return Double.isNaN(result) ? 0 : result;
        } catch (Exception e) {
            System.out.println("Couldn't calculate meeting scores for userId: " + user.getId());
            return 0;
        }
    }
}
