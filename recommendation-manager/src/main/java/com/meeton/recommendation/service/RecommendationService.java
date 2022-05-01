package com.meeton.recommendation.service;

import com.meeton.recommendation.model.dto.MeetingDTO;
import com.meeton.recommendation.model.dto.RecommendationRequestDTO;

import java.util.List;

public interface RecommendationService {
    List<List<MeetingDTO>> getRecommendationsByTags(RecommendationRequestDTO recommendationRequest);
}
