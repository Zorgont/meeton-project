package com.example.meetontest.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(length = 20)
    @NonNull
    private String name;
}