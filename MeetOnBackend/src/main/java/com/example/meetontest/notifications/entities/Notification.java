package com.example.meetontest.notifications.entities;

import com.example.meetontest.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    public Notification(Date date, String content, @NonNull User user) {
        this.date = date;
        this.content = content;
        this.user = user;
        this.status = NotificationStatus.UNVIEWED;
    }
}
