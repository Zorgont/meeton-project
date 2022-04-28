package com.meeton.notification.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document
public class Notification {
    @Id
    private String id;

    private String subject;
    private Date date;
    private String content;
    private Long userId;
    private String userEmail;
    private String username;
    private NotificationStatus status;
}
