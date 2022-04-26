package com.meeton.core.repositories;

import com.meeton.core.entities.ConfirmationToken;
import com.meeton.core.entities.User;
import com.meeton.core.entities.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationTokenIgnoreCase(String confirmationToken);
    void deleteByConfirmationToken(String token);
    List<ConfirmationToken> findAll();
}