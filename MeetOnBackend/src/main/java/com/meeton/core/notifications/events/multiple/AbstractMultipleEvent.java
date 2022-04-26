package com.meeton.core.notifications.events.multiple;

import com.meeton.core.notifications.entities.EventEntity;
import com.meeton.core.notifications.events.AbstractEvent;
import com.meeton.core.notifications.entities.EventEntity;

import java.util.List;
import java.util.Map;

public interface AbstractMultipleEvent<D, T> extends AbstractEvent<T> {
    Map<D, List<EventEntity>> preprocess(List<EventEntity> events);
    void process(D key, List<EventEntity> events);
}
