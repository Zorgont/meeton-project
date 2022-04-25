package com.spring.login.service;

import com.spring.login.model.LoginRequest;
import com.spring.login.model.SignUpRequest;
import com.spring.login.model.User;
import com.spring.login.model.UserToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {
    UserDetails loadUserByEmail(String email);
    Optional<User> getUserByEmail(String email);
    User save(User user);
    User createUser(SignUpRequest signUpRequest);
    UserToken login(LoginRequest loginRequest);
}
