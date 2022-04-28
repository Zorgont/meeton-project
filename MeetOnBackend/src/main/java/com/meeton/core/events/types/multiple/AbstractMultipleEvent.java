package com.meeton.core.events.types.multiple;

import com.meeton.core.entities.EventEntity;
import com.meeton.core.events.types.AbstractEvent;

import java.util.List;
import java.util.Map;

public interface AbstractMultipleEvent<D, T> extends AbstractEvent<T> {
    Map<D, List<EventEntity>> preprocess(List<EventEntity> events);
    void process(D key, List<EventEntity> events);
}
