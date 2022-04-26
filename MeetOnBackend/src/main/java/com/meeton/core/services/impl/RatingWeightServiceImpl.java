package com.meeton.core.services.impl;

import com.meeton.core.entities.RatingWeight;
import com.meeton.core.entities.RatingWeightType;
import com.meeton.core.repositories.RatingWeightRepository;
import com.meeton.core.services.RatingWeightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingWeightServiceImpl implements RatingWeightService {
    private final RatingWeightRepository ratingWeightRepository;

    @Override
    public void updateByType(RatingWeightType type, double value) {
        RatingWeight weight = ratingWeightRepository.findByType(type);
        weight.setValue(value);
        ratingWeightRepository.save(weight);
    }

    @Override
    public Double getValueByType(RatingWeightType type) {
        return ratingWeightRepository.findByType(type).getValue();
    }

    public Map<RatingWeightType, Double> getRatingWeightsMap() {
        return ratingWeightRepository.findAll().stream().collect(Collectors.toMap(RatingWeight::getType, RatingWeight::getValue));
    }
}
