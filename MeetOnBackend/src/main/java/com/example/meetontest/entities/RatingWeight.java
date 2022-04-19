package com.example.meetontest.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RatingWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RatingWeightType type;
    private Double value;

    public RatingWeight(RatingWeightType type, Double value) {
        this.type = type;
        this.value = value;
    }
}
