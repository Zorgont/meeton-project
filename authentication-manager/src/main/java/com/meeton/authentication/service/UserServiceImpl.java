package com.meeton.authentication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meeton.authentication.config.AppConfig;
import com.meeton.authentication.exception.ResourceNotFoundException;
import com.meeton.authentication.model.AuthProvider;
import com.meeton.authentication.model.LoginRequest;
import com.meeton.authentication.model.UserToken;
import com.meeton.authentication.repository.UserRepository;
import com.meeton.authentication.security.TokenProvider;
import com.meeton.authentication.security.UserPrincipal;
import com.meeton.authentication.security.oauth2.CustomOAuth2UserService;
import com.meeton.authentication.model.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AppConfig appConfig;
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final KafkaMessageProducer kafkaMessageProducer;

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
                .confirmationToken(UUID.randomUUID().toString())
                // todo: Add e-mail verification
                .emailVerified(false)
                .password(encoder.encode(signUpRequest.getPassword()))
                .provider(AuthProvider.platform)
                .build();

        return save(user);
    }

    public User save(User user) {
        if (!user.getUsername().equals(CustomOAuth2UserService.MOCK_USERNAME)) {
            if (user.getProvider().equals(AuthProvider.platform)) {
                user.setValid(user.getEmailVerified());
                if (!user.isValid()) {
                    sendConfirmationUserNotification(user);
                }
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
            existingUser.setValid(user.isValid());
            user = existingUser;
        }

        user = userRepository.save(user);
        if (user.isValid()) {
            sendNewUserNotification(user);
        }

        return user;
    }

    @Override
    public void confirmUser(String token) {
        User user = userRepository.findByConfirmationToken(token).get();
        user.setConfirmationToken(null);
        user.setEmailVerified(true);
        save(user);
    }

    @SneakyThrows
    private void sendNewUserNotification(User user) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode node = objectMapper.valueToTree(user);
        ((ObjectNode)node).remove("id");

        kafkaMessageProducer.sendMessage(objectMapper.writer().writeValueAsString(node));
        log.info("Notification successfully transmitted!");
    }

    @SneakyThrows
    private void sendConfirmationUserNotification(User user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = "<p>To confirm your account, please click here</p><br><a href='https://localhost:3000/confirm?token=" + user.getConfirmationToken().toString() + "'>Click here!</a>";

            URI uri = new URIBuilder(appConfig.getMeetonCoreUrl() + "/notification-manager/v1/notification/email")
                    .addParameter("email", user.getEmail())
                    .addParameter("subject", "Confirm your account")
                    .build();

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
            log.info("Notification successfully transmitted!");
            log.debug(response.toString());
        } catch (Exception e) {
            log.info("Notification processing failed! Details: {}", e.getMessage());
        }
    }
}
