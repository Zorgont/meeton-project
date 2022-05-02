package com.meeton.core.services.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
public abstract class AbstractRestClient<R, DTO> implements ServiceClient<R, DTO> {
    protected ResponseEntity<String> execute(RestTemplate restTemplate, DTO dto, String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(dto);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(URI.create(url), entity, String.class);
            log.info("Rest request to {} is processed successfully", url);
            log.debug(response.toString());
            return response;
        } catch (Exception e) {
            log.info("Rest request to {} processing failed! Details: {}", url, e.getMessage());
            return null;
        }
    }
}
