package com.meeton.core.rating.config;

import com.meeton.core.rating.component.UserRatingComponent;
import com.meeton.core.rating.component.impl.MeetingAttendanceUserRatingComponent;
import com.meeton.core.rating.component.impl.MeetingCreationUserRatingComponent;
import com.meeton.core.rating.component.impl.MeetingScoresUserRatingComponent;
import com.meeton.core.rating.component.UserRatingComponent;
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
