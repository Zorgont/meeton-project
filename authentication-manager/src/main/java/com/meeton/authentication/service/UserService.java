package com.meeton.authentication.service;

import com.meeton.authentication.model.dto.LoginRequest;
import com.meeton.authentication.model.dto.UserToken;
import com.meeton.authentication.model.dto.SignUpRequest;
import com.meeton.authentication.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    UserDetails loadUserByEmail(String email);
    Optional<User> getUserByEmail(String email);
    User save(User user);
    User createUser(SignUpRequest signUpRequest);
    UserToken login(LoginRequest loginRequest);
    void confirmUser(String token);
}
