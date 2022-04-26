package com.meeton.core.notifications;

import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.notifications.converters.NotificationConverter;
import com.meeton.core.notifications.dto.NotificationDTO;
import com.meeton.core.notifications.entities.NotificationStatus;
import com.meeton.core.notifications.services.NotificationService;
import com.meeton.core.services.UserService;
import com.meeton.core.exceptions.ValidatorException;
import com.meeton.core.notifications.converters.NotificationConverter;
import com.meeton.core.notifications.dto.NotificationDTO;
import com.meeton.core.notifications.entities.NotificationStatus;
import com.meeton.core.notifications.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "meeton-core/v1/notifications")
@RequiredArgsConstructor
public class NotificationStatusController {
    private final NotificationService notificationService;
    private final NotificationConverter notificationConverter;
    private final UserService userService;

    @GetMapping("/byUser/{id}")
    public List<NotificationDTO> getByUser(@PathVariable Long id, @RequestParam @Nullable String status) {
        return notificationService.getByUserAndStatus(userService.getUserById(id), parseStatus(status)).stream().
                map(notificationConverter::convertBack).collect(Collectors.toList());
    }

    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeStatusById(@PathVariable Long id, @RequestParam String status) {
        notificationService.changeNotificationsStatus(notificationService.getById(id), parseStatus(status));
        return ResponseEntity.ok(notificationConverter.convertBack(notificationService.getById(id)));
    }

    private NotificationStatus parseStatus(String status) throws ValidatorException {
        try {
            return status != null ? NotificationStatus.valueOf(status.toUpperCase()) : null;

        } catch (IllegalArgumentException e) {
            throw new ValidatorException("Incorrect status!");
        }
    }
}
