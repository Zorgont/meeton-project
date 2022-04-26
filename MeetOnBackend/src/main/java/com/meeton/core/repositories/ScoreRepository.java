package com.meeton.core.repositories;

import com.meeton.core.dto.AggregatedScoreDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingScore;
import com.meeton.core.entities.User;
import com.meeton.core.dto.AggregatedScoreDTO;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingScore;
import com.meeton.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreRepository extends JpaRepository<MeetingScore, Long> {
    List<MeetingScore> findByMeeting(Meeting meeting);
    List<MeetingScore> findByUser(User user);
    boolean existsByMeetingAndUser(Meeting meeting, User user);
    MeetingScore findFirstByMeetingAndUser(Meeting meeting, User user);

    @Query("SELECT new com.meeton.core.dto.AggregatedScoreDTO(s.meeting.id, COUNT(s), AVG(s.score)) FROM MeetingScore s WHERE s.meeting.id = ?1 GROUP BY s.meeting.id")
    AggregatedScoreDTO aggregateByMeeting(Long meetingIdz);
}
