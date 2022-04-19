package com.example.meetontest.services.impl;

import com.example.meetontest.entities.RatingWeight;
import com.example.meetontest.entities.RatingWeightType;
import com.example.meetontest.repositories.RatingWeightRepository;
import com.example.meetontest.services.RatingWeightService;
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
