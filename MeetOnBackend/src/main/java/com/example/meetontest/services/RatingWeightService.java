package com.example.meetontest.services;

import com.example.meetontest.entities.RatingWeightType;

import java.util.Map;

public interface RatingWeightService {
    void updateByType(RatingWeightType type, double value);
    Double getValueByType(RatingWeightType type);
    Map<RatingWeightType, Double> getRatingWeightsMap();
}
