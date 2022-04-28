package com.meeton.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDTO {
    private String id;
    private String subject;
    private String content;
    private UserDTO user;
}
