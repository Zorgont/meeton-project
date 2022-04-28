package com.meeton.notification.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDTO {
    private String id;
    private String subject;
    private String date;
    private String content;
    private UserDTO user;
    private String status;
}
