package com.spring.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.spring.login.config.AppConfig;
import com.spring.login.exception.ResourceNotFoundException;
import com.spring.login.model.AuthProvider;
import com.spring.login.model.User;
import com.spring.login.repository.UserRepository;
import com.spring.login.security.UserPrincipal;
import com.spring.login.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AppConfig appConfig;

    public UserDetails loadUserByEmail(String email) {
        User user = getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return UserPrincipal.create(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
        }

        user = userRepository.save(user);
        if (user.isValid()) {
            sendNotification(user);
        }

        return user;
    }

    @SneakyThrows
    private void sendNotification(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = new RestTemplate().postForEntity(URI.create(appConfig.getMeetonCoreUrl() + "/api/v1/auth/signup"), entity, String.class);
        log.info("Notification successfully transmitted!");
    }
}
