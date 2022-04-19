package com.example.meetontest.notifications.services;

import com.example.meetontest.notifications.entities.EventEntity;
import com.example.meetontest.notifications.entities.EventStatus;
import com.example.meetontest.notifications.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventStoringServiceImpl implements EventStoringService {
    private final EventRepository eventRepository;

    public void saveEvent(EventEntity event) {
        try {
            eventRepository.save(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EventEntity> getUnhandledEventsByTypes(Set<String> types) {
        return eventRepository.findByStatusAndTypeIn(EventStatus.UNHANDLED, types);
    }

    @Override
    public void updateEvent(EventEntity event) {
        try {
            eventRepository.save(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
