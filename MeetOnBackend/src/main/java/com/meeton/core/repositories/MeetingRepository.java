package com.meeton.core.repositories;

import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.Tag;
import com.meeton.core.entities.Meeting;
import com.meeton.core.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByTags(Tag tag);
}