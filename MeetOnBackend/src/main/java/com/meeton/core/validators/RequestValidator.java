package com.meeton.core.validators;

import com.meeton.core.dto.RequestDTO;
import com.meeton.core.entities.Request;

public interface RequestValidator {
    void validate(RequestDTO request) throws IllegalAccessException, NoSuchFieldException;
}
