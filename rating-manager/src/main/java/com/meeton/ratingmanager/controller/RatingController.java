package com.meeton.ratingmanager.controller;

import com.meeton.ratingmanager.model.dto.UserComponents;
import com.meeton.ratingmanager.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "rating-manager/v1/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public double calculateRating(@RequestBody UserComponents userComponents) {
        return ratingService.calculate(userComponents);
    }
}
