package com.example.meetontest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "meetings")
@Getter
@Setter
@NoArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date date;
    private Date endDate;
    @Column(length = 1000)
    private String about;
    private Boolean isParticipantAmountRestricted;
    private int participantAmount;
    private Boolean isPrivate;
    @Column(length = 1000)
    private String details;

    @Enumerated(EnumType.STRING)
    private MeetingStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "meeting_tags",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "meeting", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Request> requests;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "meeting", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<MeetingPlatform> meetingPlatforms;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MeetingScore> scores;

    public Meeting(String name, Date date, Date endDate, String about, boolean isParticipantAmountRestricted, int participantAmount, boolean isPrivate, String details, MeetingStatus status, Set<Tag> tags) {
        this.name = name;
        this.date = date;
        this.endDate = endDate;
        this.about = about;
        this.isParticipantAmountRestricted = isParticipantAmountRestricted;
        this.participantAmount = participantAmount;
        this.isPrivate = isPrivate;
        this.details = details;
        this.status = status;
        this.tags = tags;
    }

    public void addMeetingPlatform(MeetingPlatform meetingPlatform) {
        meetingPlatforms.add(meetingPlatform);
        meetingPlatform.setMeeting(this);
    }

    public void removeMeetingPlatform(MeetingPlatform meetingPlatform) {
        meetingPlatforms.remove(meetingPlatform);
        meetingPlatform.setMeeting(null);
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", endDate=" + endDate +
                ", about='" + about + '\'' +
                ", isParticipantAmountRestricted=" + isParticipantAmountRestricted +
                ", participantAmount=" + participantAmount +
                ", isPrivate=" + isPrivate +
                ", details='" + details + '\'' +
                ", status=" + status +
                '}';
    }
}