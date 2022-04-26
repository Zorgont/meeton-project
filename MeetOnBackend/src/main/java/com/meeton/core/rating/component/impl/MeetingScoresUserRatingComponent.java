package com.meeton.core.rating.component.impl;

import com.meeton.core.entities.RatingWeightType;
import com.meeton.core.entities.User;
import com.meeton.core.rating.component.UserRatingComponent;
import com.meeton.core.rating.utils.MeetingScoresCalculator;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RatingWeightService;
import com.meeton.core.services.ScoreService;
import com.meeton.core.entities.RatingWeightType;
import com.meeton.core.entities.User;
import com.meeton.core.rating.component.UserRatingComponent;
import com.meeton.core.rating.utils.MeetingScoresCalculator;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RatingWeightService;
import com.meeton.core.services.ScoreService;
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
