package com.meeton.core.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeton.core.dto.SignupRequest;
import com.meeton.core.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "users", groupId = "new_user_group")
    public void consume(String message) throws IOException {

        log.info(String.format("#### -> Consumed message -> %s", message));
        SignupRequest signupRequest = objectMapper.readValue(message, SignupRequest.class);
        authService.registerUser(signupRequest);
    }
}
