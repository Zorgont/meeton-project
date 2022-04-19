package com.example.meetontest.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String date;
    private String content;
    private Long user_id;
    private String status;
}
