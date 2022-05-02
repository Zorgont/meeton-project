package com.meeton.notification.converter;

import com.meeton.notification.model.dto.NotificationDTO;
import com.meeton.notification.model.dto.UserDTO;
import com.meeton.notification.model.entity.Notification;
import com.meeton.notification.model.entity.NotificationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConverter {
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public Notification convert(NotificationDTO dto) {
        String subject = "", content = "", userEmail = "", username = "";
        Long userId = 0L;
        Date date;
        NotificationStatus status;

        subject = dto.getSubject();
        content = dto.getContent();
        try {
            date = df.parse(dto.getDate());
        } catch (Exception e) {
            log.warn(e.getMessage());
            date = new Date();
        }
        try {
            status = NotificationStatus.valueOf((dto.getStatus().toUpperCase()));
        } catch (Exception e) {
            log.warn(e.getMessage());
            status = NotificationStatus.UNVIEWED;
        }
        try {
            userId = dto.getUser().getId();
            userEmail = dto.getUser().getEmail();
            username = dto.getUser().getUsername();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return Notification.builder()
                .subject(subject)
                .content(content)
                .date(date)
                .status(status)
                .userId(userId)
                .userEmail(userEmail)
                .username(username)
                .build();
    }

    public NotificationDTO convertBack(Notification entity) {
        return NotificationDTO.builder()
                .id(entity.getId())
                .subject(entity.getSubject())
                .date(df.format(entity.getDate()))
                .content(entity.getContent())
                .user(UserDTO.builder().id(entity.getUserId()).email(entity.getUserEmail()).username(entity.getUsername()).build())
                .status(entity.getStatus().toString())
                .build();
    }
}
