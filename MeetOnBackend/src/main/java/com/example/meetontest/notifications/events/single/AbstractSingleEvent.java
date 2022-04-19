package com.example.meetontest.notifications.events.single;

import com.example.meetontest.notifications.entities.EventEntity;
import com.example.meetontest.notifications.events.AbstractEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.ParseException;

public interface AbstractSingleEvent<T> extends AbstractEvent<T> {
    void process(EventEntity event) throws JsonProcessingException, ParseException;
}
