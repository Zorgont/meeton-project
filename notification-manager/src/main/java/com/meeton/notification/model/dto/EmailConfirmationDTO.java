package com.meeton.notification.model.dto;

import lombok.Data;

@Data
public class EmailConfirmationDTO {
    String email;
    String subject;
    String message;
}
