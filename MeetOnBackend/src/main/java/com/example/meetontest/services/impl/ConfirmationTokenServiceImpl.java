package com.example.meetontest.services.impl;

import com.example.meetontest.entities.ConfirmationToken;
import com.example.meetontest.entities.User;
import com.example.meetontest.repositories.ConfirmationTokenRepository;
import com.example.meetontest.services.ConfirmationTokenService;
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
