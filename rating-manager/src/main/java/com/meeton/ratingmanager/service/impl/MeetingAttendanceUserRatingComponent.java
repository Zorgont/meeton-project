package com.meeton.ratingmanager.service.impl;

import com.meeton.ratingmanager.model.dto.MeetingComponents;
import com.meeton.ratingmanager.model.dto.UserComponents;
import com.meeton.ratingmanager.model.entity.RatingWeight;
import com.meeton.ratingmanager.model.entity.RatingWeightType;
import com.meeton.ratingmanager.repository.RatingWeightRepository;
import com.meeton.ratingmanager.service.UserRatingComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingAttendanceUserRatingComponent implements UserRatingComponent {
    private final RatingWeightRepository ratingWeightRepository;

    @Override
    public double getUserRating(UserComponents userComponents) {
        try {
            RatingWeight ratingWeight = ratingWeightRepository.findByType(RatingWeightType.MEETING_ATTENDANCE).get();
            int totalParticipantsCount = userComponents.getMeetingComponents().stream()
                    .mapToInt(MeetingComponents::getParticipantCount).sum();
            double result = totalParticipantsCount * ratingWeight.getValue();
            return Double.isNaN(result) ? 0 : result;
        } catch (Exception e) {
            log.error("Couldn't calculate meeting attendance for user {}. Details: {}", userComponents.getId(), e.getMessage());
            return 0;
        }
    }
}
