package com.meeton.core.services.impl;

import com.meeton.core.dto.AggregatedScoreDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingScore;
import com.meeton.core.entities.User;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.repositories.ScoreRepository;
import com.meeton.core.services.MeetingService;
import com.meeton.core.services.ScoreService;
import com.meeton.core.services.UserService;
import com.meeton.core.dto.AggregatedScoreDTO;
import com.meeton.core.exceptions.ValidatorException;
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