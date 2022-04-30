package com.meeton.ratingmanager.service;

import com.meeton.ratingmanager.model.dto.UserComponents;

public interface RatingService {
    double calculate(UserComponents userComponents);
}
