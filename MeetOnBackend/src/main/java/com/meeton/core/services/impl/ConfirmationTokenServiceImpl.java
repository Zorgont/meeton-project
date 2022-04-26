package com.meeton.core.services.impl;

import com.meeton.core.entities.ConfirmationToken;
import com.meeton.core.entities.User;
import com.meeton.core.repositories.ConfirmationTokenRepository;
import com.meeton.core.services.ConfirmationTokenService;
import com.meeton.core.entities.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    @Override
    public ConfirmationToken createToken(User user) {
        ConfirmationToken token = new ConfirmationToken(user);
        return confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationToken getByConfirmationToken(String token) {
        return confirmationTokenRepository.findByConfirmationTokenIgnoreCase(token);
    }

    @Override
    public void deleteByConfirmationToken(String token) {
        confirmationTokenRepository.deleteByConfirmationToken(token);
    }

    @Override
    public List<ConfirmationToken> getAll() {
        return confirmationTokenRepository.findAll();
    }
}
