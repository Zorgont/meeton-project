package com.meeton.core.rating.component.impl;

import com.meeton.core.entities.MeetingRole;
import com.meeton.core.entities.RatingWeightType;
import com.meeton.core.entities.Request;
import com.meeton.core.entities.User;
import com.meeton.core.rating.component.UserRatingComponent;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RatingWeightService;
import com.meeton.core.entities.MeetingRole;
import com.meeton.core.entities.RatingWeightType;
import com.meeton.core.entities.User;
import com.meeton.core.rating.component.UserRatingComponent;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RatingWeightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MeetingAttendanceUserRatingComponent implements UserRatingComponent {
    private final MeetingService meetingService;
    private final RatingWeightService weightService;

    @Override
    public double getUserRating(User user) {
        try {
            double result = meetingService.getMeetingsByManager(user).stream()
                    .flatMap(meeting -> meeting.getRequests().stream()).distinct()
                    .filter(request -> request.getRole().equals(MeetingRole.PARTICIPANT))
                    .count() * weightService.getValueByType(RatingWeightType.MEETING_ATTENDANCE);

            return Double.isNaN(result) ? 0 : result;
        } catch (Exception e) {
            System.out.println("Couldn't calculate meeting attendance for userId: " + user.getId());
            return 0;
        }
    }
}
