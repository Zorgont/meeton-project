package com.example.meetontest.repositories;

import com.example.meetontest.entities.ConfirmationToken;
import com.example.meetontest.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationTokenIgnoreCase(String confirmationToken);
    void deleteByConfirmationToken(String token);
    List<ConfirmationToken> findAll();
}