package com.example.meetontest.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "scores")
@Getter
@Setter
@NoArgsConstructor
public class MeetingScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long score_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    @NonNull
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @NonNull
    private int score;
    private Date date;

    public MeetingScore(Meeting meeting, User user, int score, Date date) {
        this.meeting = meeting;
        this.user = user;
        this.score = score;
        this.date = date;
    }
}
