package com.example.meetontest.notifications.services;

import com.example.meetontest.notifications.entities.EventEntity;

import java.util.List;
import java.util.Set;

public interface EventStoringService {
    void saveEvent(EventEntity event);
    List<EventEntity> getUnhandledEventsByTypes(Set<String> types);
    void updateEvent(EventEntity event);
}
