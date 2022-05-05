package com.meeton.authentication.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

@Data
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private List<String> authorizedRedirectUris = new ArrayList<>();
    private String tokenSecret;
    private long tokenExpirationMsec;
    private String meetonCoreUrl;
    private String meetonUiUrl;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        return mapper;
    }
}
