package com.meeton.core.services.impl;

import com.meeton.core.services.UserService;
import com.meeton.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsImpl.build(userService.getUserByName(username));
    }

    public UserDetails loadUserByEmail(String email) {
        return UserDetailsImpl.build(userService.getUserByEmail(email));
    }
}