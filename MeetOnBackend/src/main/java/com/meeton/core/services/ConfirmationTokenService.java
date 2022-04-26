package com.meeton.core.services;

import com.meeton.core.entities.ConfirmationToken;
import com.meeton.core.entities.User;
import com.meeton.core.entities.ConfirmationToken;

import java.util.List;

public interface ConfirmationTokenService {
    ConfirmationToken createToken(User user);
    ConfirmationToken getByConfirmationToken(String token);
    void deleteByConfirmationToken(String token);
    List<ConfirmationToken> getAll();

}
