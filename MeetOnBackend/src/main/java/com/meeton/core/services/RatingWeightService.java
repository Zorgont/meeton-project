package com.meeton.core.services;

import com.meeton.core.entities.RatingWeightType;

import java.util.Map;

public interface RatingWeightService {
    void updateByType(RatingWeightType type, double value);
    Double getValueByType(RatingWeightType type);
    Map<RatingWeightType, Double> getRatingWeightsMap();
}
