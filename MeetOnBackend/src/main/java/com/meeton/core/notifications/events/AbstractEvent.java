package com.meeton.core.notifications.events;

import com.meeton.core.notifications.entities.EventEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeton.core.notifications.entities.EventEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface AbstractEvent<T> {
    default EventEntity toEntity(Object source, Date date, T oldValue, T newValue) throws JsonProcessingException {
        Map<String, T> map = new HashMap<>();
        map.put("old", oldValue);
        map.put("new", newValue);
        System.out.println(getClass().getSimpleName());
        return new EventEntity(date, getClass().getSimpleName(), new ObjectMapper().writer().withDefaultPrettyPrinter().
                writeValueAsString(map));
    };
}
