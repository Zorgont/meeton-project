package com.example.meetontest.rating.component.impl;

import com.example.meetontest.entities.MeetingRole;
import com.example.meetontest.entities.RatingWeightType;
import com.example.meetontest.entities.User;
import com.example.meetontest.rating.component.UserRatingComponent;
import com.example.meetontest.services.RatingWeightService;
import com.example.meetontest.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingCreationUserRatingComponent implements UserRatingComponent {
    private final RequestService requestService;
    private final RatingWeightService weightService;

    @Override
    public double getUserRating(User user) {
        try {
            double result = requestService.getByUserAndRole(user, MeetingRole.MANAGER).size() * weightService.getValueByType(RatingWeightType.MEETING_CREATION);
            return Double.isNaN(result) ? 0 : result;
        } catch (Exception e) {
            System.out.println("Couldn't calculate meeting creation for userId: " + user.getId());
            return 0;
        }
    }
}
