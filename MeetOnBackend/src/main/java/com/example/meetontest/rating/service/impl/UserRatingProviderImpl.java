package com.example.meetontest.rating.service.impl;

import com.example.meetontest.entities.User;
import com.example.meetontest.rating.component.UserRatingComponent;
import com.example.meetontest.rating.service.UserRatingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.DoubleStream;

@Service
public class UserRatingProviderImpl implements UserRatingProvider {
    @Autowired
    @Qualifier("userRatingComponents")
    private List<UserRatingComponent> userRatingComponents;

    @Override
    public double getUserRating(User user) {
        double doubleStream = userRatingComponents.stream().mapToDouble(component -> component.getUserRating(user)).sum();
        return doubleStream;
    }
}
