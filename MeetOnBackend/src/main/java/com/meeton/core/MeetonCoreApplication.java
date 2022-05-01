package com.meeton.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
public class MeetonCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetonCoreApplication.class, args);

    }

}