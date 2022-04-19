package com.example.meetontest.converters;

import java.text.ParseException;

public interface Converter<T, V> {
    T convert(V entity) throws ParseException;

    V convertBack(T entity);
}
