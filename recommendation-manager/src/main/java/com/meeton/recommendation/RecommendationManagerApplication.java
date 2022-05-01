package com.meeton.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RecommendationManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationManagerApplication.class, args);
    }

}
