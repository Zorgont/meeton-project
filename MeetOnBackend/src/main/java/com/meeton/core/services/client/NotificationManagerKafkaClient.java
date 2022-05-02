package com.meeton.core.services.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeton.core.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationManagerKafkaClient extends AbstractKafkaProducer<NotificationDTO> {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    private static final String TOPIC_NOTIFICATIONS = "notifications";

    public Void execute(NotificationDTO dto) {
        try {
            String message = mapper.writer().writeValueAsString(dto);
            super.sendMessage(kafkaTemplate, TOPIC_NOTIFICATIONS, message);
            log.info("Notification was successfully transmitted to Kafka!");
            log.info(message);
        } catch (Exception e) {
            log.error("Failed to transmit message to Kafka: {}", e.getMessage());
        }
        return null;
    }
}
