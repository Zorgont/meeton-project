package com.meeton.core.services;

import com.meeton.core.dto.SignupRequest;

public interface AuthService {
    void registerUser(SignupRequest signUpRequest);
}