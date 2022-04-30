package com.meeton.ratingmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RatingManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatingManagerApplication.class, args);
    }

}
