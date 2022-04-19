package com.example.meetontest.rating.config;

import com.example.meetontest.rating.component.UserRatingComponent;
import com.example.meetontest.rating.component.impl.MeetingAttendanceUserRatingComponent;
import com.example.meetontest.rating.component.impl.MeetingCreationUserRatingComponent;
import com.example.meetontest.rating.component.impl.MeetingScoresUserRatingComponent;
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
