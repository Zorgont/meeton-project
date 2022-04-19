package com.example.meetontest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(length = 1000)
    private String about;
    @JsonIgnore
    private String password;
    private String email;
    private String firstName;
    private String secondName;
    private double karma;
    private String status;
    private Boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Request> requests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MeetingScore> scores;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TagGroup> prefs;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private ImageModel avatar;

    public User(String username, String password, String email, Boolean isEnabled, String about, String firstName, String secondName, double karma, String status, Set<Role> roles, Set<TagGroup> prefs) {
        this.username = username;
        this.about = about;
        this.password = password;
        this.email = email;
        this.isEnabled =  isEnabled;
        this.firstName = firstName;
        this.secondName = secondName;
        this.karma = karma;
        this.status = status;
        this.roles = roles;
        this.prefs = prefs;
    }

    public User(String username, String password, String email, Boolean isEnabled) {
        this.username = username;
        this.about = about;
        this.password = password;
        this.email = email;
        this.isEnabled =  isEnabled;
    }
}