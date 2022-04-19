package com.example.meetontest.repositories;

import com.example.meetontest.entities.Meeting;
import com.example.meetontest.entities.MeetingPlatform;
import com.example.meetontest.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingPlatformsRepository extends JpaRepository<MeetingPlatform, Long> {
    List<MeetingPlatform> findByMeeting(Meeting meeting);

    List<MeetingPlatform> findByPlatform(Platform platform);
}
