package com.meeton.core.repositories;

import com.meeton.core.entities.RatingWeight;
import com.meeton.core.entities.RatingWeightType;
import com.meeton.core.entities.RatingWeight;
import com.meeton.core.entities.RatingWeightType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingWeightRepository extends JpaRepository<RatingWeight, Long> {
    RatingWeight findByType(RatingWeightType type);
}
