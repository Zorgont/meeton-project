package com.meeton.core.validators;

import com.meeton.core.dto.ScoreDTO;

public interface ScoreValidator {
    void validate(ScoreDTO score) throws IllegalAccessException, NoSuchFieldException;
}
