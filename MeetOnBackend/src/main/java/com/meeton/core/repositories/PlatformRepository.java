package com.meeton.core.repositories;

import com.meeton.core.entities.Platform;
import com.meeton.core.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Optional<Platform> findByName(String name);

    boolean existsByName(String name);
}
