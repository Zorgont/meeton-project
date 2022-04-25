package com.spring.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.spring.login.config.AppConfig;
import com.spring.login.exception.ResourceNotFoundException;
import com.spring.login.model.*;
import com.spring.login.repository.UserRepository;
import com.spring.login.security.TokenProvider;
import com.spring.login.security.UserPrincipal;
import com.spring.login.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AppConfig appConfig;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserDetails loadUserByEmail(String email) {
        User user = getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return UserPrincipal.create(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserToken login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isValid()) {
                if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    return new UserToken(user, tokenProvider.createToken(user.getEmail()));
                } else {
                    throw new RuntimeException("Password is not match!");
                }
            } else {
                throw new RuntimeException("User is not active!");
            }
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    public User createUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                // todo: Add e-mail verification
                .emailVerified(true)
                .password(encoder.encode(signUpRequest.getPassword()))
                .provider(AuthProvider.platform)
                .build();

        return save(user);
    }

    public User save(User user) {
        if (!user.getUsername().equals(CustomOAuth2UserService.MOCK_USERNAME)) {
            if (user.getProvider().equals(AuthProvider.platform)) {
                user.setValid(user.getEmailVerified());
            } else {
                user.setValid(!StringUtils.isEmpty(user.getUsername()));
            }
        }

        Optional<User> optionalExistingUser = userRepository.findByEmail(user.getEmail());
        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
            existingUser.setProvider(user.getProvider());
            existingUser.setProviderId(user.getProviderId());
            existingUser.setUsername(user.getUsername());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setSecondName(user.getSecondName());
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setImageUrl(user.getImageUrl());
            user = existingUser;
        }

        user = userRepository.save(user);
        if (user.isValid()) {
            sendNotification(user);
        }

        return user;
    }

    @SneakyThrows
    private void sendNotification(User user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(user);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = new RestTemplate().postForEntity(URI.create(appConfig.getMeetonCoreUrl() + "/api/v1/auth/signup"), entity, String.class);
            log.info("Notification successfully transmitted!");
        } catch (Exception e) {
            log.info("Notification processing failed! Details: {}", e.getMessage());
        }
    }
}
