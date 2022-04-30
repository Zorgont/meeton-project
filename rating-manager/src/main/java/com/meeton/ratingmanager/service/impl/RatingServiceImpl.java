package com.meeton.ratingmanager.service.impl;

import com.meeton.ratingmanager.model.dto.UserComponents;
import com.meeton.ratingmanager.model.entity.RatingWeight;
import com.meeton.ratingmanager.model.entity.RatingWeightType;
import com.meeton.ratingmanager.repository.RatingWeightRepository;
import com.meeton.ratingmanager.service.RatingService;
import com.meeton.ratingmanager.service.UserRatingComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {
    private final RatingWeightRepository ratingWeightRepository;

    @Autowired
    @Qualifier("userRatingComponents")
    private List<UserRatingComponent> userRatingComponents;

    @Override
    public double calculate(UserComponents userComponents) {
        if (ratingWeightRepository.count() == 0){
            ratingWeightRepository.save(RatingWeight.builder().type(RatingWeightType.MEETING_CREATION).value(20.0).build());
            ratingWeightRepository.save(RatingWeight.builder().type(RatingWeightType.MEETING_ATTENDANCE).value(1.0).build());
            ratingWeightRepository.save(RatingWeight.builder().type(RatingWeightType.MEETING_SCORE_COEFFICIENT).value(400.0).build());
        }

        return userRatingComponents.stream()
                .mapToDouble(component -> component.getUserRating(userComponents)).sum();
    }
}
