package com.spring.login.service;

import com.spring.login.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {
    UserDetails loadUserByEmail(String email);
    Optional<User> getUserByEmail(String email);
    User save(User user);
}
