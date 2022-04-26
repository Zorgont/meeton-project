package com.meeton.core.repositories;

import com.meeton.core.entities.ERole;
import com.meeton.core.entities.Role;
import com.meeton.core.entities.ERole;
import com.meeton.core.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}