package com.meeton.recommendation.controller;

import com.meeton.recommendation.model.dto.MeetingDTO;
import com.meeton.recommendation.model.dto.RecommendationRequestDTO;
import com.meeton.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "recommendation-manager/v1/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping
    public List<List<MeetingDTO>> calculateRecommendations(@RequestBody RecommendationRequestDTO recommendationRequest) {
        return recommendationService.getRecommendationsByTags(recommendationRequest);
    }
}
