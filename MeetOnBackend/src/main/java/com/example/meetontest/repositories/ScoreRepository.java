package com.example.meetontest.repositories;

import com.example.meetontest.dto.AggregatedScoreDTO;
import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingScore;
import com.example.meetontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreRepository extends JpaRepository<MeetingScore, Long> {
    List<MeetingScore> findByMeeting(Meeting meeting);
    List<MeetingScore> findByUser(User user);
    boolean existsByMeetingAndUser(Meeting meeting, User user);
    MeetingScore findFirstByMeetingAndUser(Meeting meeting, User user);

    @Query("SELECT new com.example.meetontest.dto.AggregatedScoreDTO(s.meeting.id, COUNT(s), AVG(s.score)) FROM MeetingScore s WHERE s.meeting.id = ?1 GROUP BY s.meeting.id")
    AggregatedScoreDTO aggregateByMeeting(Long meetingIdz);
}
