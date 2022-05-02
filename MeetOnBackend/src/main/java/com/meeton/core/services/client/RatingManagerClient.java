package com.meeton.core.services.client;

import com.meeton.core.config.AppConfig;
import com.meeton.core.dto.UserComponents;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RatingManagerClient extends AbstractRestClient<Double, UserComponents> {
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;

    public Double execute(UserComponents dto) {
        ResponseEntity<String> response =
                super.execute(restTemplate, dto, appConfig.getApiGatewayUrl() + "/rating-manager/v1/rating");

        if (response != null && response.hasBody()) {
            return Double.parseDouble(response.getBody());
        } else {
            return (double) 0;
        }
    }
}
