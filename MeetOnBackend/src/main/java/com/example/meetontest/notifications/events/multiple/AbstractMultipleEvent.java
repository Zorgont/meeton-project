package com.example.meetontest.notifications.events.multiple;

import com.example.meetontest.notifications.entities.EventEntity;
import com.example.meetontest.notifications.events.AbstractEvent;

import java.util.List;
import java.util.Map;

public interface AbstractMultipleEvent<D, T> extends AbstractEvent<T> {
    Map<D, List<EventEntity>> preprocess(List<EventEntity> events);
    void process(D key, List<EventEntity> events);
}
