package com.meeton.ratingmanager.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class RatingWeight {
    @Id
    private String id;

    private RatingWeightType type;
    private Double value;
}
