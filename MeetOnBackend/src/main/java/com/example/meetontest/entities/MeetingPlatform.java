package com.example.meetontest.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "meeting_platforms")
@Getter
@Setter
@NoArgsConstructor
public class MeetingPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_id")
    private Platform platform;

    private String address;

    public MeetingPlatform(Meeting meeting, Platform platform, String address) {
        this.meeting = meeting;
        this.platform = platform;
        this.address = address;
    }
}
