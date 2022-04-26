package com.meeton.core.notifications.repositories;

import com.meeton.core.notifications.entities.EventEntity;
import com.meeton.core.notifications.entities.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByStatus(EventStatus status);
    List<EventEntity> findByStatusAndTypeIn(EventStatus status, Set<String> types);

}
