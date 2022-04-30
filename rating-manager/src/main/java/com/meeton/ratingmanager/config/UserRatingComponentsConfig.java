package com.meeton.ratingmanager.config;

import com.meeton.ratingmanager.service.UserRatingComponent;
import com.meeton.ratingmanager.service.impl.MeetingAttendanceUserRatingComponent;
import com.meeton.ratingmanager.service.impl.MeetingCreationUserRatingComponent;
import com.meeton.ratingmanager.service.impl.MeetingScoresUserRatingComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UserRatingComponentsConfig {
    private final MeetingAttendanceUserRatingComponent meetingAttendanceUserRatingComponent;
    private final MeetingCreationUserRatingComponent meetingCreationUserRatingComponent;
    private final MeetingScoresUserRatingComponent meetingScoresUserRatingComponent;

    @Bean
    public List<UserRatingComponent> userRatingComponents() {
        List<UserRatingComponent> result = new ArrayList<>();
        result.add(meetingAttendanceUserRatingComponent);
        result.add(meetingCreationUserRatingComponent);
        result.add(meetingScoresUserRatingComponent);
        return result;
    }
}
