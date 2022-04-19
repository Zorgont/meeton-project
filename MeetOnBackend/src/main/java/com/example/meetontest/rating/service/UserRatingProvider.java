package com.example.meetontest.rating.service;

import com.example.meetontest.entities.User;

public interface UserRatingProvider {
    double getUserRating(User user);
}
