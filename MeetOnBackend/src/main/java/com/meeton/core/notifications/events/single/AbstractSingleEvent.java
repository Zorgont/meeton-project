package com.meeton.core.notifications.events.single;

import com.meeton.core.notifications.entities.EventEntity;
import com.meeton.core.notifications.events.AbstractEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.meeton.core.notifications.entities.EventEntity;

import java.text.ParseException;

public interface AbstractSingleEvent<T> extends AbstractEvent<T> {
    void process(EventEntity event) throws JsonProcessingException, ParseException;
}
