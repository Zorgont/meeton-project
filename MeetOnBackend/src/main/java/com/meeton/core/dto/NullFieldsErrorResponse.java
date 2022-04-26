package com.meeton.core.dto;

import java.util.List;

public class NullFieldsErrorResponse {
    public String message;
    public List<String> nullFields;

    public NullFieldsErrorResponse(String message, List<String> nullFields) {
        this.message = message;
        this.nullFields = nullFields;
    }
}
