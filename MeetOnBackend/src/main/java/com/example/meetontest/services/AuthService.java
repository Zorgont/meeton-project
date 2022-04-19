package com.example.meetontest.services;

import com.example.meetontest.dto.GoogleLoginRequest;
import com.example.meetontest.dto.JwtResponse;
import com.example.meetontest.dto.LoginRequest;
import com.example.meetontest.dto.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    JwtResponse authenticateUserViaGoogle(GoogleLoginRequest loginRequest);
    void confirmUser(String token);
    void registerUser(SignupRequest signUpRequest);
}