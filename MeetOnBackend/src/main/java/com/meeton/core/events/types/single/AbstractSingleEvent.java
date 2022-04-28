package com.meeton.core.events.types.single;

import com.meeton.core.entities.EventEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.ParseException;

public interface AbstractSingleEvent<T> extends com.meeton.core.events.types.AbstractEvent<T> {
    void process(EventEntity event) throws JsonProcessingException, ParseException;
}
