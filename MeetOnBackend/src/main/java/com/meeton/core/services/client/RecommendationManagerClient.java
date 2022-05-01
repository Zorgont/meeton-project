package com.meeton.core.services.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeton.core.config.AppConfig;
import com.meeton.core.dto.MeetingDTO;
import com.meeton.core.dto.RecommendationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationManagerClient extends AbstractRestClient<RecommendationRequestDTO> {
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;

    public List<List<MeetingDTO>> calculateRecommendations(RecommendationRequestDTO dto) {
        ResponseEntity<String> response =
                super.execute(restTemplate, dto, appConfig.getApiGatewayUrl() + "/recommendation-manager/v1/recommendation");

        try {
            return new ObjectMapper().readValue(response.getBody(), new TypeReference<List<List<MeetingDTO>>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
