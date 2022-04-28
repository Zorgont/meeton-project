package com.meeton.core.validators;

import com.meeton.core.dto.CommentDTO;

public interface CommentValidator {
    void validate(CommentDTO comment) throws IllegalAccessException, NoSuchFieldException;
}
