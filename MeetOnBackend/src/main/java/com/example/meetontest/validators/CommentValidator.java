package com.example.meetontest.validators;

import com.example.meetontest.dto.CommentDTO;



public interface CommentValidator {
    void validate(CommentDTO comment) throws IllegalAccessException, NoSuchFieldException;
}
