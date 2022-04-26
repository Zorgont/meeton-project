package com.meeton.core.services;

import com.meeton.core.dto.AggregatedScoreDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingScore;
import com.meeton.core.entities.User;
import com.meeton.core.dto.AggregatedScoreDTO;

import java.util.List;
import java.util.Optional;

public interface ScoreService {
    Optional<MeetingScore> getScoreById(Long id);

    MeetingScore createScore(Meeting meeting, User user, int score);

    List<MeetingScore> getScoresByMeeting(Meeting meeting);

    List<MeetingScore> getScoresByUser(User user);

    MeetingScore getByMeetingAndUser(Meeting meeting, User user);

    AggregatedScoreDTO getAggregatedScoreByMeeting(Meeting meeting);
}
