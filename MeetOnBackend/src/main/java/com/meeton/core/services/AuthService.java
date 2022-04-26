package com.meeton.core.services;

import com.meeton.core.dto.GoogleLoginRequest;
import com.meeton.core.dto.JwtResponse;
import com.meeton.core.dto.LoginRequest;
import com.meeton.core.dto.SignupRequest;
import com.meeton.core.dto.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    //JwtResponse authenticateUserViaGoogle(GoogleLoginRequest loginRequest);
    void confirmUser(String token);
    void registerUser(SignupRequest signUpRequest);
}