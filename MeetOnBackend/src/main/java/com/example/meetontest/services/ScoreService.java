package com.example.meetontest.services;

import com.example.meetontest.dto.AggregatedScoreDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingScore;
import com.example.meetontest.entities.User;

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
