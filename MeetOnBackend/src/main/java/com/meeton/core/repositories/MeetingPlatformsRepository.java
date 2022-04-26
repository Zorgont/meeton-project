package com.meeton.core.repositories;

import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingPlatform;
import com.meeton.core.entities.Platform;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.MeetingPlatform;
import com.meeton.core.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingPlatformsRepository extends JpaRepository<MeetingPlatform, Long> {
    List<MeetingPlatform> findByMeeting(Meeting meeting);

    List<MeetingPlatform> findByPlatform(Platform platform);
}
