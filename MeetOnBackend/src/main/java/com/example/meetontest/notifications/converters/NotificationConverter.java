package com.example.meetontest.notifications.converters;

import com.example.meetontest.converters.Converter;
import com.example.meetontest.notifications.dto.NotificationDTO;
import com.example.meetontest.notifications.entities.Notification;
import com.example.meetontest.notifications.entities.NotificationStatus;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
public class NotificationConverter implements Converter<Notification, NotificationDTO> {
    private final UserService userService;
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @Override
    public Notification convert(NotificationDTO entity) throws ParseException {
        Notification notification = new Notification();
        notification.setContent(entity.getContent());
        notification.setDate(df.parse(entity.getDate()));
        notification.setStatus(NotificationStatus.valueOf((entity.getStatus().toUpperCase())));
        notification.setUser(userService.getUserById(entity.getUser_id()));
        return notification;
    }

    @Override
    public NotificationDTO convertBack(Notification entity) {
        return new NotificationDTO(entity.getId(), df.format(entity.getDate()), entity.getContent(), entity.getUser().getId(), entity.getStatus().toString());
    }
}
