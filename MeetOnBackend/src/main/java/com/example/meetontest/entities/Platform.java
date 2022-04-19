package com.example.meetontest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "platforms")
@Getter
@Setter
@NoArgsConstructor
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;
    private String info;
    //    private ??? logo;
    @Enumerated(value = EnumType.STRING)
    private PlatformType type;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "platform", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MeetingPlatform> meetingPlatforms;

    public Platform(String name, String info, PlatformType type) {
        this.name = name;
        this.info = info;
        this.type = type;
    }
}
