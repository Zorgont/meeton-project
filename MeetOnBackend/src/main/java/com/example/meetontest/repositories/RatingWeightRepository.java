package com.example.meetontest.repositories;

import com.example.meetontest.entities.RatingWeight;
import com.example.meetontest.entities.RatingWeightType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingWeightRepository extends JpaRepository<RatingWeight, Long> {
    RatingWeight findByType(RatingWeightType type);
}
