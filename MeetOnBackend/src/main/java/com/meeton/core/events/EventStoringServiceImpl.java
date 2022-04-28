package com.meeton.core.events;

import com.meeton.core.entities.EventEntity;
import com.meeton.core.entities.EventStatus;
import com.meeton.core.repositories.EventRepository;
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
