package com.meeton.ratingmanager.service.impl;

import com.meeton.ratingmanager.model.dto.UserComponents;
import com.meeton.ratingmanager.model.entity.RatingWeight;
import com.meeton.ratingmanager.model.entity.RatingWeightType;
import com.meeton.ratingmanager.repository.RatingWeightRepository;
import com.meeton.ratingmanager.service.UserRatingComponent;
import com.meeton.ratingmanager.service.MeetingScoresCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingScoresUserRatingComponent implements UserRatingComponent {
    private final RatingWeightRepository ratingWeightRepository;
    private final MeetingScoresCalculator meetingScoresCalculator;
    private final double minScoreValue = 0;
    private final double maxScoreValue = 5;

    @Override
    public double getUserRating(UserComponents userComponents) {
        try {
            RatingWeight ratingWeight = ratingWeightRepository.findByType(RatingWeightType.MEETING_SCORE_COEFFICIENT).get();

            double sum = userComponents.getMeetingComponents().stream()
                    .mapToDouble(meeting ->
                            meetingScoresCalculator.calculate(
                                    meeting.getScoresSum(), meeting.getScoresCount(), minScoreValue, maxScoreValue))
                    .sum();

            double result = sum * ratingWeight.getValue();
            return Double.isNaN(result) ? 0 : result;
        } catch (Exception e) {
            log.error("Couldn't calculate meeting scores for user {}. Details: {}", userComponents.getId(), e.getMessage());
            return 0;
        }
    }
}
