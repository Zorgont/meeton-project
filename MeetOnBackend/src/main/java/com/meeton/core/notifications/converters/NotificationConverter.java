package com.meeton.core.notifications.converters;

import com.meeton.core.converters.Converter;
import com.meeton.core.notifications.dto.NotificationDTO;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.entities.NotificationStatus;
import com.meeton.core.services.UserService;
import com.meeton.core.converters.Converter;
import com.meeton.core.notifications.entities.Notification;
import com.meeton.core.notifications.entities.NotificationStatus;
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
