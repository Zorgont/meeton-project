package com.example.meetontest.services.impl;

import com.example.meetontest.dto.AggregatedScoreDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingScore;
import com.example.meetontest.entities.User;
import com.example.meetontest.exceptions.ValidatorException;
import com.example.meetontest.repositories.ScoreRepository;
import com.example.meetontest.services.MeetingService;
import com.example.meetontest.services.ScoreService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserService userService;

    public Optional<MeetingScore> getScoreById(Long id) {
        return scoreRepository.findById(id);
    }

    public MeetingScore createScore(Meeting meeting, User user, int score) {
        try {
            Meeting met = meetingService.getMeetingById(meeting.getId());
            User usr = userService.getUserById(user.getId());

            MeetingScore meetingScore;
            if (scoreRepository.existsByMeetingAndUser(met, usr)) {
                meetingScore = scoreRepository.findFirstByMeetingAndUser(met, usr);
                meetingScore.setScore(score);
                meetingScore.setDate(new Date());
            }
            else
                meetingScore = new MeetingScore(met, usr, score, new Date());

            return scoreRepository.save(meetingScore);
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage());
        }
    }

    public List<MeetingScore> getScoresByMeeting(Meeting meeting) {
        return scoreRepository.findByMeeting(meeting);
    }

    public List<MeetingScore> getScoresByUser(User user) {
        return scoreRepository.findByUser(user);
    }

    @Override
    public MeetingScore getByMeetingAndUser(Meeting meeting, User user) {
        return scoreRepository.findFirstByMeetingAndUser(meeting, user);
    }

    @Override
    public AggregatedScoreDTO getAggregatedScoreByMeeting(Meeting meeting) {
        return scoreRepository.aggregateByMeeting(meeting.getId());
    }
}