package com.meeton.core.rating.service.impl;

import com.meeton.core.dto.MeetingComponents;
import com.meeton.core.dto.UserComponents;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingRole;
import com.meeton.core.entities.MeetingScore;
import com.meeton.core.entities.User;
import com.meeton.core.rating.service.UserRatingProvider;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.RequestService;
import com.meeton.core.services.ScoreService;
import com.meeton.core.services.client.ServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RatingManagerProvider implements UserRatingProvider {
    private final MeetingService meetingService;
    private final RequestService requestService;
    private final ScoreService scoreService;
    private final ServiceClient<Double, UserComponents> ratingManagerClient;

    @Override
    public double getUserRating(User user) {
        List<Meeting> meetings = meetingService.getMeetingsByManager(user);
        List<MeetingComponents> meetingComponents = new ArrayList<>();

        for (Meeting meeting : meetings) {
            List<MeetingScore> scores = scoreService.getScoresByMeeting(meeting);

            long participantCount = requestService.getByMeeting(meeting).stream().filter(request -> request.getRole().equals(MeetingRole.PARTICIPANT)).count();
            double scoresSum = scores.stream().mapToInt(MeetingScore::getScore).sum();
            long scoresCount = scores.size();

            meetingComponents.add(new MeetingComponents(participantCount, scoresCount, scoresSum));
        }

        UserComponents userComponents = UserComponents.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .meetingComponents(meetingComponents)
                .build();

        return ratingManagerClient.execute(userComponents);
    }
}
