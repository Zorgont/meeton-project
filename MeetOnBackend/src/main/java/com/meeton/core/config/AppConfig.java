package com.meeton.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Data
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String jwtSecret;

    private long jwtExpirationMs;
}
