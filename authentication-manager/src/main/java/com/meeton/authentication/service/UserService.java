package com.meeton.authentication.service;

import com.meeton.authentication.model.LoginRequest;
import com.meeton.authentication.model.UserToken;
import com.meeton.authentication.model.LoginRequest;
import com.meeton.authentication.model.SignUpRequest;
import com.meeton.authentication.model.User;
import com.meeton.authentication.model.UserToken;
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
