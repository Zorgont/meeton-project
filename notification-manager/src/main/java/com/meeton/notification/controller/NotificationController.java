package com.meeton.notification.controller;

import com.meeton.notification.converter.NotificationConverter;
import com.meeton.notification.model.dto.NotificationDTO;
import com.meeton.notification.model.entity.NotificationStatus;
import com.meeton.notification.service.EmailService;
import com.meeton.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "notification-manager/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationConverter notificationConverter;
    private final EmailService emailService;

    @PostMapping
    public void createNotification(@RequestBody NotificationDTO notification) throws ParseException {
        notificationService.createNotification(notificationConverter.convert(notification));
    }

    @PostMapping("/user")
    public void sendEmail(@RequestParam String email, @RequestParam String subject, @RequestParam String message) {
        emailService.sendMessage(email, subject, message);
    }

    @GetMapping("/byUser/{id}")
    public List<NotificationDTO> getByUser(@PathVariable Long id, @RequestParam @Nullable String status) {
        return notificationService.getByUserAndStatus(id, parseStatus(status)).stream().
                map(notificationConverter::convertBack).collect(Collectors.toList());
    }

    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeStatusById(@PathVariable String id, @RequestParam String status) {
        notificationService.changeNotificationsStatus(id, parseStatus(status));
        return ResponseEntity.ok(notificationConverter.convertBack(notificationService.getById(id)));
    }

    private NotificationStatus parseStatus(String status) throws RuntimeException {
        try {
            return status != null ? NotificationStatus.valueOf(status.toUpperCase()) : null;

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Incorrect status!");
        }
    }
}

