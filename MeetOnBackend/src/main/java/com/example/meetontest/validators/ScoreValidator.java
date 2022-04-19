package com.example.meetontest.validators;

import com.example.meetontest.dto.ScoreDTO;

public interface ScoreValidator {
    void validate(ScoreDTO score) throws IllegalAccessException, NoSuchFieldException;
}
