package com.meeton.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meeton.core.config.AppConfig;
import com.meeton.core.dto.NotificationDTO;
import com.meeton.core.dto.UserComponents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingManagerClient {
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;

    public double calculateRating(UserComponents dto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(dto);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(URI.create(appConfig.getApiGatewayUrl() + "/rating-manager/v1/rating"), entity, String.class);
            log.debug(response.toString());
            log.info("Response from rating-manager successfully received!");
            return Double.parseDouble(response.getBody());
        } catch (Exception e) {
            log.info("Response from rating-manager has errors! Details: {}", e.getMessage());
            return 0;
        }
    }
}
