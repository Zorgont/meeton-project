package com.meeton.core.services.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public abstract class AbstractKafkaProducer<DTO> implements ServiceClient<Void, DTO> {
    protected void sendMessage(KafkaTemplate<String, String> kafkaTemplate, String topic, String message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        kafkaTemplate.send(topic, message);
    }
}
