package com.meeton.authentication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailConfirmationRequest {
    String email;
    String subject;
    String message;
}
