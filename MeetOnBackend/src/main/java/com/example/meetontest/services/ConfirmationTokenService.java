package com.example.meetontest.services;

import com.example.meetontest.entities.ConfirmationToken;
import com.example.meetontest.entities.User;

import java.util.List;

public interface ConfirmationTokenService {
    ConfirmationToken createToken(User user);
    ConfirmationToken getByConfirmationToken(String token);
    void deleteByConfirmationToken(String token);
    List<ConfirmationToken> getAll();

}
