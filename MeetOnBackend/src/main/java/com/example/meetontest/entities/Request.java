package com.example.meetontest.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long request_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    @NonNull
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @Column(length = 1000)
    private String about;

    @Enumerated(EnumType.STRING)
    @NonNull
    private MeetingRole role;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @NonNull
    private RequestStatus status;
}