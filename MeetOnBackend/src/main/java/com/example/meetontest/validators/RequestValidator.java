package com.example.meetontest.validators;

import com.example.meetontest.dto.RequestDTO;
import com.example.meetontest.entities.Request;

public interface RequestValidator {
    void validate(RequestDTO request) throws IllegalAccessException, NoSuchFieldException;
}
