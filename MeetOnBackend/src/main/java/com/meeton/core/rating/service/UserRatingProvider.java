package com.meeton.core.rating.service;

import com.meeton.core.entities.User;

public interface UserRatingProvider {
    double getUserRating(User user);
}
