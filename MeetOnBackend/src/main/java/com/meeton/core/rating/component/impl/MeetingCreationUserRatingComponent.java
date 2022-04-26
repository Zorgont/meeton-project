package com.meeton.core.rating.component.impl;

import com.meeton.core.entities.MeetingRole;
import com.meeton.core.entities.RatingWeightType;
import com.meeton.core.entities.User;
import com.meeton.core.rating.component.UserRatingComponent;
import com.meeton.core.services.RatingWeightService;
import com.meeton.core.services.RequestService;
import com.meeton.core.entities.MeetingRole;
import com.meeton.core.entities.RatingWeightType;
import com.meeton.core.entities.User;
import com.meeton.core.rating.component.UserRatingComponent;
import com.meeton.core.services.RatingWeightService;
import com.meeton.core.services.RequestService;
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
