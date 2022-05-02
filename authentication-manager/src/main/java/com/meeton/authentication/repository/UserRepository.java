package com.meeton.authentication.repository;

import com.meeton.authentication.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByConfirmationToken(String confirmationToken);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
