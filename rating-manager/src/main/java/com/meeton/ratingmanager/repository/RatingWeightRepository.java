package com.meeton.ratingmanager.repository;

import com.meeton.ratingmanager.model.entity.RatingWeight;
import com.meeton.ratingmanager.model.entity.RatingWeightType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RatingWeightRepository  extends MongoRepository<RatingWeight, String> {
    Optional<RatingWeight> findByType(RatingWeightType type);
}